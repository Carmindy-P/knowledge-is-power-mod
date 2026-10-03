package net.carmindy.kipmod.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;

public class FireAspectAbility implements Abilities {

    @Override public String getId()   { return "fire_aspect"; }
    @Override public String getName() { return "Fire Aspect"; }
    @Override public String getDescription() { return "Everything around you burns."; }
    @Override public boolean isOneTimeUse() { return false; }

    @Override
    public void activate(ServerPlayerEntity player) {
        if (player.getWorld().isClient()) return;

        AbilitySettings cfg = AbilityRegistry.settings(getId());
        double radius = cfg.range();
        int fireSec   = cfg.fireSeconds();

        ServerWorld world = player.getServerWorld();
        Box box = new Box(
                player.getX() - radius, player.getY() - 2, player.getZ() - radius,
                player.getX() + radius, player.getY() + 3, player.getZ() + radius
        );

        for (Entity target : world.getOtherEntities(player, box)) {
            target.setOnFireFor(fireSec);
        }

        player.sendMessage(Text.literal("Inferno!"), false);
    }

    @Override public void tick(ServerPlayerEntity player) { }

    @Override public int getCooldownTicks() {
        return AbilityRegistry.settings(getId()).cooldownTicks();
    }

    @Override public void deactivate(ServerPlayerEntity player) { }
}