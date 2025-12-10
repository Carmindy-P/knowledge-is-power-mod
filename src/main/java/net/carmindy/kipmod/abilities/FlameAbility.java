package net.carmindy.kipmod.abilities;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

/**
 * Example ability that sets an entity on fire.
 */
public class FlameAbility implements Abilities {

    @Override
    public String getId() {
        return "flame"; // Unique ID for registry
    }

    @Override
    public String getName() {
        return "Flame Burst"; // Display name
    }

    @Override
    public String getDescription() {
        return "Ignites the target you are looking at."; // Description for UI
    }

    @Override
    public void activate(ServerPlayerEntity player) {
        if (player.getWorld().isClient()) return; // Only run on server side

        // Raycast to detect target entity within 5 blocks
        var target = player.raycast(5.0, 0, false);

        if (target.getType() == HitResult.Type.ENTITY) {
            var entity = ((EntityHitResult) target).getEntity();
            entity.setOnFireFor(4); // Set entity on fire for 4 seconds
        }
    }

    @Override
    public boolean isOneTimeUse() {
        return false; // Can be used multiple times
    }

    @Override
    public int getCooldownTicks() {
        return 20 * 10; // 10 seconds cooldown (20 ticks = 1 second)
    }
}
