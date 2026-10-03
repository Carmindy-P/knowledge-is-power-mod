package net.carmindy.kipmod.abilities;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;

public class SwiftSneakAbility implements Abilities {

    @Override public String getId()   { return "swift_sneak"; }
    @Override public String getName() { return "Swift Sneak"; }
    @Override public String getDescription() { return "Nothing can hear you while sneaking."; }
    @Override public boolean isOneTimeUse() { return false; }
    @Override public int getCooldownTicks() { return 0; }

    @Override public void activate(ServerPlayerEntity player) { }

    @Override
    public void tick(ServerPlayerEntity player) {
        if (player.getWorld().isClient()) return;

        boolean sneaking = player.isSneaking();
        boolean hasInvis = player.hasStatusEffect(StatusEffects.INVISIBILITY);

        if (sneaking && !hasInvis) {
            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.INVISIBILITY,
                    60,
                    0,
                    false, false, false));
        } else if (!sneaking && hasInvis) {
            player.removeStatusEffect(StatusEffects.INVISIBILITY);
        }
    }

    @Override public void deactivate(ServerPlayerEntity player) {
        player.removeStatusEffect(StatusEffects.INVISIBILITY);
    }
}