package net.carmindy.kipmod.component;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class AbilityCooldownTicker {

    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            server.getPlayerManager().getPlayerList().forEach(player -> {
                KIPModComponents.ABILITIES.get(player).tickCooldown();
            });
        });
    }
}