package net.carmindy.kipmod.abilities;

import net.minecraft.server.network.ServerPlayerEntity;

public interface Abilities {
    String getId(); // Add this
    String getName();
    String getDescription();
    void activate(ServerPlayerEntity player);
    boolean isOneTimeUse();
    int getCooldownTicks();
}
