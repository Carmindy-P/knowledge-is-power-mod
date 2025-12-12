package net.carmindy.kipmod.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class AbilityUsePacket {
    public static final Identifier ID = Identifier.of("kipmod", "use_ability");

    private final String abilityId;

    public AbilityUsePacket(String abilityId) {
        this.abilityId = abilityId;
    }

    public void write(PacketByteBuf buf) {
        buf.writeString(abilityId);
    }

    public static AbilityUsePacket read(PacketByteBuf buf) {
        return new AbilityUsePacket(buf.readString(32767));
    }

    public String getAbilityId() {
        return abilityId;
    }
}
