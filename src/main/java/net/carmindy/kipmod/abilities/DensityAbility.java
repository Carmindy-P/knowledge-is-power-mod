package net.carmindy.kipmod.abilities;

import net.minecraft.block.Blocks;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class DensityAbility implements Abilities {

    @Override public String getId()   { return "density"; }
    @Override public String getName() { return "Density"; }
    @Override public String getDescription() { return "So dense the ground beneath you breaks."; }
    @Override public boolean isOneTimeUse() { return false; }
    @Override public int getCooldownTicks() { return 0; }

    @Override public void activate(ServerPlayerEntity player) { }
    @Override public void deactivate(ServerPlayerEntity player) { }

    @Override
    public void tick(ServerPlayerEntity player) {
        if (player.getWorld().isClient()) return;
        if (!player.isOnGround()) return;

        ServerWorld world = player.getServerWorld();
        BlockPos pos = player.getBlockPos().down();

        if (world.getBlockState(pos).isOf(Blocks.BEDROCK)) return;
        if (world.getBlockState(pos).isAir()) return;

        world.breakBlock(pos, true, player);
    }
}