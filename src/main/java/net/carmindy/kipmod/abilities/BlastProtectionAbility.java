package net.carmindy.kipmod.abilities;

import net.carmindy.kipmod.component.KIPModComponents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BlastProtectionAbility implements Abilities {

    private static final Map<UUID, Integer> charges = new ConcurrentHashMap<>();

    @Override
    public String getId() {
        return "blast_protection";
    }

    @Override
    public String getName() {
        return "Blast Protection";
    }

    @Override
    public String getDescription() {
        return "Absorbs some explosions.";
    }

    @Override
    public void activate(ServerPlayerEntity player) {
        AbilitySettings cfg = AbilityRegistry.settings(getId());
        int maxCharges = cfg.times();
        charges.put(player.getUuid(), maxCharges);
    }

    @Override
    public void tick(ServerPlayerEntity player) {

    }

    @Override
    public boolean isOneTimeUse() {
        return false;
    }

    @Override
    public int getCooldownTicks() {
        return AbilityRegistry.settings(getId()).cooldownTicks();
    }

    @Override
    public void deactivate(ServerPlayerEntity player) {
        charges.remove(player.getUuid());
    }

    public static void registerEvents() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (!(entity instanceof ServerPlayerEntity player)) return true;

            boolean isExplosion = source.isOf(DamageTypes.EXPLOSION) ||
                    source.isOf(DamageTypes.PLAYER_EXPLOSION);

            if (!isExplosion) return true;
            return handleExplosion(player);
        });

        ServerTickEvents.END_WORLD_TICK.register(world -> {
            if (world.getTime() % 5 != 0) return;

            world.getEntitiesByClass(CreeperEntity.class,
                    Box.of(world.getSpawnPos().toCenterPos(), 256, 256, 256),
                    c -> c.getFuseSpeed() > 0
            ).forEach(creeper ->
                    world.getEntitiesByClass(ServerPlayerEntity.class,
                            creeper.getBoundingBox().expand(4),
                            p -> charges.containsKey(p.getUuid())
                    ).stream().findFirst().ifPresent(player -> {
                        handleExplosion(player);
                        creeper.discard();
                    })
            );
        });
    }

    private static boolean handleExplosion(ServerPlayerEntity player) {
        UUID uuid = player.getUuid();
        int left = charges.getOrDefault(uuid, 0) - 1;
        if (left > 0) {
            charges.put(uuid, left);
            player.sendMessage(Text.literal("Explosion absorbed!  Charges left: " + left), false);
            return false;
        }

        player.sendMessage(Text.literal("Blast Protection exhausted."), false);
        charges.remove(uuid);
        KIPModComponents.ABILITIES.get(player).setAbility(null);
        return false;
    }
}