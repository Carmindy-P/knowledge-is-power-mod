package net.carmindy.kipmod.items;

import net.carmindy.kipmod.abilities.Abilities;
import net.carmindy.kipmod.abilities.AbilityRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;

/**
 * Factory class to create and read ability books.
 */
public class AbilityBookFactory {
    public static final String ABILITY_KEY = "Ability";

    /** Creates an enchanted book ItemStack with the given ability */
    public static ItemStack createAbilityBook(Abilities ability) {
        ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);

        Object nbt = ItemStackNbtCompat.getOrCreateNbt(book);
        if (nbt != null) {
            ItemStackNbtCompat.putString((NbtCompound) nbt, ABILITY_KEY, ability.getId());
        }
        return book;
    }

    /** Reads the ability from an enchanted book */
    public static Abilities getAbilityFromBook(ItemStack stack) {
        if (stack == null || stack.isEmpty() || !ItemStackNbtCompat.hasNbt(stack)) return null;

        Object nbt = ItemStackNbtCompat.getNbt(stack);
        if (nbt == null) return null;

        if (ItemStackNbtCompat.contains((NbtCompound) nbt, ABILITY_KEY)) {
            String id = ItemStackNbtCompat.getString((NbtCompound) nbt, ABILITY_KEY);
            if (id != null && !id.isEmpty()) {
                return AbilityRegistry.get(id);
            }
        }
        return null;
    }
}
