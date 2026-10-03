package net.carmindy.kipmod.abilities;

import net.minecraft.resource.ResourceManager;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class AbilityRegistry {

    private static final Map<String, Abilities> ABILITIES = new HashMap<>();
    private static final Map<String, AbilitySettings> SETTINGS = new HashMap<>();

    private AbilityRegistry() {
    }

    public static void register(String id, Abilities ability) {
        ABILITIES.put(id, ability);
    }

    public static @Nullable Abilities get(String id) {
        return ABILITIES.get(id);
    }

    public static Collection<Abilities> getAllAbilities() {
        return ABILITIES.values();
    }

    public static int size() {
        return ABILITIES.size();
    }

    public static void reload(ResourceManager manager) {
        SETTINGS.clear();

        for (String abilityId : ABILITIES.keySet()) {
            SETTINGS.put(
                    abilityId,
                    AbilitySettings.load(abilityId, manager)
            );
        }
    }

    public static AbilitySettings settings(String id) {
        return SETTINGS.getOrDefault(
                id,
                AbilitySettings.DEFAULT
        );
    }
}