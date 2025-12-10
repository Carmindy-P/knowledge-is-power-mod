package net.carmindy.kipmod.data;

import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

// TEMPORARY fallback to test handler; does not use NBT
public class AbilityBookComponent {
    public static void setAbility(ItemStack stack, String abilityId) {
        // no-op temporary
    }

    public static boolean hasAbility(ItemStack stack) {
        // Always return true for testing so BookUseHandler proceeds
        return true;
    }

    @Nullable
    public static String getAbility(ItemStack stack) {
        // return a known ability id you have registered (e.g. "flame")
        return "flame";
    }

    public static void removeAbility(ItemStack stack) {
        // no-op
    }
}