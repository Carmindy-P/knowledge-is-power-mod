package net.carmindy.kipmod.abilities;

import net.carmindy.kipmod.component.KIPModComponents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.server.network.ServerPlayerEntity;

public class MultishotAbility implements Abilities {

    @Override public String getId()   { return "multishot"; }
    @Override public String getName() { return "Multishot"; }
    @Override public String getDescription() { return "All attacks happen twice."; }
    @Override public boolean isOneTimeUse() { return false; }
    @Override public int getCooldownTicks() { return 0; }

    @Override public void activate(ServerPlayerEntity player) { }
    @Override public void tick(ServerPlayerEntity player) { }
    @Override public void deactivate(ServerPlayerEntity player) { }

    private static final ThreadLocal<Boolean> APPLYING = ThreadLocal.withInitial(() -> false);

    public static void registerEvents() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (APPLYING.get()) return true;
            if (!(source.getAttacker() instanceof ServerPlayerEntity attacker)) return true;

            if (!(KIPModComponents.ABILITIES.get(attacker).getAbility()
                    instanceof MultishotAbility)) return true;

            APPLYING.set(true);
            entity.damage(source, amount);
            APPLYING.set(false);

            return true;
        });
    }
}