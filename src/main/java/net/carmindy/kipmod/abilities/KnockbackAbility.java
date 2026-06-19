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

public class KnockbackAbility implements Abilities {

    @Override public String getId()   { return "knockback"; }
    @Override public String getName() { return "Knockback"; }
    @Override public String getDescription() { return "Push all entities away for 2 seconds."; }
    @Override public boolean isOneTimeUse() { return false; }

    private static final Map<UUID, Integer> expiryTick = new ConcurrentHashMap<>();

    @Override
    public void activate(ServerPlayerEntity player) {
        int duration = AbilityRegistry.settings(getId()).durationTicks();
        int expire   = player.getServer().getTicks() + duration;
        expiryTick.put(player.getUuid(), expire);
        player.sendMessage(Text.literal("Repelling!"), false);
    }

    @Override
    public void tick(ServerPlayerEntity player) {
        if (player.getWorld().isClient()) return;

        int now = player.getServer().getTicks();
        if (!expiryTick.containsKey(player.getUuid())) return;

        if (now >= expiryTick.get(player.getUuid())) {
            expiryTick.remove(player.getUuid());
            return;
        }

        double radius = AbilityRegistry.settings(getId()).range();
        ServerWorld world = player.getServerWorld();
        Box box = new Box(
                player.getX() - radius, player.getY() - 2, player.getZ() - radius,
                player.getX() + radius, player.getY() + 3, player.getZ() + radius
        );

        for (Entity target : world.getOtherEntities(player, box)) {
            Vec3d push = target.getPos().subtract(player.getPos()).normalize().multiply(0.8);
            target.addVelocity(push.x, 0.3, push.z);
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