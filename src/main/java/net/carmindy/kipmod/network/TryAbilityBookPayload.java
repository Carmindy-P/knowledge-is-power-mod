package net.carmindy.kipmod.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record TryAbilityBookPayload() implements CustomPayload {

    public static final CustomPayload.Id<TryAbilityBookPayload> ID =
            new CustomPayload.Id<>(
                    Identifier.of("knowledge-is-power-mod", "try_book")
            );

    public static final TryAbilityBookPayload INSTANCE =
            new TryAbilityBookPayload();

    public static final PacketCodec<RegistryByteBuf, TryAbilityBookPayload> CODEC =
            PacketCodec.unit(INSTANCE);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    // Compatibility with existing code
    public static TryAbilityBookPayload decode(RegistryByteBuf buf) {
        return INSTANCE;
    }
}