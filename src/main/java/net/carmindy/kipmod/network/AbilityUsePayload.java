package net.carmindy.kipmod.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record AbilityUsePayload() implements CustomPayload {

    public static final CustomPayload.Id<AbilityUsePayload> ID =
            new CustomPayload.Id<>(
                    Identifier.of("knowledge-is-power-mod", "use_ability")
            );

    public static final AbilityUsePayload INSTANCE =
            new AbilityUsePayload();

    public static final PacketCodec<RegistryByteBuf, AbilityUsePayload> CODEC =
            PacketCodec.unit(INSTANCE);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    // Compatibility with existing code
    public static AbilityUsePayload decode(RegistryByteBuf buf) {
        return INSTANCE;
    }
}