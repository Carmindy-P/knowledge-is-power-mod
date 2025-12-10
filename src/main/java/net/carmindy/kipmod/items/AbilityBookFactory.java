package net.carmindy.kipmod.items;

import net.carmindy.kipmod.abilities.Abilities;
import net.carmindy.kipmod.abilities.AbilityRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

public class AbilityBookFactory {

    // Common candidate names for methods/classes across mappings
    private static final String[] GET_OR_CREATE_NAMES = {
            "getOrCreateNbt", "getOrCreateTag", "getOrCreateCompoundTag"
    };
    private static final String[] GET_TAG_NAMES = {
            "getNbt", "getTag", "getCompoundTag"
    };
    private static final String[] SET_TAG_NAMES = {
            "setNbt", "setTag", "setCompoundTag"
    };
    private static final String[] NBT_CLASS_NAMES = {
            "net.minecraft.nbt.NbtCompound",
            "net.minecraft.nbt.CompoundTag",
            "net.minecraft.nbt.CompoundNBT"
    };

    public static ItemStack createAbilityBook(Abilities ability) {
        ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);

        // Try to call an existing getOrCreate* method
        Object nbt = invokeItemStackMethodReturningObject(book, GET_OR_CREATE_NAMES);

        // If no getOrCreate method exists, try to get existing tag and create+set one if null
        if (nbt == null) {
            nbt = invokeItemStackMethodReturningObject(book, GET_TAG_NAMES);
            if (nbt == null) {
                Object newNbt = createNbtInstance();
                if (newNbt != null) {
                    boolean setOk = invokeItemStackMethodWithArg(book, SET_TAG_NAMES, newNbt);
                    if (setOk) nbt = newNbt;
                }
            }
        }

        // If we have an NBT object, call putString (or alternative names) via reflection
        if (nbt != null) {
            invokeNbtPutString(nbt, "Ability", ability.getId());
        }

        return book;
    }

    public static Abilities getAbilityFromBook(ItemStack stack) {
        Object nbt = invokeItemStackMethodReturningObject(stack, GET_TAG_NAMES);
        if (nbt == null) {
            // Try getOrCreate but it should create — we don't want to create here; just return null
            nbt = invokeItemStackMethodReturningObject(stack, GET_OR_CREATE_NAMES);
            // If getOrCreate returned something, proceed, otherwise nbt stays null
        }
        if (nbt != null) {
            Boolean has = invokeNbtContains(nbt, "Ability");
            if (Boolean.TRUE.equals(has)) {
                String id = invokeNbtGetString(nbt, "Ability");
                if (id != null) {
                    return AbilityRegistry.get(id);
                }
            }
        }
        return null;
    }

    // -------------------------
    // Reflection helpers
    // -------------------------
    private static Object invokeItemStackMethodReturningObject(ItemStack stack, String[] candidates) {
        for (String name : candidates) {
            try {
                Method m = ItemStack.class.getMethod(name);
                m.setAccessible(true);
                return m.invoke(stack);
            } catch (NoSuchMethodException ignored) {
            } catch (IllegalAccessException | InvocationTargetException e) {
                // If the method exists but invocation failed, bubble up as runtime problem
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    private static boolean invokeItemStackMethodWithArg(ItemStack stack, String[] candidates, Object arg) {
        for (String name : candidates) {
            try {
                Method[] methods = ItemStack.class.getMethods();
                for (Method m : methods) {
                    if (m.getName().equals(name) && m.getParameterCount() == 1) {
                        m.setAccessible(true);
                        m.invoke(stack, arg);
                        return true;
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    private static Object createNbtInstance() {
        for (String clsName : NBT_CLASS_NAMES) {
            try {
                Class<?> cls = Class.forName(clsName);
                // Try no-arg constructor
                Constructor<?> ctor = cls.getDeclaredConstructor();
                ctor.setAccessible(true);
                return ctor.newInstance();
            } catch (ClassNotFoundException ignored) {
            } catch (NoSuchMethodException ignored) {
                // Some mappings may not expose a public no-arg ctor; ignore
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    private static void invokeNbtPutString(Object nbt, String key, String value) {
        // common put method names: putString, put
        String[] names = {"putString", "put"};
        for (String name : names) {
            try {
                Method m = findMethodByNameAndParams(nbt.getClass(), name, String.class, String.class);
                if (m != null) {
                    m.setAccessible(true);
                    m.invoke(nbt, key, value);
                    return;
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        // if none found, ignore gracefully
    }

    private static Boolean invokeNbtContains(Object nbt, String key) {
        String[] names = {"contains", "containsKey", "hasKey"};
        for (String name : names) {
            try {
                Method m = findMethodByNameAndParams(nbt.getClass(), name, String.class);
                if (m != null) {
                    m.setAccessible(true);
                    Object result = m.invoke(nbt, key);
                    if (result instanceof Boolean) return (Boolean) result;
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    private static String invokeNbtGetString(Object nbt, String key) {
        String[] names = {"getString", "get"};
        for (String name : names) {
            try {
                Method m = findMethodByNameAndParams(nbt.getClass(), name, String.class);
                if (m != null) {
                    m.setAccessible(true);
                    Object result = m.invoke(nbt, key);
                    if (result != null) return String.valueOf(result);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    private static Method findMethodByNameAndParams(Class<?> cls, String name, Class<?>... params) {
        try {
            return cls.getMethod(name, params);
        } catch (NoSuchMethodException ignored) {
            // fallback: try declared methods
            for (Method m : cls.getDeclaredMethods()) {
                if (m.getName().equals(name) && m.getParameterCount() == params.length) {
                    // not a perfect match on param types but may be acceptable in many mappings
                    return m;
                }
            }
        }
        return null;
    }
}