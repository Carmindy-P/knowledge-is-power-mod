package net.carmindy.kipmod.data;

import net.carmindy.kipmod.abilities.AbilityRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

/**
 * Handles storing abilities on ItemStacks (usually enchanted books).
 * Uses reflection to avoid mapping-specific method issues.
 */
public class AbilityBookComponent {

    private static final String NBT_KEY = "Ability";
    private static final String STORED_ENCHANTMENTS_KEY = "StoredEnchantments";

    private static final String[] GET_NBT_NAMES = {"getNbt", "getTag", "getOrCreateNbt", "getOrCreateTag"};
    private static final String[] SET_NBT_NAMES = {"setNbt", "setTag"};
    private static final String[] HAS_NBT_NAMES = {"hasNbt", "hasTag"};

    private Enchantment ability;

    public void setAbility(Enchantment ench) {
        this.ability = ench;
    }

    public Enchantment getAbility() {
        return this.ability;
    }


    /** Checks if the ItemStack has an ability */
    public static boolean hasAbility(ItemStack stack) {
        NbtCompound tag = readNbt(stack);
        if (tag != null && tag.contains(NBT_KEY)) return true;
        // fallback: check enchantments
        return getAbilityFromEnchants(stack) != null;
    }

    /** Gets the ability ID from the ItemStack */
    @Nullable
    public static String getAbility(ItemStack stack) {
        NbtCompound tag = readNbt(stack);
        if (tag != null && tag.contains(NBT_KEY)) return tag.getString(NBT_KEY);
        return getAbilityFromEnchants(stack);
    }

    /** Removes the ability from the ItemStack */
    public static void removeAbility(ItemStack stack) {
        NbtCompound tag = readNbt(stack);
        if (tag == null) return;
        tag.remove(NBT_KEY);
        writeNbt(stack, tag.isEmpty() ? null : tag);
    }

    @Nullable
    private static String getAbilityFromEnchants(ItemStack stack) {
        NbtCompound tag = readNbt(stack);
        if (tag == null) return null;

        // First, check StoredEnchantments (standard for EnchantedBookItem)
        String ability = checkEnchantList(tag.getList("StoredEnchantments", 10));
        if (ability != null) return ability;

        // Fallback: check Enchantments (used by /give or command-given books)
        if (tag.contains("Enchantments")) {
            ability = checkEnchantList(tag.getList("Enchantments", 10));
            if (ability != null) return ability;
        }

        return null;
    }

    /**
     * Helper: checks a NbtList of enchantments for a registered ability.
     */
    private static String checkEnchantList(NbtList enchants) {
        for (int i = 0; i < enchants.size(); i++) {
            NbtCompound ench = enchants.getCompound(i);

            if (!ench.contains("id")) continue;

            // Modern MC: enchantments store the ID as a STRING, not a SHORT
            String enchantId = ench.getString("id");
            if (enchantId == null || enchantId.isEmpty()) continue;

            // Try full ID first: "minecraft:flame"
            if (AbilityRegistry.get(enchantId) != null)
                return enchantId;

            // Try stripped ID: "flame"
            int colon = enchantId.indexOf(':');
            if (colon != -1) {
                String stripped = enchantId.substring(colon + 1);
                if (AbilityRegistry.get(stripped) != null)
                    return stripped;
            }
        }

        return null;
    }




    @Nullable
    private static NbtCompound readNbt(ItemStack stack) {
        Boolean has = invokeBooleanMethod(stack, HAS_NBT_NAMES);
        if (has != null && !has) return null;

        Object result = invokeMethod(stack, GET_NBT_NAMES);
        if (result instanceof NbtCompound) return (NbtCompound) result;
        return null;
    }

    private static void writeNbt(ItemStack stack, @Nullable NbtCompound tag) {
        invokeVoidMethodWithParam(stack, SET_NBT_NAMES, tag);
    }

    @Nullable
    private static Boolean invokeBooleanMethod(ItemStack stack, String[] names) {
        for (String name : names) {
            try {
                Method m = ItemStack.class.getMethod(name);
                Object res = m.invoke(stack);
                if (res instanceof Boolean) return (Boolean) res;
            } catch (Exception ignored) {}
        }
        return null;
    }

    @Nullable
    private static Object invokeMethod(ItemStack stack, String[] names) {
        for (String name : names) {
            try {
                Method m = ItemStack.class.getMethod(name);
                return m.invoke(stack);
            } catch (Exception ignored) {}
        }
        return null;
    }

    private static void invokeVoidMethodWithParam(ItemStack stack, String[] names, @Nullable NbtCompound param) {
        for (String name : names) {
            try {
                Method m = ItemStack.class.getMethod(name, NbtCompound.class);
                m.invoke(stack, param);
                return;
            } catch (Exception ignored) {}
        }
    }
}
