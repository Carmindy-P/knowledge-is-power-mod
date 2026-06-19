package net.carmindy.kipmod.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class PiercingAbility implements Abilities {

    @Override public String getId()   { return "piercing"; }
    @Override public String getName() { return "Piercing"; }
    @Override public String getDescription() { return "A perfectly aimed shot at the nearest enemy."; }
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
                new Box(start, end).expand(2.0),
                e -> !e.isSpectator() && e.canHit() && e != player,
                range * range);

        if (ehr == null) {
            player.sendMessage(Text.literal("No target in range."), false);
            return;
        }

        Entity target = ehr.getEntity();
        ServerWorld world = player.getServerWorld();

        Vec3d spawnPos = start.add(rot.multiply(1.5));
        Vec3d aimPoint = target.getPos().add(0, target.getHeight() * 0.5, 0);
        Vec3d velocity = aimPoint.subtract(spawnPos).normalize().multiply(3.5);

        ArrowEntity arrow = EntityType.ARROW.create(world);
        if (arrow == null) return;

        arrow.setOwner(player);
        arrow.setPosition(spawnPos.x, spawnPos.y, spawnPos.z);
        arrow.setVelocity(velocity.x, velocity.y, velocity.z);
        world.spawnEntity(arrow);

        player.sendMessage(Text.literal("Piercing shot!"), false);
    }

    @Override public void tick(ServerPlayerEntity player) { }

    @Override public int getCooldownTicks() {
        return AbilityRegistry.settings(getId()).cooldownTicks();
    }

    @Override public void deactivate(ServerPlayerEntity player) { }
}