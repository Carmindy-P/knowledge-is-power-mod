package net.carmindy.kipmod.abilities;

import org.jetbrains.annotations.Nullable;
import java.util.HashMap;
import java.util.Map;

public final class AbilityRegistry {
    private static final Map<String, Abilities> REGISTRY = new HashMap<>();

    public static void register(String id, Abilities a) {
        REGISTRY.put(id, a);
    }

    public static @Nullable Abilities get(String id) {
        return REGISTRY.get(id);
    }
}