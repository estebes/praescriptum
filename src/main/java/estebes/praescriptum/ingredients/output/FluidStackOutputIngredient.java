package estebes.praescriptum.ingredients.output;

import estebes.praescriptum.Utils.FluidUtils;
import net.minecraftforge.fluids.FluidStack;

public class FluidStackOutputIngredient extends OutputIngredient<FluidStack> {
	public static FluidStackOutputIngredient of(FluidStack ingredient) {
		return new FluidStackOutputIngredient(ingredient);
	}

	protected FluidStackOutputIngredient(FluidStack ingredient) {
		super(ingredient);
	}

	@Override
	public OutputIngredient<FluidStack> copy() {
		return of(ingredient.copy());
	}

	@Override
	public boolean isEmpty() {
		return ingredient.amount <= 0;
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
}