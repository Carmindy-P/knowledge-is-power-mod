package net.carmindy.kipmod;

import net.carmindy.kipmod.abilities.ModAbilities;
import net.carmindy.kipmod.events.AbilityTickHandler;
import net.carmindy.kipmod.events.BookUseHandler;
import net.carmindy.kipmod.network.AbilityPackets;

import net.fabricmc.api.ModInitializer;

public class KnowledgeIsPowerMod implements ModInitializer {

    public static final String MOD_ID = "knowledge-is-power-mod";

    @Override
    public void onInitialize() {

        System.out.println("KIP Mod initializing...");

        ModAbilities.register();
        BookUseHandler.registerHandler();

        // Register server-side ability packets
        AbilityPackets.register();
        AbilityTickHandler.register();
        System.out.println("Handlers registered.");
    }
}
