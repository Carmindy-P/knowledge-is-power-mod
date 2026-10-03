package net.carmindy.kipmod.abilities;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class FireProtectionAbility implements Abilities {

    @Override public String getId() { return "fire_protection"; }
    @Override public String getName() { return "Fire Protection"; }
    @Override public String getDescription() { return "Permanent fire resistance while equipped."; }
    @Override public boolean isOneTimeUse() { return false; }
    @Override public int getCooldownTicks() { return 0; }   // never goes on cooldown

    @Override
    public void activate(ServerPlayerEntity player) {
        if (player.getWorld().isClient()) return;
        player.sendMessage(Text.literal("Fire-protection aura active."), false);
    }

    @Override
    public void tick(ServerPlayerEntity player) {
        if (player.getWorld().isClient()) return;

        if (!player.hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) {
            int duration = AbilityRegistry.settings(getId()).durationTicks();
            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.FIRE_RESISTANCE,
                    duration,
                    0,
                    false, false, false));
        }
    }

    @Override
    public void deactivate(ServerPlayerEntity player) {
        player.removeStatusEffect(StatusEffects.FIRE_RESISTANCE);
    }
}