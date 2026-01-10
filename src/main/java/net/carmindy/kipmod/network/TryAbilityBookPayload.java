package net.carmindy.kipmod.network;


import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

// net.carmindy.kipmod.network.TryAbilityBookPayload
public record TryAbilityBookPayload() implements CustomPayload {
    public static final Id<TryAbilityBookPayload> ID =
            new Id<>(Identifier.of("knowledge_is_power_mod", "try_book"));

    @Override public Id<? extends CustomPayload> getId() { return ID; }

    public static TryAbilityBookPayload decode(PacketByteBuf buf) { return new TryAbilityBookPayload(); }
    public void encode(PacketByteBuf buf) { }
}
