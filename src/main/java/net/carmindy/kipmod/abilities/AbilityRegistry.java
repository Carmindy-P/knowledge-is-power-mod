package net.carmindy.kipmod.abilities;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry to manage all abilities.
 * Stores abilities by their unique ID.
 */
public class AbilityRegistry {
    private static final Map<String, Abilities> MAP = new HashMap<>();

    // Registers a new ability with its ID
    public static void register(String id, Abilities ability) {
        MAP.put(id, ability);
    }

    // Retrieves an ability by its ID
    public static Abilities get(String id) {
        return MAP.get(id);
    }
}
