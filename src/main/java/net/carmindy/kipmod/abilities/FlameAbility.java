package net.carmindy.kipmod.abilities;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;

public class FlameAbility implements Abilities {

    @Override
    public String getId() {
        return "";
    }

    @Override
    public String getName() {
        return "Flame Burst";
    }

    @Override
    public String getDescription() {
        return "Ignites the target you are looking at.";
    }

    @Override
    public void activate(ServerPlayerEntity player) {
        if (player.getWorld().isClient()) return;

        var target = player.raycast(5.0, 0, false);

        if (target.getType() == net.minecraft.util.hit.HitResult.Type.ENTITY) {
            var entity = ((net.minecraft.util.hit.EntityHitResult)target).getEntity();
            entity.setOnFireFor(4);
        }
    }

    @Override
    public boolean isOneTimeUse() {
        return false;
    }

    @Override
    public int getCooldownTicks() {
        return 20 * 10; // 10 seconds
    }
}
