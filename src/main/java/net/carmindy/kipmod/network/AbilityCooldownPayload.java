package net.carmindy.kipmod.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record AbilityCooldownPayload(int ticks)
        implements CustomPayload {

    public static final CustomPayload.Id<AbilityCooldownPayload> ID =
            new CustomPayload.Id<>(
                    Identifier.of(
                            "knowledge-is-power-mod",
                            "ability_cooldown"
                    )
            );

    public static final PacketCodec<RegistryByteBuf, AbilityCooldownPayload> CODEC =
            PacketCodec.of(
                    (payload, buf) ->
                            buf.writeVarInt(payload.ticks()),

                    buf ->
                            new AbilityCooldownPayload(
                                    buf.readVarInt()
                            )
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

