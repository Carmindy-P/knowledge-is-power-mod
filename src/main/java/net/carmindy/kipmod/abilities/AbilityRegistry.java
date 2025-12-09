package net.carmindy.kipmod.abilities;

import java.util.HashMap;
import java.util.Map;

public class AbilityRegistry {
    private static final Map<String, Abilities> MAP = new HashMap<>();

    public static void register(String id, Abilities ability) {
        MAP.put(id, ability);
    }

    public static Abilities get(String id) {
        return MAP.get(id);
    }
}
