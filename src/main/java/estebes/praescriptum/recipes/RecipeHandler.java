package estebes.praescriptum.recipes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import net.minecraft.item.ItemStack;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import estebes.praescriptum.Utils.IngredientUtils;
import estebes.praescriptum.Utils.ItemUtils;
import estebes.praescriptum.Utils.LogUtils;
import estebes.praescriptum.ingredients.input.FluidStackInputIngredient;
import estebes.praescriptum.ingredients.input.InputIngredient;
import estebes.praescriptum.ingredients.input.ItemStackInputIngredient;
import estebes.praescriptum.ingredients.output.OutputIngredient;
import net.minecraftforge.fluids.FluidStack;

public class RecipeHandler {
	/**
	 * Create a recipe for this handler.
	 *
	 * @return new recipe object for ease of use
	 */
	public Recipe createRecipe() {
		return new Recipe(this);
	}

	/**
	 * Adds a recipe to this handler.
	 *
	 * @param replace Replace conflicting existing recipes, not recommended, may be ignored
	 * @return True on success, false otherwise, e.g. on conflicts
	 */
	public boolean addRecipe(Recipe recipe, boolean replace) {
		Objects.requireNonNull(recipe.getInputIngredients(), "The input input is null");

		if (recipe.getInputIngredients().size() <= 0) throw new IllegalArgumentException("No inputs");

		Objects.requireNonNull(recipe.getOutputIngredients(), "The input output is null");

		if (recipe.getOutputIngredients().size() <= 0) throw new IllegalArgumentException("No outputs");

		ImmutableList<InputIngredient> listOfInputs = recipe.getInputIngredients().stream()
			.filter(IngredientUtils.isIngredientEmpty((ingredient) ->
				LogUtils.LOGGER.warn(String.format("The %s %s is invalid. Skipping...", ingredient.getClass().getSimpleName(), ingredient.toFormattedString()))))
			.collect(ImmutableList.toImmutableList());

		ImmutableList<OutputIngredient> listOfOutputs = recipe.getOutputIngredients().stream()
			.filter(IngredientUtils.isIngredientEmpty((ingredient) ->
					LogUtils.LOGGER.warn(String.format("The %s %s is invalid. Skipping...", ingredient.getClass().getSimpleName(), ingredient.toFormattedString()))))
			.collect(ImmutableList.toImmutableList());

		Optional<Recipe> temp = getRecipe(listOfInputs);

		if (temp.isPresent()) {
			if (replace) {
				do {
					if (!removeRecipe(listOfInputs))
						LogUtils.LOGGER.error(String.format("Something went wrong while removing the recipe with inputs %s", listOfInputs));
				} while (getRecipe(listOfInputs).isPresent());
			} else {
				LogUtils.LOGGER.error(String.format("Skipping %s => %s due to duplicate input for %s (%s => %s)", listOfInputs, listOfOutputs, listOfInputs, listOfInputs, listOfOutputs));
				return false;
			}
		}

		Recipe newRecipe = createRecipe()
			.withInput(listOfInputs)
			.withOutput(listOfOutputs)
			.withEnergyCostPerTick(recipe.getEnergyCostPerTick())
			.withOperationDuration(recipe.getOperationDuration());

		recipes.add(newRecipe);

		return true;
	}

	/**
	 * Get the recipe for the given ingredients.
	 *
	 * @param ingredients The ingredient list
	 * @return The recipe if it exists or empty otherwise
	 */
	protected Optional<Recipe> getRecipe(ImmutableList<InputIngredient> ingredients) {
		return recipes.stream()
			.filter(recipe -> {
				final List<InputIngredient> listA = new ArrayList<>(recipe.getInputIngredients());
				ingredients.forEach(entry ->
					listA.removeIf(temp -> temp.matches(entry.ingredient) && entry.getCount() >= temp.getCount()));

				return listA.isEmpty();
			})
			.findAny();
	}

	/**
	 * Find a matching recipe for the provided inputs
	 *
	 * @param items
	 * @param fluids
	 * @return
	 */
	public Optional<Recipe> findRecipe(ImmutableList<ItemStack> items, ImmutableList<FluidStack> fluids) {
		Stream<ItemStackInputIngredient> itemIngredients = items.stream()
			.filter(stack -> !ItemUtils.isEmpty(stack))
			.map(ItemStackInputIngredient::copyOf); // map ItemStacks

		Stream<FluidStackInputIngredient> fluidIngredients = fluids.stream()
			.filter(stack -> stack.amount <= 0)
			.map(FluidStackInputIngredient::copyOf); // map FluidStacks

		ImmutableList<InputIngredient> ingredients = Stream.concat(itemIngredients, fluidIngredients)
			.collect(ImmutableList.toImmutableList());

		return Optional.ofNullable(cachedRecipes.get(ingredients));
	}

	/**
	 * Given the inputs find and apply the recipe to said inputs.
	 *
	 * @param items Recipe input (not modified)
	 * @param simulate If true the manager will accept partially missing ingredients or
	 * ingredients with insufficient quantities. This is primarily used to check whether a
	 * slot/tank/etc can accept the input while trying to supply a machine with resources
	 * @return Recipe result, or empty if none
	 */
	public Optional<Recipe> findAndApply(ImmutableList<ItemStack> items, ImmutableList<FluidStack> fluids, boolean simulate) {
		Stream<ItemStackInputIngredient> itemIngredients = items.stream()
			.filter(stack -> !ItemUtils.isEmpty(stack))
			.map(ItemStackInputIngredient::of); // map ItemStacks

		Stream<FluidStackInputIngredient> fluidIngredients = fluids.stream()
			.filter(stack -> stack.amount <= 0)
			.map(FluidStackInputIngredient::of); // map FluidStacks

		ImmutableList<InputIngredient> ingredients = Stream.concat(itemIngredients, fluidIngredients)
			.collect(ImmutableList.toImmutableList());

		if (ingredients.isEmpty()) return Optional.empty(); // if the inputs are empty we can return nothing

		Optional<Recipe> ret = Optional.ofNullable(cachedRecipes.get(ingredients));

		ret.map(recipe -> {
			// check if everything need for the input is available in the input (ingredients + quantities)
			if (ingredients.size() != recipe.getInputIngredients().size()) return Optional.empty();

			final List<InputIngredient> listA = new ArrayList<>(recipe.getInputIngredients());
			ingredients.forEach(entry ->
				listA.removeIf(temp -> temp.matches(entry.ingredient) && entry.getCount() >= temp.getCount()));

			if (!listA.isEmpty()) return Optional.empty(); // the input did not match

			if (!simulate) {
				final List<InputIngredient> listB = new ArrayList<>(recipe.getInputIngredients());
				ingredients.forEach(entry ->
					listB.removeIf(temp -> {
						if (temp.matches(entry.ingredient)) {
							entry.shrink(temp.getCount()); // adjust the quantity
							return true;
						}
						return false;
					})
				);
			}

			return Optional.of(recipe);
		});

		return ret;
	}

	public boolean apply(Recipe recipe, ImmutableList<ItemStack> items, ImmutableList<FluidStack> fluids, boolean simulate) {
		Stream<ItemStackInputIngredient> itemIngredients = items.stream()
			.filter(stack -> !ItemUtils.isEmpty(stack))
			.map(ItemStackInputIngredient::of); // map ItemStacks

		Stream<FluidStackInputIngredient> fluidIngredients = fluids.stream()
			.filter(stack -> stack.amount <= 0)
			.map(FluidStackInputIngredient::of); // map FluidStacks

		ImmutableList<InputIngredient> ingredients = Stream.concat(itemIngredients, fluidIngredients)
			.collect(ImmutableList.toImmutableList());

		// check if everything need for the input is available in the input (ingredients + quantities)
		if (ingredients.size() != recipe.getInputIngredients().size()) return false;

		final List<InputIngredient> listA = new ArrayList<>(recipe.getInputIngredients());
		ingredients.forEach(entry ->
			listA.removeIf(temp -> temp.matches(entry.ingredient) && entry.getCount() >= temp.getCount()));

		if (!listA.isEmpty()) return false; // the input did not match

		if (!simulate) {
			final List<InputIngredient> listB = new ArrayList<>(recipe.getInputIngredients());
			ingredients.forEach(entry ->
				listB.removeIf(temp -> {
					if (temp.matches(entry.ingredient)) {
						entry.shrink(temp.getCount()); // adjust the quantity
						return true;
					}
					return false;
				})
			);
		}

		return true;
	}

	/**
	 * Removes a recipe from this handler.
	 *
	 * @param ingredients The input ingredients
	 * @return True if a valid recipe has been found and removed or false otherwise
	 */
	public boolean removeRecipe(ImmutableList<InputIngredient> ingredients) {
		Recipe recipe = getRecipe(ingredients).orElse(null);
		if (recipe == null) return false;

		cachedRecipes.invalidate(ingredients); // remove from cache
		return recipes.remove(recipe);
	}

	public List<Recipe> getRecipes() {
		return recipes;
	}

	// Fields >>
	protected final List<Recipe> recipes = new ArrayList<>();

	protected final LoadingCache<ImmutableList<InputIngredient>, Recipe> cachedRecipes =
		Caffeine.newBuilder()
			.expireAfterAccess(10, TimeUnit.MINUTES)
			.maximumSize(100)
			.build(ingredients ->
				recipes.stream()
					.filter(recipe -> {
						final List<InputIngredient> listA = new ArrayList<>(recipe.getInputIngredients());
						ingredients.forEach(entry ->
							listA.removeIf(temp -> temp.matches(entry.ingredient) && entry.getCount() >= temp.getCount()));

						return listA.isEmpty();
					})
					.findAny()
					.orElse(null)
			);
	// << Fields
}