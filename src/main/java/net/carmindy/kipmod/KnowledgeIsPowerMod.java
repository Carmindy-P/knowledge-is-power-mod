package net.carmindy.kipmod;

import net.carmindy.kipmod.abilities.AbilityRegistry;
import net.carmindy.kipmod.abilities.FlameAbility;
import net.carmindy.kipmod.events.KIPModEvents;
import net.fabricmc.api.ModInitializer;

public class KnowledgeIsPowerMod implements ModInitializer {

    public static final String MOD_ID = "knowledge-is-power-mod";

    @Override
    public void onInitialize() {
        // Register abilities so AbilityRegistry.get("flame") returns the ability
        AbilityRegistry.register("flame", new FlameAbility());

        // Register event handlers (book use, etc.)
        KIPModEvents.register();

        // Runtime sanity print (shows up in the game log)
        System.out.println(MOD_ID + " initialized: abilities and events registered");
    }
}