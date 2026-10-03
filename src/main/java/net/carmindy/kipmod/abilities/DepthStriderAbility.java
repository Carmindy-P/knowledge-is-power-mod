package net.carmindy.kipmod.abilities;

import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class DepthStriderAbility implements Abilities {

    @Override public String getId()   { return "depth_strider"; }
    @Override public String getName() { return "Depth Strider"; }
    @Override public String getDescription() { return "Speed underwater, walk on water."; }
    @Override public boolean isOneTimeUse() { return false; }

    @Override
    public void activate(ServerPlayerEntity player) {
        if (player.getWorld().isClient()) return;

        int duration = AbilityRegistry.settings(getId()).durationTicks();
        player.addStatusEffect(new StatusEffectInstance(
                StatusEffects.DOLPHINS_GRACE,
                duration,
                0,
                false, false, false));

        player.sendMessage(Text.literal("Depth Strider!"), false);
    }

    @Override
    public void tick(ServerPlayerEntity player) {
        if (player.getWorld().isClient()) return;

        // Walk on water: freeze top layer like Frost Walker
        if (player.isOnGround()) {
            ServerWorld world = player.getServerWorld();
            BlockPos pos = player.getBlockPos().down();
            if (world.getBlockState(pos).isOf(Blocks.WATER)) {
                world.setBlockState(pos, Blocks.FROSTED_ICE.getDefaultState());
            }
        }
    }

    @Override public int getCooldownTicks() {
        return AbilityRegistry.settings(getId()).cooldownTicks();
    }

    @Override public void deactivate(ServerPlayerEntity player) {
        player.removeStatusEffect(StatusEffects.DOLPHINS_GRACE);
    }
}