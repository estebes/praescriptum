package estebes.praescriptum.ingredients.output;

import estebes.praescriptum.ingredients.Ingredient;

public abstract class OutputIngredient<T> extends Ingredient<T> {
	protected OutputIngredient(T ingredient) {
		super(ingredient);
	}

	public abstract OutputIngredient<T> copy();
}

