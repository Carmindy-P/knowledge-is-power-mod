package net.carmindy.kipmod;

import net.carmindy.kipmod.abilities.AbilityRegistry;
import net.carmindy.kipmod.abilities.FlameAbility;
import net.carmindy.kipmod.events.KIPModEvents;
import net.fabricmc.api.ModInitializer;

/**
 * Main entry point for the mod.
 * This class runs on both server and client and sets up the mod.
 */
public class KnowledgeIsPowerMod implements ModInitializer {

    public static final String MOD_ID = "knowledge-is-power-mod";

    @Override
    public void onInitialize() {
        // Register abilities (so AbilityRegistry.get("flame") works)
        AbilityRegistry.register("flame", new FlameAbility());

        // Register event handlers, such as using books
        KIPModEvents.register();

        // Runtime confirmation in the log
        System.out.println(MOD_ID + " initialized: abilities and events registered");
    }
}
