package net.carmindy.kipmod.abilities;

import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.network.packet.s2c.play.PlayerAbilitiesS2CPacket;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class FeatherFallingAbility implements Abilities {
    private static final int FLIGHT_DURATION_TICKS = 20 * 15;
    private long activationTime = -1;

    @Override
    public String getId() {
        return "feather_falling";
    }

    @Override
    public String getName() {
        return "Flight";
    }

    @Override
    public String getDescription() {
        return "Allows you to fly for fifteen seconds and prevents fall damage when the ability expires.";
    }

    @Override
    public void activate(ServerPlayerEntity player) {
        if (player.getWorld().isClient()) return;

        // Set the activation time to the current tick
        activationTime = player.getWorld().getTime();
        player.sendMessage(Text.literal("Fly!"), false);

        // Enable flying
        PlayerAbilities abilities = player.getAbilities();
        abilities.allowFlying = true;
        abilities.flying = true;
        player.networkHandler.sendPacket(new PlayerAbilitiesS2CPacket(abilities));
    }

    @Override
    public void tick(ServerPlayerEntity player) {
        if (player.getWorld().isClient()) return;

        if (activationTime != -1) {
            long currentTime = player.getWorld().getTime();
            if (currentTime - activationTime < FLIGHT_DURATION_TICKS) {
                // Player is still within the flight duration
                PlayerAbilities abilities = player.getAbilities();
                abilities.allowFlying = true;
                abilities.flying = true;
                player.networkHandler.sendPacket(new PlayerAbilitiesS2CPacket(abilities)); // Update the player's abilities
            } else {
                // Flight duration has expired
                PlayerAbilities abilities = player.getAbilities();
                abilities.allowFlying = false;
                abilities.flying = false;
                player.networkHandler.sendPacket(new PlayerAbilitiesS2CPacket(abilities)); // Update the player's abilities
                player.sendMessage(Text.literal("Flight ability has expired."), false);

                // Apply Feather Falling effect to prevent fall damage
                applyFeatherFallingEffect(player);

                activationTime = -1; // Reset the activation time
            }
        }
    }

    private void applyFeatherFallingEffect(ServerPlayerEntity player) {
        // Apply the Feather Falling effect for a short duration (e.g., 5 seconds)
        StatusEffectInstance featherFallingEffect = new StatusEffectInstance(StatusEffects.SLOW_FALLING, 20 * 10, 0, false, false);
        player.addStatusEffect(featherFallingEffect);
    }

    @Override
    public boolean isOneTimeUse() {
        return false;
    }

    @Override
    public int getCooldownTicks() {
        return 20 * 60;
    }
}