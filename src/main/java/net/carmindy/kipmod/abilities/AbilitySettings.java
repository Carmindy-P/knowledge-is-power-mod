package net.carmindy.kipmod.abilities;

import com.google.gson.Gson;
import net.carmindy.kipmod.KnowledgeIsPowerMod;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import java.io.InputStream;
import java.io.InputStreamReader;

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
    public static final AbilitySettings DEFAULT =
            new AbilitySettings("dummy", 100, 200, 10.0, 4, 0.1, 10,10, 3);

    public static AbilitySettings load(String abilityId, ResourceManager mgr) {
        Identifier loc = Identifier.of("kipmod", "ability_settings/" + abilityId + ".json");
        try (InputStream in = mgr.getResource(loc).get().getInputStream()) {
            return new Gson().fromJson(new InputStreamReader(in), AbilitySettings.class);
        } catch (Exception ex) {
            return DEFAULT;
        }
    }

}