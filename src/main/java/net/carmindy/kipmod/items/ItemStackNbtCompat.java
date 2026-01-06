package net.carmindy.kipmod.items;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;   // Yarn name
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public final class ItemStackNbtCompat {

    private ItemStackNbtCompat() {}

    /* ---------- read / write the legacy "tag" component ---------- */

    public static NbtCompound getOrCreateNbt(ItemStack stack) {
        if (stack == null) throw new IllegalArgumentException("ItemStack cannot be null");
        NbtComponent comp = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT);
        return comp.copyNbt();               // already returns a **mutable** copy
    }

    public static NbtCompound getNbt(ItemStack stack) {
        if (stack == null) throw new IllegalArgumentException("ItemStack cannot be null");
        NbtComponent comp = stack.get(DataComponentTypes.CUSTOM_DATA);
        return comp == null ? null : comp.copyNbt();
    }

    public static boolean hasNbt(ItemStack stack) {
        if (stack == null) throw new IllegalArgumentException("ItemStack cannot be null");
        return stack.contains(DataComponentTypes.CUSTOM_DATA);
    }

    /* ---------- helpers ---------- */

    public static void putString(NbtCompound nbt, String key, String value) {
        if (nbt == null) throw new IllegalArgumentException("NbtCompound cannot be null");
        nbt.putString(key, value);
    }

    public static String getString(NbtCompound nbt, String key) {
        if (nbt == null) throw new IllegalArgumentException("NbtCompound cannot be null");
        return nbt.getString(key);
    }

    public static boolean contains(NbtCompound nbt, String key) {
        if (nbt == null) throw new IllegalArgumentException("NbtCompound cannot be null");
        return nbt.contains(key);
    }

    /* ---------- convenience: write back to stack ---------- */

    public static void setNbt(ItemStack stack, NbtCompound nbt) {
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));
    }
}