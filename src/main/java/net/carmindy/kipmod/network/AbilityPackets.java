package net.carmindy.kipmod.network;

import net.carmindy.kipmod.data.KIPModComponents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class AbilityPackets {
    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(
                AbilityUsePayload.ID,
                (payload, context) -> {
                    /* 1. make sure we are on the server thread */
                    context.server().execute(() -> {
                        var player   = context.player();
                        var component = KIPModComponents.ABILITIES.get(player);
                        component.tryUseAbility();
                    });
                }
        );
    }
}