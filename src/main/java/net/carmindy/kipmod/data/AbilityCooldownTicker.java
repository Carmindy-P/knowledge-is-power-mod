package net.carmindy.kipmod.data;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class AbilityCooldownTicker {

    public static void init() {
        // END phase = run after entities, before networking
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            server.getPlayerManager().getPlayerList().forEach(player -> {
                KIPModComponents.ABILITIES.get(player).tickCooldown();
            });
        });
    }
}