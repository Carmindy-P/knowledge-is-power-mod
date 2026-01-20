package net.carmindy.kipmod.abilities;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class FireProtectionAbility implements Abilities{
    private int getDuration() {
        return AbilityRegistry.settings(getId()).durationTicks();
    }

    private long activationTime = -1;

    @Override
    public String getId() {
        return "fire_protection";
    }

    @Override
    public String getName() {
        return "Fire Protection";
    }

    @Override
    public String getDescription() {
        return "Fire Protection Ability";
    }

    @Override
    public boolean isOneTimeUse() {
        return false;
    }

    @Override
    public void activate(ServerPlayerEntity player) {
        if (player.getWorld().isClient()) return;
        int duration = getDuration();
        player.addStatusEffect(new StatusEffectInstance(
                StatusEffects.FIRE_RESISTANCE,
                duration,
                5000, false, false, false));
        activationTime = player.getWorld().getTime();
        if (!player.hasStatusEffect(StatusEffects.FIRE_RESISTANCE))
            player.sendMessage(Text.literal("Fire Resistance!"), false);
    }

    @Override
    public int getCooldownTicks() {
        return AbilityRegistry.settings(getId()).cooldownTicks();
    }

    @Override
    public void tick(ServerPlayerEntity player) {
        if (player.getWorld().isClient()) return;

        if (activationTime != -1) {
            long currentTime = player.getWorld().getTime();
            if (currentTime - activationTime >= getDuration()) {
                player.removeStatusEffect(StatusEffects.FIRE_RESISTANCE);
                activationTime = -1;
            }
        }
    }

    @Override
    public void deactivate(ServerPlayerEntity player) {
        player.removeStatusEffect(StatusEffects.FIRE_RESISTANCE);
        activationTime = -1;
    }
}

