package net.carmindy.kipmod.abilities;

import net.carmindy.kipmod.component.KIPModComponents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class SharpnessAbility implements Abilities {

    @Override public String getId()   { return "sharpness"; }
    @Override public String getName() { return "Sharpness"; }
    @Override public String getDescription() { return "Your strikes deal double damage."; }
    @Override public boolean isOneTimeUse() { return false; }
    @Override public int getCooldownTicks() { return 0; }

    private static boolean applyingBonus = false;

    @Override public void activate(ServerPlayerEntity player) { }
    @Override public void tick(ServerPlayerEntity player) { }
    @Override public void deactivate(ServerPlayerEntity player) { }

    public static void registerEvents() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (applyingBonus) return true;
            if (!(source.getAttacker() instanceof ServerPlayerEntity attacker)) return true;

            if (!(KIPModComponents.ABILITIES.get(attacker).getAbility()
                    instanceof SharpnessAbility)) return true;

            applyingBonus = true;
            entity.damage(source, amount); // duplicate the hit
            applyingBonus = false;

            return true;
        });
    }
}