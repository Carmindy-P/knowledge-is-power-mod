package net.carmindy.kipmod.abilities;

import net.carmindy.kipmod.data.KIPModComponents;
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
        return "Haste";
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
    public int getCooldownTicks() {
        return 20 * 10; // 10 seconds
    }

    @Override
    public void activate(ServerPlayerEntity player) {
        if (player.getWorld().isClient()) return;

        player.addStatusEffect(new StatusEffectInstance(
                StatusEffects.HASTE,
                10 * 20,
                5000,
                false, false, true));

        player.addStatusEffect(new StatusEffectInstance(
                StatusEffects.SPEED,
                10 * 20,
                0,
                false, false, true));

        player.sendMessage(Text.literal("Haste surge!"), false);


        KIPModComponents.ABILITIES.get(player).setInstamine(true);
    }

    @Override
    public void tick(ServerPlayerEntity player) {

        if (!player.hasStatusEffect(StatusEffects.HASTE)) {
            KIPModComponents.ABILITIES.get(player).setInstamine(false);
        }
    }
}