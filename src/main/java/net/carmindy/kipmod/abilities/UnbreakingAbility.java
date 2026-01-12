package net.carmindy.kipmod.abilities;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class UnbreakingAbility implements Abilities {
    private static final int IMMUTABILITY_DURATION_TICKS = 20 * 5;

    private static final UnbreakingAbility ABILITY_INSTANCE = new UnbreakingAbility();
    public static UnbreakingAbility instance() { return ABILITY_INSTANCE; }

    private final Map<UUID, Integer> expiryTick = new ConcurrentHashMap<>();

    @Override public String getId()   { return "unbreaking"; }
    @Override public String getName() { return "Immutability"; }

    @Override
    public String getDescription() {
        return "Makes you immutable briefly";
    }

    @Override
    public void activate(ServerPlayerEntity player) {
        int expire = player.getServer().getTicks() + IMMUTABILITY_DURATION_TICKS;
        expiryTick.put(player.getUuid(), expire);
        player.sendMessage(Text.literal("Immutability active for 5 s"), false);
    }

    @Override
    public void tick(ServerPlayerEntity player) {
        int now = player.getServer().getTicks();
        expiryTick.computeIfPresent(player.getUuid(), (u, ex) -> ex <= now ? null : ex);

        if (isImmutable(player)) {
            player.fallDistance = 0.0F;
        }
    }

    @Override
    public boolean isOneTimeUse() {
        return false;
    }

    @Override
    public int getCooldownTicks() {
        return 20*15;
    }

    public boolean isImmutable(ServerPlayerEntity p) {
        return expiryTick.containsKey(p.getUuid());
    }

    public static void registerEvents() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (entity instanceof ServerPlayerEntity player) {
                return !ABILITY_INSTANCE.isImmutable(player);
            }
            return true;
        });
    }

    @Override
    public void deactivate(ServerPlayerEntity player) {
        expiryTick.remove(player.getUuid());
    }
}