package net.carmindy.kipmod.abilities;

import net.carmindy.kipmod.component.KIPModComponents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LoyaltyAbility implements Abilities {

    @Override public String getId()   { return "loyalty"; }
    @Override public String getName() { return "Loyalty"; }
    @Override public String getDescription() { return "Choose one player, they cannot harm you."; }
    @Override public boolean isOneTimeUse() { return false; }
    @Override public int getCooldownTicks() { return 0; }

    private static final Map<UUID, UUID> bonded = new ConcurrentHashMap<>();

    @Override public void activate(ServerPlayerEntity player) { }
    @Override public void tick(ServerPlayerEntity player) { }
    @Override public void deactivate(ServerPlayerEntity player) {
        bonded.remove(player.getUuid());
    }

    public static void setBond(UUID protector, UUID protectedPlayer) {
        bonded.put(protectedPlayer, protector);
    }

    public static UUID getBond(UUID player) {
        return bonded.get(player);
    }

    public static void registerEvents() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (!(entity instanceof ServerPlayerEntity victim)) return true;
            if (!(source.getAttacker() instanceof ServerPlayerEntity attacker)) return true;

            UUID protector = bonded.get(victim.getUuid());
            if (protector == null) return true;

            if (attacker.getUuid().equals(protector)) {
                attacker.sendMessage(Text.literal("You cannot harm your loyal ally!"), false);
                return false;
            }

            return true;
        });
    }
}