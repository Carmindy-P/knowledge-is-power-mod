package net.carmindy.kipmod.abilities;

import net.carmindy.kipmod.component.KIPModComponents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SmiteAbility implements Abilities {

    @Override public String getId()   { return "smite"; }
    @Override public String getName() { return "Smite"; }
    @Override public String getDescription() { return "3 seconds of flame shooting."; }
    @Override public boolean isOneTimeUse() { return false; }

    private static final int DURATION = 60; // 3 seconds
    private static final Map<UUID, Integer> remainingTicks = new ConcurrentHashMap<>();

    @Override
    public void activate(ServerPlayerEntity player) {
        if (player.getWorld().isClient()) return;
        remainingTicks.put(player.getUuid(), DURATION);
        player.sendMessage(Text.literal("Flame Shooter active!"), false);
    }

    @Override
    public void tick(ServerPlayerEntity player) {
        if (player.getWorld().isClient()) return;

        Integer ticks = remainingTicks.get(player.getUuid());
        if (ticks == null || ticks <= 0) return;

        // Auto-fire every 4 ticks
        if (ticks % 4 == 0) {
            shootFireball(player);
        }

        remainingTicks.put(player.getUuid(), ticks - 1);
    }

    private void shootFireball(ServerPlayerEntity player) {
        ServerWorld world = player.getServerWorld();
        Vec3d start = player.getCameraPosVec(1.0F);
        Vec3d rot = player.getRotationVec(1.0F);

        // Spawn slightly in front so it doesn't hit the player
        Vec3d spawnPos = start.add(rot.multiply(1.5));

        SmallFireballEntity fireball = EntityType.SMALL_FIREBALL.create(world);
        if (fireball == null) return;

        fireball.setOwner(player);
        fireball.setPosition(spawnPos.x, spawnPos.y, spawnPos.z);
        fireball.setVelocity(rot.x, rot.y, rot.z, 1.5f, 0);
        world.spawnEntity(fireball);
    }

    @Override public int getCooldownTicks() {
        return AbilityRegistry.settings(getId()).cooldownTicks();
    }

    @Override public void deactivate(ServerPlayerEntity player) {
        remainingTicks.remove(player.getUuid());
    }
}