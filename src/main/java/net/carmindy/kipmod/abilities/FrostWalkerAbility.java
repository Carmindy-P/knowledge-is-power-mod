package net.carmindy.kipmod.abilities;

import net.minecraft.block.Blocks;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class FrostWalkerAbility implements Abilities {

    @Override public String getId()   { return "frost_walker"; }
    @Override public String getName() { return "Frost Walker"; }
    @Override public String getDescription() { return "A path of ice forms beneath your feet."; }
    @Override public boolean isOneTimeUse() { return false; }

    @Override
    public void activate(ServerPlayerEntity player) {
        player.sendMessage(Text.literal("Frost Walker active!"), false);
    }

    @Override
    public void tick(ServerPlayerEntity player) {
        if (player.getWorld().isClient()) return;
        if (!player.isOnGround()) return;

        ServerWorld world = player.getServerWorld();
        int radius = AbilityRegistry.settings(getId()).radius();
        BlockPos center = player.getBlockPos().down();

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                if (dx * dx + dz * dz > radius * radius) continue;

                BlockPos pos = center.add(dx, 0, dz);
                if (world.getBlockState(pos).isOf(Blocks.WATER)) {
                    world.setBlockState(pos, Blocks.FROSTED_ICE.getDefaultState());
                }
            }
        }
    }

    @Override public int getCooldownTicks() { return 0; }

    @Override public void deactivate(ServerPlayerEntity player) { }
}