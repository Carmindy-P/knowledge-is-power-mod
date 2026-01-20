package net.carmindy.kipmod.data;

import com.google.gson.Gson;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public record AbilitySettings(
        String id,
        int durationTicks,
        int cooldownTicks,
        double range,
        int fireSeconds,
        double heartsPerOrb,
        int orbDivisor,
        int radius,
        int times
) {
    private static final Logger LOG = LoggerFactory.getLogger("kipmod");
    private static final Gson GSON = new Gson();

    public static final AbilitySettings DEFAULT =
            new AbilitySettings("dummy", 100, 200, 10.0, 4, 0.1, 10, 10, 3);

    private static final Map<String, AbilitySettings> CACHE = new HashMap<>();

    public static AbilitySettings load(String abilityId, ResourceManager mgr) {
        return CACHE.computeIfAbsent(abilityId, id -> {
            Identifier loc = Identifier.of("kipmod", "ability_settings/" + id + ".json");
            try (var res = mgr.getResource(loc).orElse(null);
                 var reader = res != null ? new InputStreamReader(res.getInputStream()) : null) {
                if (reader != null) return GSON.fromJson(reader, AbilitySettings.class);
            } catch (Exception e) {
                LOG.warn("Could not load settings for {}, using default", id);
            }
            return DEFAULT;
        });
    }

    public static void clearCache() { CACHE.clear(); }
}