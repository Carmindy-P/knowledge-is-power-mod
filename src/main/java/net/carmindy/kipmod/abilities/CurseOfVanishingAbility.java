package net.carmindy.kipmod.abilities;

import net.carmindy.kipmod.data.KIPModComponents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class CurseOfVanishingAbility implements Abilities{
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
        return "10s of Invisibility";
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
                10 * 20,
                0,
                false, false, false));
        if (!player.hasStatusEffect(StatusEffects.INVISIBILITY)) {
            player.sendMessage(Text.literal("Invisibility!"), false);
        }

}

    @Override
    public void tick(ServerPlayerEntity player) {

    }
}
