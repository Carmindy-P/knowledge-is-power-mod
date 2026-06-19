package net.carmindy.kipmod.abilities;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class ImpalingAbility implements Abilities {

    @Override public String getId()   { return "impaling"; }
    @Override public String getName() { return "Impaling"; }
    @Override public String getDescription() { return "Falling dripstone rains on your target."; }
    @Override public boolean isOneTimeUse() { return false; }

    @Override
    public void activate(ServerPlayerEntity player) {
        if (!(player.getWorld() instanceof ServerWorld)) return;

        AbilitySettings cfg = AbilityRegistry.settings(getId());
        double range = cfg.range();
        int count = cfg.times(); // dripstones to spawn

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
            if (hit.getType() == net.minecraft.util.hit.HitResult.Type.MISS) {
                player.sendMessage(Text.literal("No target in range."), false);
                return;
            }
            targetPos = hit.getPos();
        }

        ServerWorld world = player.getServerWorld();
        BlockPos basePos = BlockPos.ofFloored(targetPos).up(8);

        for (int i = 0; i < count; i++) {
            BlockPos spawnPos = basePos.add(
                    world.random.nextInt(3) - 1,
                    i * 2,
                    world.random.nextInt(3) - 1
            );
            FallingBlockEntity dripstone = FallingBlockEntity.spawnFromBlock(
                    world, spawnPos, Blocks.POINTED_DRIPSTONE.getDefaultState());
            dripstone.dropItem = false;
        }

        player.sendMessage(Text.literal("Impaling!"), false);
    }

    @Override public void tick(ServerPlayerEntity player) { }

    @Override public int getCooldownTicks() {
        return AbilityRegistry.settings(getId()).cooldownTicks();
    }

    @Override public void deactivate(ServerPlayerEntity player) { }
}