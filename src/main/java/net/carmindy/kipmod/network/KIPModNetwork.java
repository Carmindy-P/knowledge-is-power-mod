
package net.carmindy.kipmod.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class KIPModNetwork {

    public static void register() {

        // Client -> Server
        PayloadTypeRegistry.playC2S().register(
                AbilityUsePayload.ID,
                AbilityUsePayload.CODEC
        );

        PayloadTypeRegistry.playC2S().register(
                TryAbilityBookPayload.ID,
                TryAbilityBookPayload.CODEC
        );

        // Server -> Client
        PayloadTypeRegistry.playS2C().register(
                AbilityCooldownPayload.ID,
                AbilityCooldownPayload.CODEC
        );
    }
}

