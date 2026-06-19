package net.carmindy.kipmod.abilities;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class QuickChargeAbility implements Abilities {

    @Override public String getId()   { return "quick_charge"; }
    @Override public String getName() { return "Quick Charge"; }
    @Override public String getDescription() { return "Insane speed boost for 3 seconds."; }
    @Override public boolean isOneTimeUse() { return false; }

    @Override
    public void activate(ServerPlayerEntity player) {
        if (player.getWorld().isClient()) return;

        int duration = AbilityRegistry.settings(getId()).durationTicks();
        player.addStatusEffect(new StatusEffectInstance(
                StatusEffects.SPEED,
                duration,
                4, // Speed V
                false, false, false));

        player.sendMessage(Text.literal("Quick Charge!"), false);
    }

    @Override public void tick(ServerPlayerEntity player) { }

    @Override public int getCooldownTicks() {
        return AbilityRegistry.settings(getId()).cooldownTicks();
    }

    @Override public void deactivate(ServerPlayerEntity player) {
        player.removeStatusEffect(StatusEffects.SPEED);
    }
}