package estebes.praescriptum.Utils;

import java.util.function.Consumer;
import java.util.function.Predicate;

import estebes.praescriptum.ingredients.Ingredient;
import estebes.praescriptum.recipes.RecipeHandler;

public class IngredientUtils {
	/**
	 * Evaluate if an ingredient is empty.
	 *
	 * @param <T> Type of the ingredient to be checked
	 * @return A simple isEmpty() predicate
	 */
	public static <T extends Ingredient> Predicate<T> isIngredientEmpty() {
		return Ingredient::isEmpty;
	}

	/**
	 * Evaluate if an ingredient is empty while also executing an arbitrary action.
	 * This is useful if you want to perform some logging when an item is null.
	 * Check {@link RecipeHandler#addRecipe(Recipe, boolean)} for an example.
	 *
	 * @param action The action to be executed
	 * @param <T> Type of the ingredient to be checked
	 * @return A simple isEmpty() predicate that also executes the provided action
	 */
	public static <T extends Ingredient> Predicate<T> isIngredientEmpty(Consumer<T> action) {
		return ingredient -> {
			boolean isEmpty = ingredient.isEmpty();
			if (isEmpty)
				action.accept(ingredient);
			return !isEmpty;
		};
	}

	public static <T> Predicate<T> isPartOfRecipe(RecipeHandler handler) {
		return object -> handler.getRecipes().stream()
			.anyMatch(recipe -> recipe.getInputIngredients()
				.stream()
				.anyMatch(ingredient -> ingredient.matches(object)));
	}
}
