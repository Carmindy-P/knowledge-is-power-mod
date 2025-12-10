package net.carmindy.kipmod.items;

import net.carmindy.kipmod.abilities.Abilities;
import net.carmindy.kipmod.abilities.AbilityRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

/**
 * Uses ItemStackNbtCompat so this compiles regardless of the method names in mappings.
 */
public class AbilityBookFactory {
    private static final String ABILITY_KEY = "Ability";

    public static ItemStack createAbilityBook(Abilities ability) {
        ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);

        Object nbt = ItemStackNbtCompat.getOrCreateNbt(book);
        if (nbt != null) {
            ItemStackNbtCompat.putString(nbt, ABILITY_KEY, ability.getId());
        }
        return book;
    }

    public static Abilities getAbilityFromBook(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return null;
        if (!ItemStackNbtCompat.hasNbt(stack)) return null;

        Object nbt = ItemStackNbtCompat.getNbt(stack);
        if (nbt == null) return null;

        if (ItemStackNbtCompat.contains(nbt, ABILITY_KEY)) {
            String id = ItemStackNbtCompat.getString(nbt, ABILITY_KEY);
            if (id != null && !id.isEmpty()) {
                return AbilityRegistry.get(id);
            }
        }
        return null;
    }
}