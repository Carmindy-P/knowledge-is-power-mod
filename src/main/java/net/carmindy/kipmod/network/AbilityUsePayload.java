package net.carmindy.kipmod.network;

import net.fabricmc.fabric.api.networking.v1.CustomPayload;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public record AbilityUsePayload(String abilityId) implements CustomPayload {

    public static final CustomPayload.Id<AbilityUsePayload> ID =
            new CustomPayload.Id<>(Identifier.of("knowledge_is_power_mod", "use_ability"));

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static AbilityUsePayload decode(PacketByteBuf buf) {
        return new AbilityUsePayload(buf.readString(32767));
    }

    public void encode(PacketByteBuf buf) {
        buf.writeString(abilityId);
    }
}
