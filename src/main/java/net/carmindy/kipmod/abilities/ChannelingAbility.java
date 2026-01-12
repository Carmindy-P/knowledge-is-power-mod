package net.carmindy.kipmod.abilities;

import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.text.Text;
import net.minecraft.entity.EntityType;

public class ChannelingAbility implements Abilities {

    @Override
    public String getId() {
        return "channeling";
    }

    @Override
    public String getName() {
        return "Lightning Strike";
    }

    @Override
    public String getDescription() {
        return "Strike the target you are looking at.";
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
                ((ServerPlayerEntity) target).sendMessage(Text.literal("You have been struck by lightning!"), false);
            }
            target.setOnFireFor(4);
            player.sendMessage(Text.literal("Lightning Strike!"), false);
            strikeLightning((ServerWorld) player.getWorld(), ehr.getPos());
        } else {
            var hit = player.raycast(range, 0, true);
            if (hit instanceof net.minecraft.util.hit.BlockHitResult bhr &&
                    hit.getType() != HitResult.Type.MISS) {
                strikeLightning((ServerWorld) player.getWorld(), bhr.getPos());
                player.sendMessage(Text.literal("Lightning Strike!"), false);
            } else {
                player.sendMessage(Text.literal("No target in range."), false);
            }
        }
    }

    @Override
    public void tick(ServerPlayerEntity player) {

    }

    private void strikeLightning(ServerWorld world, Vec3d pos) {

        Entity lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
        if (lightningEntity != null) {
            lightningEntity.setPosition(pos.x, pos.y, pos.z);
            world.spawnEntity(lightningEntity);
        }
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
    public void deactivate(ServerPlayerEntity player) {
    }
}