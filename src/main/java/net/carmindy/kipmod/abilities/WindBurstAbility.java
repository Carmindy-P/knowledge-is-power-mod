package net.carmindy.kipmod.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class WindBurstAbility implements Abilities {

    @Override public String getId()   { return "wind_burst"; }
    @Override public String getName() { return "Wind Burst"; }
    @Override public String getDescription() { return "Launch any enemy high into the sky."; }
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
                new Box(start, end),
                e -> !e.isSpectator() && e.canHit() && e != player,
                range * range);

        if (ehr != null) {
            Entity target = ehr.getEntity();
            target.addVelocity(0, 2.5, 0);
            target.velocityModified = true;
            player.sendMessage(Text.literal("Wind Burst!"), false);
        } else {
            player.sendMessage(Text.literal("No target in range."), false);
        }
    }

    @Override public void tick(ServerPlayerEntity player) { }

    @Override public int getCooldownTicks() {
        return AbilityRegistry.settings(getId()).cooldownTicks();
    }

    @Override public void deactivate(ServerPlayerEntity player) { }
}