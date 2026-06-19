package net.carmindy.kipmod.abilities;

import net.minecraft.server.network.ServerPlayerEntity;

public interface Abilities {

    String getId();
    String getName();
    String getDescription();

    void activate(ServerPlayerEntity player);
    void tick(ServerPlayerEntity player);

    boolean isOneTimeUse();
    int getCooldownTicks();
    default void deactivate(ServerPlayerEntity player) { }
    default void onApply(ServerPlayerEntity player) {
    }
}