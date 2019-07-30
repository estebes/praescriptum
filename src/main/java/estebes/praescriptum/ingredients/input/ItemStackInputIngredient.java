package estebes.praescriptum.ingredients.input;

import net.minecraft.item.ItemStack;

import estebes.praescriptum.Utils.ItemUtils;

public class ItemStackInputIngredient extends InputIngredient<ItemStack> {
	public static ItemStackInputIngredient of(ItemStack ingredient) {
		return new ItemStackInputIngredient(ingredient);
	}

	public static ItemStackInputIngredient of(ItemStack ingredient, boolean consumable) {
		return new ItemStackInputIngredient(ingredient, consumable);
	}

	public static ItemStackInputIngredient copyOf(ItemStack ingredient) {
		return new ItemStackInputIngredient(ingredient.copy());
	}

	public static ItemStackInputIngredient copyOf(ItemStack ingredient, boolean consumable) {
		return new ItemStackInputIngredient(ingredient.copy(), consumable);
	}

	protected ItemStackInputIngredient(ItemStack ingredient) {
		super(ingredient);
	}

	protected ItemStackInputIngredient(ItemStack ingredient, boolean consumable) {
		super(ingredient, consumable);
	}

	@Override
	public Object getUnspecific() {
		return ingredient.getItem();
	}

	@Override
	public InputIngredient<ItemStack> copy() {
		return of(ingredient.copy());
	}

	@Override
	public boolean isEmpty() {
		return ItemUtils.isEmpty(ingredient);
	}

	@Override
	public int getCount() {
		return ItemUtils.getSize(ingredient);
	}

	@Override
	public void shrink(int amount) {
		ingredient.shrink(amount);
	}

	@Override
	public boolean matches(Object other) {
		return other instanceof ItemStack &&
			ItemUtils.isItemEqual(ingredient, (ItemStack) other, true, true);
	}

	@Override
	public boolean matchesStrict(Object other) {
		return matches(other);
	}

	@Override
	public String toFormattedString() {
		return ItemUtils.toFormattedString(ingredient);
	}

//	@Override
//	public int hashCode() {
//		return Objects.hash(ingredient.getItem(), ingredient.getMetadata(), ingredient.getTagCompound(), consumable);
//	}

	@Override
	public boolean equals(Object object) {
		if (getClass() != object.getClass()) return false;

		return matches(((ItemStackInputIngredient) object).ingredient) && this.consumable == ((ItemStackInputIngredient) object).consumable;
	}
}