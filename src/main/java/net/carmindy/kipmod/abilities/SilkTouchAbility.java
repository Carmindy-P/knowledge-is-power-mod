package net.carmindy.kipmod.abilities;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

public class SilkTouchAbility implements Abilities {

    @Override public String getId()   { return "silk_touch"; }
    @Override public String getName() { return "Silk Touch"; }
    @Override public String getDescription() { return "Pick up any block like an enderman."; }
    @Override public boolean isOneTimeUse() { return false; }

    @Override
    public void activate(ServerPlayerEntity player) {
        if (!(player.getWorld() instanceof ServerWorld)) return;

        double range = AbilityRegistry.settings(getId()).range();
        var hit = player.raycast(range, 0, false);

        if (hit.getType() != HitResult.Type.BLOCK || !(hit instanceof BlockHitResult bhr)) {
            player.sendMessage(Text.literal("No block in range."), false);
            return;
        }

        ServerWorld world = player.getServerWorld();
        BlockPos pos = bhr.getBlockPos();
        var state = world.getBlockState(pos);

        if (state.isOf(Blocks.BEDROCK) || state.isOf(Blocks.AIR) ||
                state.isOf(Blocks.WATER) || state.isOf(Blocks.LAVA)) {
            player.sendMessage(Text.literal("Cannot pick up that block."), false);
            return;
        }

        var item = state.getBlock().asItem();
        if (item == null || item == Items.AIR) {
            player.sendMessage(Text.literal("Cannot pick up that block."), false);
            return;
        }

        player.giveItemStack(new ItemStack(item));
        world.setBlockState(pos, Blocks.AIR.getDefaultState());
        player.sendMessage(Text.literal("Silk Touch!"), false);
    }

    @Override public void tick(ServerPlayerEntity player) { }

    @Override public int getCooldownTicks() {
        return AbilityRegistry.settings(getId()).cooldownTicks();
    }

    @Override public void deactivate(ServerPlayerEntity player) { }
}