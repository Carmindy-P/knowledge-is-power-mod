package net.carmindy.kipmod.abilities;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class SoulSpeedAbility implements Abilities {

    @Override public String getId()   { return "soul_speed"; }
    @Override public String getName() { return "Soul Speed"; }
    @Override public String getDescription() { return "Turn into a fast ghost for 4 seconds."; }
    @Override public boolean isOneTimeUse() { return false; }

    @Override
    public void activate(ServerPlayerEntity player) {
        if (player.getWorld().isClient()) return;

        int duration = AbilityRegistry.settings(getId()).durationTicks();

        player.addStatusEffect(new StatusEffectInstance(
                StatusEffects.SPEED,
                duration,
                2, // Speed III
                false, false, false));

        player.addStatusEffect(new StatusEffectInstance(
                StatusEffects.INVISIBILITY,
                duration,
                0,
                false, false, false));

        player.sendMessage(Text.literal("Soul Speed!"), false);
    }

    @Override public void tick(ServerPlayerEntity player) { }

    @Override public int getCooldownTicks() {
        return AbilityRegistry.settings(getId()).cooldownTicks();
    }

    @Override public void deactivate(ServerPlayerEntity player) {
        player.removeStatusEffect(StatusEffects.SPEED);
        player.removeStatusEffect(StatusEffects.INVISIBILITY);
    }
}