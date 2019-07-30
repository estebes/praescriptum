package estebes.praescriptum.ingredients.input;

import estebes.praescriptum.ingredients.Ingredient;

public abstract class InputIngredient<T> extends Ingredient<T> {
	protected InputIngredient(T ingredient) {
		this(ingredient, true);
	}

	protected InputIngredient(T ingredient, boolean consumable) {
		super(ingredient);

		this.consumable = consumable;
	}

	public abstract Object getUnspecific();

	public abstract InputIngredient<T> copy();

	public abstract int getCount();

	public abstract void shrink(int amount);

	public boolean isConsumable() {
		return consumable;
	}

	@Override
	public int hashCode() {
//		return Objects.hash(ingredient, consumable);
		return 1;
	}

	@Override
	public boolean equals(Object object) {
		if (getClass() != object.getClass()) return false;

		return matches(((InputIngredient) object).ingredient) && this.consumable == ((InputIngredient) object).consumable;
	}

	// Fields >>
	public final boolean consumable;
	// << Fields
}
