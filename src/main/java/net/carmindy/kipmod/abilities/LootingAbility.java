package net.carmindy.kipmod.abilities;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LootingAbility implements Abilities {

    @Override public String getId()   { return "looting"; }
    @Override public String getName() { return "Looting"; }
    @Override public String getDescription() { return "10 seconds of extra rare mob drops."; }
    @Override public boolean isOneTimeUse() { return false; }

    private static final Map<UUID, Integer> expiryTick = new ConcurrentHashMap<>();

    private static final Item[] RARE_DROPS = {
            Items.DIAMOND,
            Items.EMERALD,
            Items.GOLD_INGOT,
            Items.IRON_INGOT,
            Items.ENCHANTED_GOLDEN_APPLE,
            Items.NETHERITE_SCRAP,
            Items.TOTEM_OF_UNDYING
    };

    @Override
    public void activate(ServerPlayerEntity player) {
        int duration = AbilityRegistry.settings(getId()).durationTicks();
        int expire = player.getServer().getTicks() + duration;
        expiryTick.put(player.getUuid(), expire);
        player.sendMessage(Text.literal("Looting active!"), false);
    }

    @Override
    public void tick(ServerPlayerEntity player) {
        if (player.getWorld().isClient()) return;
        int now = player.getServer().getTicks();
        expiryTick.computeIfPresent(player.getUuid(), (u, ex) -> ex <= now ? null : ex);
    }

    @Override public int getCooldownTicks() {
        return AbilityRegistry.settings(getId()).cooldownTicks();
    }

    @Override public void deactivate(ServerPlayerEntity player) {
        expiryTick.remove(player.getUuid());
    }

    public static void registerEvents() {
        ServerLivingEntityEvents.ALLOW_DEATH.register((entity, source, damageSource) -> {
            if (!(source.getAttacker() instanceof ServerPlayerEntity attacker)) return true;
            if (!expiryTick.containsKey(attacker.getUuid())) return true;

            ServerWorld world = attacker.getServerWorld();
            var pos = entity.getPos();
            var drop = RARE_DROPS[world.random.nextInt(RARE_DROPS.length)];

            world.spawnEntity(new ItemEntity(world, pos.x, pos.y, pos.z, new ItemStack(drop)));
            return true;
        });
    }
}