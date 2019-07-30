package estebes.praescriptum.ingredients.input;

import estebes.praescriptum.Utils.FluidUtils;
import net.minecraftforge.fluids.FluidStack;

import java.util.Objects;

public class FluidStackInputIngredient extends InputIngredient<FluidStack> {
	public static FluidStackInputIngredient of(FluidStack ingredient) {
		return new FluidStackInputIngredient(ingredient);
	}

	public static FluidStackInputIngredient copyOf(FluidStack ingredient) {
		return new FluidStackInputIngredient(ingredient.copy());
	}

	protected FluidStackInputIngredient(FluidStack ingredient) {
		super(ingredient);
	}

	@Override
	public Object getUnspecific() {
		return ingredient.getFluid();
	}

	@Override
	public InputIngredient<FluidStack> copy() {
		return of(ingredient.copy());
	}

	@Override
	public boolean isEmpty() {
		return ingredient.amount <= 0;
	}

	@Override
	public int getCount() {
		return ingredient.amount;
	}

	@Override
	public void shrink(int amount) {
		ingredient.amount -= amount;
	}

	@Override
	public boolean matches(Object other) {
		return other instanceof FluidStack &&
			ingredient.isFluidEqual((FluidStack) other);
	}

	@Override
	public boolean matchesStrict(Object other) {
		return other instanceof FluidStack &&
			ingredient.isFluidStackIdentical((FluidStack) other);
	}

	@Override
	public String toFormattedString() {
		return FluidUtils.toFormattedString(ingredient);
	}

	@Override
	public int hashCode() {
		return Objects.hash(ingredient, consumable);
	}

	@Override
	public boolean equals(Object object) {
		if (getClass() != object.getClass()) return false;

		return matches(((FluidStackInputIngredient) object).ingredient) && this.consumable == ((FluidStackInputIngredient) object).consumable;
	}
}
