package net.carmindy.kipmod.items;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.item.ItemStack;

/**
 * Compatibility helper that finds ItemStack / NBT methods via reflection at runtime.
 * Call its methods from your code instead of calling mapping-specific getters directly.
 */
public final class ItemStackNbtCompat {
    private static final String[] GET_OR_CREATE_NAMES = {"getOrCreateNbt", "getOrCreateTag", "getOrCreateCompoundTag"};
    private static final String[] GET_NAMES = {"getNbt", "getTag", "getCompoundTag"};
    private static final String[] HAS_NAMES = {"hasNbt", "hasTag", "hasCompoundTag", "hasNbt"};
    private static final String[] SET_NAMES = {"setNbt", "setTag", "setCompoundTag"};
    private static final String[] NBT_CLASS_NAMES = {
            "net.minecraft.nbt.NbtCompound",
            "net.minecraft.nbt.CompoundTag",
            "net.minecraft.nbt.CompoundNBT"
    };

    private static Method itemstack_getOrCreate = null;
    private static Method itemstack_get = null;
    private static Method itemstack_has = null;
    private static Method itemstack_set = null;

    private static Class<?> nbtClass = null;
    private static Method nbt_putString = null;
    private static Method nbt_getString = null;
    private static Method nbt_contains = null;

    static {
        // locate ItemStack methods
        for (String name : GET_OR_CREATE_NAMES) {
            try {
                itemstack_getOrCreate = ItemStack.class.getMethod(name);
                itemstack_getOrCreate.setAccessible(true);
                break;
            } catch (NoSuchMethodException ignored) {}
        }
        for (String name : GET_NAMES) {
            try {
                itemstack_get = ItemStack.class.getMethod(name);
                itemstack_get.setAccessible(true);
                break;
            } catch (NoSuchMethodException ignored) {}
        }
        for (String name : HAS_NAMES) {
            try {
                itemstack_has = ItemStack.class.getMethod(name);
                itemstack_has.setAccessible(true);
                break;
            } catch (NoSuchMethodException ignored) {}
        }
        for (String name : SET_NAMES) {
            try {
                // find any method with name and single param
                for (Method m : ItemStack.class.getMethods()) {
                    if (m.getName().equals(name) && m.getParameterCount() == 1) {
                        itemstack_set = m;
                        itemstack_set.setAccessible(true);
                        break;
                    }
                }
                if (itemstack_set != null) break;
            } catch (SecurityException ignored) {}
        }

        // locate NBT class and methods
        for (String clsName : NBT_CLASS_NAMES) {
            try {
                nbtClass = Class.forName(clsName);
                break;
            } catch (ClassNotFoundException ignored) {}
        }
        if (nbtClass != null) {
            // putString(String, String)
            for (String name : new String[]{"putString", "put", "setString"}) {
                try {
                    nbt_putString = nbtClass.getMethod(name, String.class, String.class);
                    nbt_putString.setAccessible(true);
                    break;
                } catch (NoSuchMethodException ignored) {}
            }
            // getString(String)
            for (String name : new String[]{"getString", "get", "getAsString"}) {
                try {
                    nbt_getString = nbtClass.getMethod(name, String.class);
                    nbt_getString.setAccessible(true);
                    break;
                } catch (NoSuchMethodException ignored) {}
            }
            // contains(String)
            for (String name : new String[]{"contains", "containsKey", "has"}) {
                try {
                    nbt_contains = nbtClass.getMethod(name, String.class);
                    nbt_contains.setAccessible(true);
                    break;
                } catch (NoSuchMethodException ignored) {}
            }
        }
    }

    private ItemStackNbtCompat() {}

    public static Object getOrCreateNbt(ItemStack stack) {
        try {
            if (itemstack_getOrCreate != null) {
                return itemstack_getOrCreate.invoke(stack);
            }
            // fallback: try get and if null create new instance and set it
            if (itemstack_get != null) {
                Object existing = itemstack_get.invoke(stack);
                if (existing != null) return existing;
                Object newNbt = createNbtInstance();
                if (newNbt != null && itemstack_set != null) {
                    itemstack_set.invoke(stack, newNbt);
                    return newNbt;
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static Object getNbt(ItemStack stack) {
        try {
            if (itemstack_get != null) return itemstack_get.invoke(stack);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static boolean hasNbt(ItemStack stack) {
        try {
            if (itemstack_has != null) {
                Object res = itemstack_has.invoke(stack);
                if (res instanceof Boolean) return (Boolean) res;
            } else {
                // fallback: check getNbt() != null
                return getNbt(stack) != null;
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public static void putString(Object nbt, String key, String value) {
        if (nbt == null || nbt_putString == null) return;
        try {
            nbt_putString.invoke(nbt, key, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getString(Object nbt, String key) {
        if (nbt == null || nbt_getString == null) return null;
        try {
            Object res = nbt_getString.invoke(nbt, key);
            return res != null ? res.toString() : null;
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean contains(Object nbt, String key) {
        if (nbt == null || nbt_contains == null) return false;
        try {
            Object res = nbt_contains.invoke(nbt, key);
            if (res instanceof Boolean) return (Boolean) res;
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    private static Object createNbtInstance() {
        if (nbtClass == null) return null;
        try {
            Constructor<?> ctor = nbtClass.getDeclaredConstructor();
            ctor.setAccessible(true);
            return ctor.newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}