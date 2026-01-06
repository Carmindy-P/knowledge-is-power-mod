package net.carmindy.kipmod.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;

public class KIPModNetwork {
    public static void register() {
        // 1. create a codec once
        PacketCodec<PacketByteBuf, AbilityUsePayload> CODEC = PacketCodec.of(
                AbilityUsePayload::encode,          // writer
                AbilityUsePayload::decode           // reader
        );

        // 2. register it
        PayloadTypeRegistry.playC2S().register(AbilityUsePayload.ID, CODEC);
    }
}