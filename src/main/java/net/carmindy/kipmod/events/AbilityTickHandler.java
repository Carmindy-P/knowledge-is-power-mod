package net.carmindy.kipmod.events;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import net.carmindy.kipmod.component.KIPModComponents;

public class AbilityTickHandler {
    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(AbilityTickHandler::onServerTick);
    }

    private static void onServerTick(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            KIPModComponents.ABILITIES.maybeGet(player).ifPresent(comp -> {
                comp.tickCooldown();
                if (comp.getAbility() != null) comp.getAbility().tick(player);
            });
        }
    }
}