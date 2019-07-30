package estebes.praescriptum.ingredients.output;

import net.minecraft.item.ItemStack;

import estebes.praescriptum.Utils.ItemUtils;

public class ItemStackOutputIngredient extends OutputIngredient<ItemStack> {
	public static ItemStackOutputIngredient of(ItemStack ingredient) {
		return new ItemStackOutputIngredient(ingredient);
	}

	public static ItemStackOutputIngredient copyOf(ItemStack ingredient) {
		return new ItemStackOutputIngredient(ingredient.copy());
	}

	protected ItemStackOutputIngredient(ItemStack ingredient) {
		super(ingredient);
	}

	@Override
	public OutputIngredient<ItemStack> copy() {
		return of(ingredient.copy());
	}

	@Override
	public boolean isEmpty() {
		return ItemUtils.isEmpty(ingredient);
	}

	@Override
	public boolean matches(Object other) {
		if (!(other instanceof ItemStack)) return false;

		return ItemUtils.isItemEqual(ingredient, (ItemStack) other, true, true);
	}

	@Override
	public boolean matchesStrict(Object other) {
		return matches(other);
	}

	@Override
	public String toFormattedString() {
		return ItemUtils.toFormattedString(ingredient);
	}

	@Override
	public boolean equals(Object object) {
		if (getClass() != object.getClass()) return false;

		return matches(((ItemStackOutputIngredient) object).ingredient);
	}
}
