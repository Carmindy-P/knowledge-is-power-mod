package net.carmindy.kipmod.abilities;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class RiptideAbility implements Abilities {

    @Override public String getId()   { return "riptide"; }
    @Override public String getName() { return "Riptide"; }
    @Override public String getDescription() { return "Trap your target in a cage of water."; }
    @Override public boolean isOneTimeUse() { return false; }

    @Override
    public void activate(ServerPlayerEntity player) {
        if (!(player.getWorld() instanceof ServerWorld)) return;

        AbilitySettings cfg = AbilityRegistry.settings(getId());
        double range = cfg.range();

        Vec3d start = player.getCameraPosVec(1.0F);
        Vec3d rot   = player.getRotationVec(1.0F);
        Vec3d end   = start.add(rot.multiply(range));

        EntityHitResult ehr = ProjectileUtil.raycast(
                player, start, end,
                new Box(start, end).expand(1.0),
                e -> !e.isSpectator() && e.canHit() && e != player,
                range * range);

        Vec3d targetPos;
        if (ehr != null) {
            targetPos = ehr.getEntity().getPos();
        } else {
            var hit = player.raycast(range, 0, true);
            if (hit.getType() == HitResult.Type.MISS) {
                player.sendMessage(Text.literal("No target in range."), false);
                return;
            }
            targetPos = hit.getPos();
        }

        ServerWorld world = player.getServerWorld();
        BlockPos center = BlockPos.ofFloored(targetPos);

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = 0; dy < 3; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (dx == 0 && dz == 0) continue; // air shaft in center

                    BlockPos pos = center.add(dx, dy, dz);
                    if (world.getBlockState(pos).isReplaceable()) {
                        world.setBlockState(pos, Blocks.WATER.getDefaultState());
                    }
                }
            }
        }

        player.sendMessage(Text.literal("Riptide!"), false);
    }

    @Override public void tick(ServerPlayerEntity player) { }

    @Override public int getCooldownTicks() {
        return AbilityRegistry.settings(getId()).cooldownTicks();
    }

    @Override public void deactivate(ServerPlayerEntity player) { }
}