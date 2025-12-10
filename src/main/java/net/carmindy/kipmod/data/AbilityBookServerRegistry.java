package net.carmindy.kipmod.data;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class AbilityBookServerRegistry {
    private static final Map<UUID, String> MAP = new ConcurrentHashMap<>();

    public static void register(UUID id, String abilityId) {
        MAP.put(id, abilityId);
    }

    public static String getAbilityId(UUID id) {
        return MAP.get(id);
    }

    public static void unregister(UUID id) {
        MAP.remove(id);
    }

    public static boolean contains(UUID id) {
        return MAP.containsKey(id);
    }
}