package net.carmindy.kipmod.abilities;

import net.carmindy.kipmod.abilities.Abilities;
import net.minecraft.resource.ResourceManager;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class AbilityRegistry {
    private static final Map<String, Abilities> ABILITIES = new HashMap<>();
    private static final Map<String, AbilitySettings> SETTINGS = new HashMap<>();

    public static void reload(ResourceManager mgr) {
        SETTINGS.clear();
        for (String abilityId : ABILITIES.keySet()) {
            SETTINGS.put(abilityId, AbilitySettings.load(abilityId, mgr));
        }
    }
    public static AbilitySettings settings(String id) {
        return SETTINGS.getOrDefault(id, AbilitySettings.DEFAULT);
    }

    public static void register(String id, Abilities a) {
        ABILITIES.put(id, a);
    }

    public static @Nullable Abilities get(String id) {
        return ABILITIES.get(id);
    }

    public static void register() {
    }
}