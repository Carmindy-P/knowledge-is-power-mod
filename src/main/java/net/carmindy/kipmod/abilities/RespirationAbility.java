package net.carmindy.kipmod.abilities;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;

public class RespirationAbility implements Abilities {

    @Override public String getId()   { return "respiration"; }
    @Override public String getName() { return "Respiration"; }
    @Override public String getDescription() { return "Breathe underwater indefinitely."; }
    @Override public boolean isOneTimeUse() { return false; }
    @Override public int getCooldownTicks() { return 0; }

    @Override public void activate(ServerPlayerEntity player) { }

    @Override
    public void tick(ServerPlayerEntity player) {
        if (player.getWorld().isClient()) return;

        if (!player.hasStatusEffect(StatusEffects.WATER_BREATHING)) {
            int duration = AbilityRegistry.settings(getId()).durationTicks();
            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.WATER_BREATHING,
                    duration,
                    0,
                    false, false, false));
        }
    }

    @Override public void deactivate(ServerPlayerEntity player) {
        player.removeStatusEffect(StatusEffects.WATER_BREATHING);
    }
}