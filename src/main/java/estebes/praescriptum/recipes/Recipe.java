package estebes.praescriptum.recipes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

import estebes.praescriptum.Utils.ItemUtils;
import estebes.praescriptum.Utils.LogUtils;
import estebes.praescriptum.ingredients.input.FluidStackInputIngredient;
import estebes.praescriptum.ingredients.input.InputIngredient;
import estebes.praescriptum.ingredients.input.ItemStackInputIngredient;
import estebes.praescriptum.ingredients.input.OreDictionaryInputIngredient;
import estebes.praescriptum.ingredients.output.FluidStackOutputIngredient;
import estebes.praescriptum.ingredients.output.ItemStackOutputIngredient;
import estebes.praescriptum.ingredients.output.OutputIngredient;
import net.minecraftforge.fluids.FluidStack;

public class Recipe implements Serializable, Cloneable {
	public Recipe(RecipeHandler manager) {
		this.manager = manager;
	}

	public Recipe withInput(List<InputIngredient> inputs) {
		inputIngredients.addAll(inputs);
		return this;
	}

	public Recipe withInput(ItemStack itemStack) {
		if (ItemUtils.isEmpty(itemStack)) throw new IllegalArgumentException("Input cannot be empty");

		inputIngredients.add(ItemStackInputIngredient.of(itemStack));
		return this;
	}

	public Recipe withInput(String oreDict) {
		inputIngredients.add(OreDictionaryInputIngredient.of(oreDict));
		return this;
	}
	public Recipe withInput(String oreDict, int amount) {
		if (amount <= 0) throw new IllegalArgumentException("Input cannot be empty");

		inputIngredients.add(OreDictionaryInputIngredient.of(oreDict, amount));
		return this;
	}

	public Recipe withInput(String oreDict, int amount, Integer meta) {
		if (amount <= 0) throw new IllegalArgumentException("Input cannot be empty");

		inputIngredients.add(OreDictionaryInputIngredient.of(oreDict, amount, meta));
		return this;
	}

	public Recipe withInput(FluidStack fluidStack) {
		if (fluidStack.amount <= 0) throw new IllegalArgumentException("Input cannot be empty");

		inputIngredients.add(FluidStackInputIngredient.of(fluidStack));
		return this;
	}

	public Recipe withOutput(List<OutputIngredient> outputs) {
		outputIngredients.addAll(outputs);
		return this;
	}

	public Recipe withOutput(ItemStack itemStack) {
		if (ItemUtils.isEmpty(itemStack)) throw new IllegalArgumentException("Output cannot be empty");

		outputIngredients.add(ItemStackOutputIngredient.of(itemStack));
		return this;
	}

	public Recipe withOutput(FluidStack fluidStack) {
		if (fluidStack.amount <= 0) throw new IllegalArgumentException("Output cannot be empty");

		outputIngredients.add(FluidStackOutputIngredient.of(fluidStack));
		return this;
	}

	public Recipe withEnergyCostPerTick(int energyCostPerTick) {
		if (energyCostPerTick < 0) throw new IllegalArgumentException("Energy cost per tick cannot be less than 0");

		this.energyCostPerTick = energyCostPerTick;
		return this;
	}

	public Recipe withOperationDuration(int operationDuration) {
		if (operationDuration < 0) throw new IllegalArgumentException("Operation duration cannot be less than 0");

		this.operationDuration = operationDuration;
		return this;
	}

	public void register() {
		register(false);
	}

	public void register(boolean replace) {
		boolean success = false;

		success = manager.addRecipe(this, replace);

		if (!success) LogUtils.LOGGER.warn("Registration failed for input " + this);
	}

	// Cloneable >>
	@Override
	protected Recipe clone() {
		return new Recipe(manager)
			.withInput(inputIngredients)
			.withOutput(outputIngredients)
			.withEnergyCostPerTick(energyCostPerTick)
			.withOperationDuration(operationDuration);
	}
	// << Cloneable

	// Getters >>
	public List<InputIngredient> getInputIngredients() {
		return inputIngredients;
	}

	public List<OutputIngredient> getOutputIngredients() {
		return outputIngredients;
	}

	public int getEnergyCostPerTick() {
		return energyCostPerTick;
	}

	public int getOperationDuration() {
		return operationDuration;
	}
	// << Getters

	// Fields >>
	private final RecipeHandler manager;

	private List<InputIngredient> inputIngredients = new ArrayList<>();
	private List<OutputIngredient> outputIngredients = new ArrayList<>();
	private int energyCostPerTick;
	private int operationDuration;
	// << Fields
}