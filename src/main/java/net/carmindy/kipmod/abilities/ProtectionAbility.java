package net.carmindy.kipmod.abilities;

import net.carmindy.kipmod.data.KIPModComponents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;

public class ProtectionAbility implements  Abilities{
    public static final int REGENERATION_DURATION_TICKS = 20*10;
    public static final int RADIUS = 10;

    @Override
    public String getId() {
        return "protection";
    }

    @Override
    public String getName() {
        return "Regeneration Field";
    }

    @Override
    public String getDescription() {
        return "An AOE of regeneration";
    }

    @Override
    public boolean isOneTimeUse() {
        return false;
    }

    @Override
    public int getCooldownTicks() {
        return 20 * 10;
    }

    @Override
    public void activate(ServerPlayerEntity player) {
        if (player.getWorld().isClient()) return;

        ServerWorld world = player.getServerWorld();
        Box box = Box.of(player.getPos(), RADIUS, RADIUS, RADIUS);

        for (PlayerEntity target : world.getPlayers()) {
            if (target.squaredDistanceTo(player) <= RADIUS * RADIUS) {
                target.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.REGENERATION,
                        REGENERATION_DURATION_TICKS,
                        1,
                        false,true,false
                ));
            }
        }
        player.sendMessage(Text.literal("Regeneration field activated!"), false);
    }

    @Override
    public void tick(ServerPlayerEntity player) {

    }
}
