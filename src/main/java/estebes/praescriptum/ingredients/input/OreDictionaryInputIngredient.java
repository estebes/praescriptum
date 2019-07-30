package estebes.praescriptum.ingredients.input;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class OreDictionaryInputIngredient extends InputIngredient<String> {
	public static OreDictionaryInputIngredient of(String ingredient) {
		return of(ingredient, 1);
	}

	public static OreDictionaryInputIngredient of(String ingredient, int amount) {
		return of(ingredient, amount, null);
	}

	public static OreDictionaryInputIngredient of(String ingredient, int amount, Integer meta) {
		return new OreDictionaryInputIngredient(ingredient, amount, meta);
	}

	protected OreDictionaryInputIngredient(String ingredient) {
		this(ingredient, 1);
	}

	protected OreDictionaryInputIngredient(String ingredient, int amount) {
		this(ingredient, amount, null);
	}

	protected OreDictionaryInputIngredient(String ingredient, int amount, Integer meta) {
		super(ingredient);

		this.amount = amount;
		this.meta = meta;
	}

	@Override
	public Object getUnspecific() {
		return null;
	}

	@Override
	public InputIngredient<String> copy() {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public boolean isEmpty() {
		return amount <= 0;
	}

	@Override
	public int getCount() {
		return amount;
	}

	@Override
	public void shrink(int amount) {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public boolean matches(Object other) {
		if (!(other instanceof ItemStack)) return false;

		List<ItemStack> inputs = getEquivalents();
		boolean useOreStackMeta = meta == null;
		Item subjectItem = ((ItemStack) other).getItem();
		int subjectMeta = ((ItemStack) other).getItemDamage();

		return inputs.stream()
			.anyMatch(entry -> {
				Item oreItem = entry.getItem();

				int metaRequired = useOreStackMeta ? entry.getItemDamage() : meta;

				return subjectItem == oreItem && (subjectMeta == metaRequired ||
					metaRequired == OreDictionary.WILDCARD_VALUE);
			});
	}

	@Override
	public boolean matchesStrict(Object other) {
		return other instanceof String && ingredient.equals(other);
	}

	@Override
	public String toFormattedString() {
		return ingredient;
	}

	private List<ItemStack> getEquivalents() {
		if (equivalents != null) return equivalents;

		// cache the ore list by making use of the fact that forge always uses the same list,
		// unless it's EMPTY_LIST, which should never happen.
		List<ItemStack> ret = OreDictionary.getOres((String) ingredient);

		if (ret != OreDictionary.EMPTY_LIST) equivalents = ret;

		return ret;
	}

	// Fields >>
	public final int amount;
	public final Integer meta;

	private List<ItemStack> equivalents;
	// << Fields
}
