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

    /** Called when this ability is assigned to a player */
    default void onApply(ServerPlayerEntity player) {
        // default: do nothing
    }
}
