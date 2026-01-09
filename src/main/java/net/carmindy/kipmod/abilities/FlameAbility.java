package net.carmindy.kipmod.abilities;

import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.text.Text;

public class FlameAbility implements Abilities {

    @Override
    public String getId() { return "flame"; }

    @Override
    public String getName() { return "Flame Burst"; }

    @Override
    public String getDescription() {
        return "Ignites the target you are looking at (active).";
    }

    @Override
    public void activate(ServerPlayerEntity player) {
        if (!(player.getWorld() instanceof ServerWorld)) return;

        double range = 10.0;

        Vec3d start = player.getCameraPosVec(1.0F);
        Vec3d rot = player.getRotationVec(1.0F);
        Vec3d end = start.add(rot.multiply(range));

        EntityHitResult ehr = ProjectileUtil.raycast(
                player, start, end,
                new Box(start, end),
                e -> !e.isSpectator() && e.canHit() && e != player,
                range * range);

        if (ehr != null) {
            Entity target = ehr.getEntity();
            if (target instanceof ServerPlayerEntity) {
                ((ServerPlayerEntity) target).sendMessage(Text.literal("You have been ignited by Flame Burst!"), false);
            }
            target.setOnFireFor(4);
            player.sendMessage(Text.literal("Flame Burst!"), false);
            spawnFlameParticles((ServerWorld) player.getWorld(), target.getX(), target.getY() + 1, target.getZ());
        } else {
            var hit = player.raycast(range, 0, true);
            if (hit instanceof net.minecraft.util.hit.BlockHitResult bhr &&
                    hit.getType() != HitResult.Type.MISS) {

                Vec3d hitPos = new Vec3d(bhr.getBlockPos().getX() + 0.5, bhr.getBlockPos().getY() + 0.5, bhr.getBlockPos().getZ() + 0.5);
                double distanceToPlayer = start.distanceTo(hitPos);

                double verticalDistance = Math.abs(start.y - hitPos.y);

                if (distanceToPlayer > 1.5 && verticalDistance > 1.0) {
                    ServerWorld w = (ServerWorld) player.getWorld();
                    w.setBlockState(bhr.getBlockPos().offset(bhr.getSide()),
                            net.minecraft.block.Blocks.FIRE.getDefaultState());
                    player.sendMessage(Text.literal("Flame Burst!"), false);
                    spawnFlameParticles((ServerWorld) player.getWorld(),
                            player.getX(), player.getY() + 1, player.getZ());
                } else {
                    player.sendMessage(Text.literal("Target is too close or directly beneath you."), false);
                }
            } else {
                player.sendMessage(Text.literal("No target in range."), false);
            }
        }
    }

    /**
     * @param player
     */
    @Override
    public void tick(ServerPlayerEntity player) {

    }

    private void spawnFlameParticles(ServerWorld world, double x, double y, double z) {
        world.spawnParticles(ParticleTypes.FLAME, x, y, z, 4, 0.2, 0.2, 0.2, 0.01);
    }

    @Override
    public boolean isOneTimeUse() { return false; }

    @Override
    public int getCooldownTicks() { return 20 * 10; }
}
