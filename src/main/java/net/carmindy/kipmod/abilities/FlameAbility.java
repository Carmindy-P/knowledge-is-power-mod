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
        return "Ignites the target you are looking at (active). Passive: small flame particles.";
    }

    @Override
    public void activate(ServerPlayerEntity player) {
        if (player.getWorld().isClient()) return;

        Vec3d start = player.getCameraPosVec(1.0F);
        Vec3d rot   = player.getRotationVec(1.0F);
        Vec3d end   = start.add(rot.multiply(4.5));

        EntityHitResult ehr = ProjectileUtil.raycast(
                player, start, end,
                new Box(start, end),
                e -> !e.isSpectator() && e.canHit(),
                4.5 * 4.5);

        if (ehr != null) {
            ehr.getEntity().setOnFireFor(4);
            player.sendMessage(Text.literal("Flame Burst!"), false);
            spawnFlameParticles((ServerWorld) player.getWorld(), player.getX(), player.getY() + 1, player.getZ());
        } else {                                 // ---------- try block ----------
            var hit = player.raycast(4.5, 0, true);        // HitResult
            if (hit instanceof net.minecraft.util.hit.BlockHitResult bhr &&
                    hit.getType() != HitResult.Type.MISS) {

                ServerWorld w = (ServerWorld) player.getWorld();
                w.setBlockState(bhr.getBlockPos().offset(bhr.getSide()),
                        net.minecraft.block.Blocks.FIRE.getDefaultState());
                player.sendMessage(Text.literal("Flame Burst!"), false);
                spawnFlameParticles((ServerWorld) player.getWorld(),
                        player.getX(), player.getY() + 1, player.getZ());
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
    public int getCooldownTicks() { return 20 * 10; } // 10 seconds
}
