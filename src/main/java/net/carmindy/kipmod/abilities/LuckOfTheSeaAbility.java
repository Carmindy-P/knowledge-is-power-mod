package net.carmindy.kipmod.abilities;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LuckOfTheSeaAbility implements Abilities {

    @Override public String getId()   { return "luck_of_the_sea"; }
    @Override public String getName() { return "Luck of the Sea"; }
    @Override public String getDescription() { return "5 casts of extra luck."; }
    @Override public boolean isOneTimeUse() { return false; }

    private static final Map<UUID, Integer> charges = new ConcurrentHashMap<>();

    @Override
    public void activate(ServerPlayerEntity player) {
        if (player.getWorld().isClient()) return;

        Integer left = charges.computeIfAbsent(player.getUuid(), u -> {
            int max = AbilityRegistry.settings(getId()).times();
            return max;
        });

        if (left <= 0) {
            player.sendMessage(Text.literal("Luck of the Sea exhausted."), false);
            return;
        }

        int duration = AbilityRegistry.settings(getId()).durationTicks();
        player.addStatusEffect(new StatusEffectInstance(
                StatusEffects.LUCK,
                duration,
                1,
                false, false, false));

        charges.put(player.getUuid(), left - 1);
        player.sendMessage(Text.literal("Luck active! " + (left - 1) + " casts remaining."), false);
    }

    @Override public void tick(ServerPlayerEntity player) { }

    @Override public int getCooldownTicks() {
        return AbilityRegistry.settings(getId()).cooldownTicks();
    }

    @Override
    public void deactivate(ServerPlayerEntity player) {
        charges.remove(player.getUuid());
        player.removeStatusEffect(StatusEffects.LUCK);
    }
}