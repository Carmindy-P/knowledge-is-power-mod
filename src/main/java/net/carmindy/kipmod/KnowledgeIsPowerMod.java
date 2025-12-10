package net.carmindy.kipmod;

import net.carmindy.kipmod.abilities.AbilityRegistry;
import net.carmindy.kipmod.abilities.FlameAbility;
import net.carmindy.kipmod.events.KIPModEvents;
import net.fabricmc.api.ModInitializer;

public class KnowledgeIsPowerMod implements ModInitializer {
    public static final String MOD_ID = "knowledge-is-power-mod";

    @Override
    public void onInitialize() {
        // Register abilities
        AbilityRegistry.register("flame", new FlameAbility());

        // Register events (book use handler, etc.)
        KIPModEvents.register();
    }
}