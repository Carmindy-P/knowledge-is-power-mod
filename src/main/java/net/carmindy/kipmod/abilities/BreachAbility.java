package net.carmindy.kipmod.abilities;

import net.carmindy.kipmod.component.KIPModComponents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BreachAbility implements Abilities {

    @Override public String getId()   { return "breach"; }
    @Override public String getName() { return "Breach"; }
    @Override public String getDescription() { return "Ignore all armor for 5 hits."; }
    @Override public boolean isOneTimeUse() { return false; }

    private static final Map<UUID, Integer> charges = new ConcurrentHashMap<>();
    private static boolean applyingBonus = false;

    @Override
    public void activate(ServerPlayerEntity player) {
        int maxCharges = AbilityRegistry.settings(getId()).times();
        charges.put(player.getUuid(), maxCharges);
        player.sendMessage(Text.literal("Breach active: " + maxCharges + " armor-piercing hits."), false);
    }

    @Override public void tick(ServerPlayerEntity player) { }

    @Override public int getCooldownTicks() {
        return AbilityRegistry.settings(getId()).cooldownTicks();
    }

    @Override public void deactivate(ServerPlayerEntity player) {
        charges.remove(player.getUuid());
    }

    public static void registerEvents() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (applyingBonus) return true;
            if (!(source.getAttacker() instanceof ServerPlayerEntity attacker)) return true;

            if (!(KIPModComponents.ABILITIES.get(attacker).getAbility()
                    instanceof BreachAbility)) return true;

            Integer left = charges.get(attacker.getUuid());
            if (left == null || left <= 0) return true;

            // Deal same damage again as magic (ignores armor)
            applyingBonus = true;
            entity.damage(attacker.getDamageSources().magic(), amount);
            applyingBonus = false;

            left--;
            if (left > 0) {
                charges.put(attacker.getUuid(), left);
                attacker.sendMessage(Text.literal("Breach hit! " + left + " remaining."), false);
            } else {
                charges.remove(attacker.getUuid());
                attacker.sendMessage(Text.literal("Breach exhausted."), false);
            }

            return true;
        });
    }
}