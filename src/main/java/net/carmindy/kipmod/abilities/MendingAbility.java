package net.carmindy.kipmod.abilities;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.server.network.ServerPlayerEntity;

public class MendingAbility implements Abilities {

    @Override
    public String getId() {
        return "mending";
    }

    @Override
    public String getName() {
        return "Mending";
    }

    @Override
    public String getDescription() {
        return "Heals hearts when gaining XP orbs.";
    }

    @Override
    public void activate(ServerPlayerEntity player) {
        // This ability does not need an activation method
    }

    @Override
    public void tick(ServerPlayerEntity player) {
        // This ability will be handled in the player's component
    }

    @Override
    public boolean isOneTimeUse() {
        return false;
    }

    @Override
    public int getCooldownTicks() {
        return 0; // No cooldown for this ability
    }
}