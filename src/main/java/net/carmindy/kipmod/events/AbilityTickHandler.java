package net.carmindy.kipmod.events;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import net.carmindy.kipmod.data.KIPModComponents;
import net.carmindy.kipmod.abilities.Abilities;

public class AbilityTickHandler {
    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(AbilityTickHandler::onServerTick);
    }

    private static void onServerTick(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {

            var component = KIPModComponents.ABILITIES.get(player);
            var ability = component.getAbility();

            // decrement cooldown
            component.tickCooldown();

            if (ability == null) {
                // remove the noisy debug messages — keep silent when no ability
                continue;
            }

            // call the per-tick passive hook
            ability.tick(player);
        }
    }
}
