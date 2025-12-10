package net.carmindy.kipmod.data;

import net.carmindy.kipmod.abilities.AbilityRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

/**
 * NBT helper that avoids direct calls to mapping-specific ItemStack#getNbt/getTag/getOrCreateNbt
 * which can cause IntelliJ/IDE mapping issues. This implementation uses reflection to call
 * whichever method name is available at runtime.
 *
 * If reflection cannot find any suitable NBT accessor, the code fails safe (treats as no NBT).
 */
public class AbilityBookComponent {
    private static final String NBT_KEY = "Ability";
    private static final String STORED_ENCHANTMENTS_KEY = "StoredEnchantments";

    // Try common method names in this order
    private static final String[] GET_NBT_NAMES = {"getNbt", "getTag", "getOrCreateNbt", "getOrCreateTag"};
    private static final String[] SET_NBT_NAMES = {"setNbt", "setTag"};
    private static final String[] HAS_NBT_NAMES = {"hasNbt", "hasTag"};

    public static void setAbility(ItemStack stack, String abilityId) {
        NbtCompound tag = readNbt(stack);
        if (tag == null) {
            tag = new NbtCompound();
        }
        tag.putString(NBT_KEY, abilityId);
        writeNbt(stack, tag);
    }

    public static boolean hasAbility(ItemStack stack) {
        NbtCompound tag = readNbt(stack);
        if (tag != null && tag.contains(NBT_KEY)) return true;
        // fall back to stored enchantments on enchanted books
        return getAbilityFromEnchants(stack) != null;
    }

    @Nullable
    public static String getAbility(ItemStack stack) {
        NbtCompound tag = readNbt(stack);
        if (tag != null && tag.contains(NBT_KEY)) {
            return tag.getString(NBT_KEY);
        }
        return getAbilityFromEnchants(stack);
    }

    public static void removeAbility(ItemStack stack) {
        NbtCompound tag = readNbt(stack);
        if (tag == null) return;
        tag.remove(NBT_KEY);
        if (tag.isEmpty()) {
            // set to null by writing null via setNbt if available
            writeNbt(stack, null);
        } else {
            writeNbt(stack, tag);
        }
    }

    @Nullable
    private static String getAbilityFromEnchants(ItemStack stack) {
        NbtCompound tag = readNbt(stack);
        if (tag == null) return null;
        if (!tag.contains(STORED_ENCHANTMENTS_KEY)) return null;

        NbtList enchants = tag.getList(STORED_ENCHANTMENTS_KEY, 10); // 10 = compound
        for (int i = 0; i < enchants.size(); i++) {
            NbtCompound ench = enchants.getCompound(i);
            if (!ench.contains("id")) continue;
            String enchantId = ench.getString("id"); // e.g. "minecraft:flame" or "flame"

            // Try full id first, then strip namespace if present ("minecraft:flame" -> "flame")
            if (AbilityRegistry.get(enchantId) != null) return enchantId;

            int colon = enchantId.indexOf(':');
            if (colon != -1 && colon < enchantId.length() - 1) {
                String stripped = enchantId.substring(colon + 1);
                if (AbilityRegistry.get(stripped) != null) return stripped;
            }
        }

        return null;
    }

    // --------------------------
    // Reflection helpers
    // --------------------------

    @Nullable
    private static NbtCompound readNbt(ItemStack stack) {
        // Try hasNbt/hasTag first (reflection)
        Boolean has = invokeBooleanMethod(stack, HAS_NBT_NAMES);
        if (has != null && !has) return null;

        // Try to read the NBT using multiple possible method names
        Object result = invokeMethod(stack, GET_NBT_NAMES);
        if (result instanceof NbtCompound) {
            return (NbtCompound) result;
        }

        // If getOrCreate was attempted and returned something else, guard
        return null;
    }

    private static void writeNbt(ItemStack stack, @Nullable NbtCompound tag) {
        // Try setNbt/setTag
        invokeVoidMethodWithParam(stack, SET_NBT_NAMES, tag);
    }

    // reflection util: invoke a no-arg boolean-returning method with possible names
    @Nullable
    private static Boolean invokeBooleanMethod(ItemStack stack, String[] names) {
        for (String name : names) {
            try {
                Method m = ItemStack.class.getMethod(name);
                Object res = m.invoke(stack);
                if (res instanceof Boolean) return (Boolean) res;
            } catch (NoSuchMethodException ignored) {
                // try next
            } catch (Exception e) {
                // reflection error -> stop trying this name, continue trying others
            }
        }
        return null;
    }

    // reflection util: invoke a no-arg method returning Object
    @Nullable
    private static Object invokeMethod(ItemStack stack, String[] names) {
        for (String name : names) {
            try {
                Method m = ItemStack.class.getMethod(name);
                return m.invoke(stack);
            } catch (NoSuchMethodException ignored) {
                // try next
            } catch (Exception e) {
                // reflection error -> try next name
            }
        }
        return null;
    }

    // reflection util: invoke a single-arg void method (setNbt / setTag)
    private static void invokeVoidMethodWithParam(ItemStack stack, String[] names, @Nullable NbtCompound param) {
        for (String name : names) {
            try {
                Method m = ItemStack.class.getMethod(name, NbtCompound.class);
                m.invoke(stack, param);
                return;
            } catch (NoSuchMethodException ignored) {
                // try next
            } catch (Exception e) {
                // reflection error -> try next name
            }
        }
    }
}