package net.carmindy.kipmod.events;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import net.carmindy.kipmod.data.KIPModComponents;

public class AbilityTickHandler {
    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(AbilityTickHandler::onServerTick);
    }

    private static void onServerTick(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {

            var component = KIPModComponents.ABILITIES.get(player);
            var ability = component.getAbility();

            if (ability == null) {
                // Debug message (remove later)
                player.sendMessage(Text.literal("No ability found"), true);
            } else {
                player.sendMessage(Text.literal("Ability: " + ability.getName()), true);
            }
        }
    }
}
