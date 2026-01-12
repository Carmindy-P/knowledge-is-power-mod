package net.carmindy.kipmod.events;

import net.carmindy.kipmod.component.KIPModComponents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.server.world.ServerWorld;

public class EffBreakHandler {

    public static void register() {
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, entity) -> {
            if (world.isClient) return true;

            if (KIPModComponents.ABILITIES.get(player).isInstamine()) {
                ((ServerWorld) world).breakBlock(pos, true, player);
                return false; // cancel vanilla
            }
            return true;
        });
    }
}
