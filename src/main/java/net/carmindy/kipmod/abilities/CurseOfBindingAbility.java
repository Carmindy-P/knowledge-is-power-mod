package net.carmindy.kipmod.abilities;

import net.carmindy.kipmod.component.KIPModComponents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public class CurseOfBindingAbility implements Abilities {

    @Override public String getId()   { return "binding_curse"; }
    @Override public String getName() { return "Curse of Binding"; }
    @Override public String getDescription() { return "Your pain is shared with the nearest player."; }
    @Override public boolean isOneTimeUse() { return false; }
    @Override public int getCooldownTicks() { return 0; }

    @Override public void activate(ServerPlayerEntity player) { }
    @Override public void tick(ServerPlayerEntity player) { }
    @Override public void deactivate(ServerPlayerEntity player) { }

    public static void registerEvents() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (!(entity instanceof ServerPlayerEntity victim)) return true;

            if (!(KIPModComponents.ABILITIES.get(victim).getAbility()
                    instanceof CurseOfBindingAbility)) return true;

            ServerWorld world = victim.getServerWorld();
            ServerPlayerEntity nearest = null;
            double closest = Double.MAX_VALUE;

            for (ServerPlayerEntity other : world.getPlayers()) {
                if (other == victim) continue;
                double dist = victim.squaredDistanceTo(other);
                if (dist < closest && dist < 256) { // 16 blocks
                    closest = dist;
                    nearest = other;
                }
            }

            if (nearest != null) {
                float shared = amount * 0.5f;
                nearest.damage(source, shared);
                victim.sendMessage(Text.literal("Shared " + shared + " damage with " + nearest.getName().getString()), false);
            }

            return true;
        });
    }
}