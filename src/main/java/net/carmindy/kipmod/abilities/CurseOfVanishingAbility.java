package net.carmindy.kipmod.abilities;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class CurseOfVanishingAbility implements Abilities {
    private static final int INVISIBILITY_DURATION_TICKS = 20 * 5; // 5 seconds
    private long activationTime = -1;

    @Override
    public String getId() {
        return "vanishing_curse";
    }

    @Override
    public String getName() {
        return "Invisibility";
    }

    @Override
    public String getDescription() {
        return "Invisibility";
    }

    @Override
    public boolean isOneTimeUse() {
        return false;
    }

    @Override
    public int getCooldownTicks() {
        return 20 * 10; // 10 seconds
    }

    @Override
    public void activate(ServerPlayerEntity player) {
        if (player.getWorld().isClient()) return;

        player.addStatusEffect(new StatusEffectInstance(
                StatusEffects.INVISIBILITY,
                INVISIBILITY_DURATION_TICKS,
                0,
                false, false, false));

        activationTime = player.getWorld().getTime();

        if (!player.hasStatusEffect(StatusEffects.INVISIBILITY)) {
            player.sendMessage(Text.literal("Invisibility!"), false);
        }
    }

    @Override
    public void tick(ServerPlayerEntity player) {
        if (player.getWorld().isClient()) return;

        if (activationTime != -1) {
            long currentTime = player.getWorld().getTime();
            if (currentTime - activationTime >= INVISIBILITY_DURATION_TICKS) {
                player.removeStatusEffect(StatusEffects.INVISIBILITY);
                activationTime = -1;
            }
        }
    }

    @Override
    public void deactivate(ServerPlayerEntity player) {
        player.removeStatusEffect(StatusEffects.INVISIBILITY);
        activationTime = -1;
    }
}