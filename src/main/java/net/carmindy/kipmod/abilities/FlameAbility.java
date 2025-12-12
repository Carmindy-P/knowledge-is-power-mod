package net.carmindy.kipmod.abilities;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
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

        var target = player.raycast(5.0, 0, false);
        if (target != null && target.getType() == HitResult.Type.ENTITY) {
            var entity = ((EntityHitResult) target).getEntity();
            entity.setOnFireFor(4);
            player.sendMessage(Text.literal("Flame Burst!"), false);
            spawnFlameParticles((ServerWorld) player.getWorld(), player.getX(), player.getY() + 1, player.getZ());
        } else {
            player.sendMessage(Text.literal("No target in range."), false);
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
