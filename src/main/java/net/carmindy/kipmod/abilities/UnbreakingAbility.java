package net.carmindy.kipmod.abilities;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class UnbreakingAbility implements Abilities {

    private static final UnbreakingAbility INSTANCE = new UnbreakingAbility();
    public static UnbreakingAbility instance() { return INSTANCE; }

    private static final Map<UUID, Integer> expiryTick = new ConcurrentHashMap<>();

    @Override public String getId()   { return "unbreaking"; }
    @Override public String getName() { return "Immutability"; }
    @Override public String getDescription() { return "Makes you immutable briefly"; }
    @Override public boolean isOneTimeUse() { return false; }

    @Override
    public void activate(ServerPlayerEntity player) {
        int duration = AbilityRegistry.settings(getId()).durationTicks();
        int expire   = player.getServer().getTicks() + duration;
        expiryTick.put(player.getUuid(), expire);
        player.sendMessage(Text.literal("Immutability active for " + duration / 20 + " s"), false);
    }

    @Override
    public void tick(ServerPlayerEntity player) {
        int now = player.getServer().getTicks();
        expiryTick.computeIfPresent(player.getUuid(), (u, ex) -> ex <= now ? null : ex);
        if (isImmutable(player)) player.fallDistance = 0.0F; // no fall damage build-up
    }

    @Override
    public int getCooldownTicks() {
        return AbilityRegistry.settings(getId()).cooldownTicks();
    }

    @Override
    public void deactivate(ServerPlayerEntity player) {
        expiryTick.remove(player.getUuid());
    }

    public static boolean isImmutable(ServerPlayerEntity p) {
        return expiryTick.containsKey(p.getUuid());
    }

    public static void registerEvents() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (entity instanceof ServerPlayerEntity p && isImmutable(p)) {
                return false;
            }
            return true;
        });
    }
}