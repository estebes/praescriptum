package estebes.praescriptum.Utils;

import net.minecraft.item.ItemStack;

import net.minecraftforge.oredict.OreDictionary;

public class ItemUtils {
    /**
     * Check if two stacks are equal.
     *
     * @param stackA The first stack
     * @param stackB The second stack
     * @param matchDamage Evaluate damage
     * @param matchNBT Evaluate NBT
     * @return True if the stacks are equal or false otherwise
     */
    public static boolean isItemEqual(final ItemStack stackA, final ItemStack stackB, final boolean matchDamage, final boolean matchNBT) {
        if (stackA.isEmpty() || stackB.isEmpty() || stackA.getItem() != stackB.getItem()) return false;

        if (matchNBT && !ItemStack.areItemStackTagsEqual(stackA, stackB)) return false;

        return !matchDamage || !stackA.getHasSubtypes() || isWildcard(stackA) || isWildcard(stackB) ||
                stackA.getItemDamage() == stackB.getItemDamage();
    }

    /**
     * Check if a stack uses a wildcard value regarding damage.
     *
     * @param stack The provided stack
     * @return True if the stack uses a wildcard damage or false otherwise
     */
    public static boolean isWildcard(ItemStack stack) {
        int damage = stack.getItemDamage();
        return damage == -1 || damage == OreDictionary.WILDCARD_VALUE;
    }

    /**
     * Check if a stack is empty
     *
     * @param stack the itemstack
     * @return true if the stack is empty or false otherwise
     */
    public static boolean isEmpty(ItemStack stack) {
        return stack == ItemStack.EMPTY || stack == null || stack.getCount() <= 0;
    }

    /**
     * Get the size of a stack
     *
     * @param stack the itemstack
     * @return the size of the stack
     */
    public static int getSize(ItemStack stack) {
        return isEmpty(stack) ? 0 : stack.getCount();
    }

    /**
     * Set the size of a stack
     *
     * @param stack the itemstack
     * @param size the new size
     * @return the resulting stack
     */
    public static ItemStack setSize(ItemStack stack, int size) {
        if (size <= 0) return ItemStack.EMPTY;

        stack.setCount(size);

        return stack;
    }

    /**
     * Increase the size of a stack
     *
     * @param stack the itemstack
     * @param amount amount to be increased by
     * @return the resulting stack
     */
    public static ItemStack increaseSize(ItemStack stack, int amount) {
        return setSize(stack, getSize(stack) + amount);
    }

    /**
     * Increase the size of a stack
     *
     * @param stack the itemstack
     * @return the resulting stack
     */
    public static ItemStack increaseSize(ItemStack stack) {
        return increaseSize(stack, 1);
    }

    /**
     * Decrease the size of a stack
     *
     * @param stack the itemstack
     * @param amount amount to be decreased by
     * @return the resulting stack
     */
    public static ItemStack decreaseSize(ItemStack stack, int amount) {
        return setSize(stack, getSize(stack) - amount);
    }

    /**
     * Decrease the size of a stack
     *
     * @param stack the itemstack
     * @return the resulting stack
     */
    public static ItemStack decreaseSize(ItemStack stack) {
        return decreaseSize(stack, 1);
    }

    /**
     * Simple util to decently represent an ItemStack with a string
     *
     * @param itemStack the ItemStack to be represent
     * @return a formatted string representing the ItemStack
     */
    public static String toFormattedString(ItemStack itemStack) {
        return (itemStack == null) ? "(null)" : (itemStack.getItem() == null) ?
                getSize(itemStack)+"x(null)@(unknown)" : itemStack.toString();
    }
}
