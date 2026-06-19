package net.carmindy.kipmod.abilities;

import net.carmindy.kipmod.component.KIPModComponents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class EfficiencyAbility implements Abilities {

    @Override
    public String getId() {
        return "efficiency";
    }

    @Override
    public String getName() {
        return "Instamine";
    }

    @Override
    public String getDescription() {
        return "10 s of Haste, Speed and netherite-level harvest.";
    }

    @Override
    public boolean isOneTimeUse() {
        return false;
    }

    @Override
    public void activate(ServerPlayerEntity player) {
        if (player.getWorld().isClient()) return;

        AbilitySettings cfg = AbilityRegistry.settings(getId());
        int duration = cfg.durationTicks();

        player.addStatusEffect(new StatusEffectInstance(
                StatusEffects.HASTE,
                duration,
                5000,
                false, false, false));

        player.addStatusEffect(new StatusEffectInstance(
                StatusEffects.SPEED,
                duration,
                0,
                false, false, false));

        player.sendMessage(Text.literal("Instamine!"), false);
        KIPModComponents.ABILITIES.get(player).setInstamine(true);
    }

    @Override
    public int getCooldownTicks() {
        return AbilityRegistry.settings(getId()).cooldownTicks();
    }

    @Override
    public void tick(ServerPlayerEntity player) {

        if (!player.hasStatusEffect(StatusEffects.HASTE)) {
            KIPModComponents.ABILITIES.get(player).setInstamine(false);
        }
    }

    @Override
    public void deactivate(ServerPlayerEntity player) {
        player.removeStatusEffect(StatusEffects.HASTE);
        player.removeStatusEffect(StatusEffects.SPEED);
        KIPModComponents.ABILITIES.get(player).setInstamine(false);
    }
}