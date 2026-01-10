package net.carmindy.kipmod;

import net.carmindy.kipmod.data.KIPModComponents;
import net.carmindy.kipmod.network.AbilityUsePayload;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.codec.PacketCodec;

public class KIPModServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        KIPModComponents.class.getName();
        registerServerPackets();
    }

    public static void registerServerPackets() {
        PayloadTypeRegistry.playC2S().register(
                AbilityUsePayload.ID,
                PacketCodec.of(AbilityUsePayload::encode, AbilityUsePayload::decode)
        );

        ServerPlayNetworking.registerGlobalReceiver(
                AbilityUsePayload.ID,
                (payload, ctx) -> ctx.server().execute(() ->
                        KIPModComponents.ABILITIES.get(ctx.player()).tryUseAbility())
        );
    }
}