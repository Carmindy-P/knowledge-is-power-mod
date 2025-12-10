package net.carmindy.kipmod.data;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Server-side registry that tracks which player (by UUID) has which ability.
 * Uses a thread-safe ConcurrentHashMap for safe access in multiplayer environments.
 */
public class AbilityBookServerRegistry {
    private static final Map<UUID, String> MAP = new ConcurrentHashMap<>();

    // Registers an ability for a player
    public static void register(UUID id, String abilityId) {
        MAP.put(id, abilityId);
    }

    // Retrieves a player's ability by UUID
    public static String getAbilityId(UUID id) {
        return MAP.get(id);
    }

    // Removes a player's ability from the registry
    public static void unregister(UUID id) {
        MAP.remove(id);
    }

    // Checks if a player has a registered ability
    public static boolean contains(UUID id) {
        return MAP.containsKey(id);
    }
}
