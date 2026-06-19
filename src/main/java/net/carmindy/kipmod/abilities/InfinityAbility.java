package net.carmindy.kipmod.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InfinityAbility implements Abilities {

    @Override public String getId()   { return "infinity"; }
    @Override public String getName() { return "Infinity"; }
    @Override public String getDescription() { return "A 10-second black hole pulls everything in."; }
    @Override public boolean isOneTimeUse() { return false; }

    private static final Map<UUID, Integer> expiryTick = new ConcurrentHashMap<>();

    @Override
    public void activate(ServerPlayerEntity player) {
        int duration = AbilityRegistry.settings(getId()).durationTicks();
        int expire = player.getServer().getTicks() + duration;
        expiryTick.put(player.getUuid(), expire);
        player.sendMessage(Text.literal("Black hole!"), false);
    }

    @Override
    public void tick(ServerPlayerEntity player) {
        if (player.getWorld().isClient()) return;

        int now = player.getServer().getTicks();
        Integer expire = expiryTick.get(player.getUuid());
        if (expire == null) return;
        if (now >= expire) {
            expiryTick.remove(player.getUuid());
            return;
        }

        double radius = AbilityRegistry.settings(getId()).range();
        ServerWorld world = player.getServerWorld();
        Box box = new Box(
                player.getX() - radius, player.getY() - radius, player.getZ() - radius,
                player.getX() + radius, player.getY() + radius, player.getZ() + radius
        );

        for (Entity target : world.getOtherEntities(player, box)) {
            Vec3d pull = player.getPos().subtract(target.getPos()).normalize().multiply(0.12);
            target.addVelocity(pull.x, pull.y * 0.2 + 0.02, pull.z);
            target.velocityModified = true;
        }
    }

    @Override public int getCooldownTicks() {
        return AbilityRegistry.settings(getId()).cooldownTicks();
    }

    @Override public void deactivate(ServerPlayerEntity player) {
        expiryTick.remove(player.getUuid());
    }
}