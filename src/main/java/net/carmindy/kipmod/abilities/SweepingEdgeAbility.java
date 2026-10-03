package net.carmindy.kipmod.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class SweepingEdgeAbility implements Abilities {

    @Override public String getId()   { return "sweeping_edge"; }
    @Override public String getName() { return "Sweeping Edge"; }
    @Override public String getDescription() { return "Long reach melee attack."; }
    @Override public boolean isOneTimeUse() { return false; }

    @Override
    public void activate(ServerPlayerEntity player) {
        if (player.getWorld().isClient()) return;

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

        if (ehr != null) {
            Entity target = ehr.getEntity();
            // Deal same damage as player's current held item would, or barehanded
            float damage = (float) player.getAttributeValue(net.minecraft.entity.attribute.EntityAttributes.GENERIC_ATTACK_DAMAGE);
            target.damage(player.getDamageSources().playerAttack(player), damage);
            player.sendMessage(Text.literal("Sweeping strike!"), false);
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