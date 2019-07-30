package estebes.praescriptum.ingredients;

public abstract class Ingredient<T> {
	public Ingredient(T ingredient) {
		this.ingredient = ingredient;
	}

	public abstract boolean isEmpty();

	public abstract boolean matches(Object other);

	public abstract boolean matchesStrict(Object other);

	public abstract String toFormattedString();

	@Override
	public boolean equals(Object object) {
		if (getClass() != object.getClass()) return false;

		return matches(((Ingredient) object).ingredient);
	}

	@Override
	public String toString() {
		return String.format("Ingredient(%s)", toFormattedString());
	}

	// Fields >>
	public final T ingredient;
	// << Fields
}

