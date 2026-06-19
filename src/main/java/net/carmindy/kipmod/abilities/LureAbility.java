package net.carmindy.kipmod.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class LureAbility implements Abilities {

    @Override public String getId()   { return "lure"; }
    @Override public String getName() { return "Lure"; }
    @Override public String getDescription() { return "All passive mobs are attracted to you."; }
    @Override public boolean isOneTimeUse() { return false; }
    @Override public int getCooldownTicks() { return 0; }

    @Override public void activate(ServerPlayerEntity player) { }
    @Override public void deactivate(ServerPlayerEntity player) { }

    @Override
    public void tick(ServerPlayerEntity player) {
        if (player.getWorld().isClient()) return;

        int radius = AbilityRegistry.settings(getId()).radius();
        ServerWorld world = player.getServerWorld();
        Box box = new Box(
                player.getX() - radius, player.getY() - radius, player.getZ() - radius,
                player.getX() + radius, player.getY() + radius, player.getZ() + radius
        );

        for (Entity entity : world.getOtherEntities(player, box)) {
            if (!(entity instanceof PassiveEntity) && !(entity instanceof AnimalEntity)) continue;

            Vec3d pull = player.getPos().subtract(entity.getPos()).normalize().multiply(0.06);
            entity.addVelocity(pull.x, 0.02, pull.z);
            entity.velocityModified = true;
        }
    }
}