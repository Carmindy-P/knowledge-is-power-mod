package net.carmindy.kipmod.abilities;

import net.carmindy.kipmod.component.KIPModComponents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.server.network.ServerPlayerEntity;

public class PunchAbility implements Abilities {

    @Override public String getId()   { return "punch"; }
    @Override public String getName() { return "Punch"; }
    @Override public String getDescription() { return "Barehanded attacks hurt more."; }
    @Override public boolean isOneTimeUse() { return false; }
    @Override public int getCooldownTicks() { return 0; }

    @Override public void activate(ServerPlayerEntity player) { }
    @Override public void tick(ServerPlayerEntity player) { }
    @Override public void deactivate(ServerPlayerEntity player) { }

    private static boolean applyingBonus = false;

    public static void registerEvents() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (applyingBonus) return true;
            if (!(source.getAttacker() instanceof ServerPlayerEntity attacker)) return true;

            if (!(KIPModComponents.ABILITIES.get(attacker).getAbility()
                    instanceof PunchAbility)) return true;

            // Only when main hand is empty
            if (!attacker.getMainHandStack().isEmpty()) return true;

            applyingBonus = true;
            entity.damage(source, amount * 1.5f); // 2.5x total (original + 1.5x bonus)
            applyingBonus = false;

            return true;
        });
    }
}