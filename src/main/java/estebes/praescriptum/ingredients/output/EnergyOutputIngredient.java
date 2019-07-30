package estebes.praescriptum.ingredients.output;

import org.apache.commons.lang3.ClassUtils;

public class EnergyOutputIngredient extends OutputIngredient<Double> {
	public static EnergyOutputIngredient of(Double ingredient) {
		return new EnergyOutputIngredient(ingredient);
	}

	public static EnergyOutputIngredient copyOf(Double ingredient) {
		return new EnergyOutputIngredient(ingredient);
	}

	protected EnergyOutputIngredient(Double ingredient) {
		super(ingredient);
	}

	@Override
	public OutputIngredient<Double> copy() {
		return of(ingredient);
	}

	@Override
	public boolean isEmpty() {
		return ingredient == null || ingredient == 0.0D;
	}

	@Override
	public boolean matches(Object other) {
		if (!(ClassUtils.isPrimitiveOrWrapper(Double.class))) return false;

		return ingredient.equals(other);
	}

	@Override
	public boolean matchesStrict(Object other) {
		return matches(other);
	}

	@Override
	public String toFormattedString() {
		return ingredient.toString();
	}
}