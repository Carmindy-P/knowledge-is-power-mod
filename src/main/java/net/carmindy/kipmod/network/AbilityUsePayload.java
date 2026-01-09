package net.carmindy.kipmod.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record AbilityUsePayload() implements CustomPayload {

    public static final CustomPayload.Id<AbilityUsePayload> ID =
            new CustomPayload.Id<>(Identifier.of("knowledge_is_power_mod", "use_ability"));

    public static final AbilityUsePayload INSTANCE = new AbilityUsePayload();

    public static AbilityUsePayload decode(PacketByteBuf buf) { return INSTANCE; }
    public void encode(PacketByteBuf buf) { }

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() { return ID; }
}