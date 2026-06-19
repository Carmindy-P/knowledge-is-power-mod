package net.carmindy.kipmod.abilities;

import net.carmindy.kipmod.component.KIPModComponents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.mob.*;
import net.minecraft.server.network.ServerPlayerEntity;

public class AquaAffinityAbility implements Abilities {

    @Override public String getId()   { return "aqua_affinity"; }
    @Override public String getName() { return "Aqua Affinity"; }
    @Override public String getDescription() { return "Sea creatures ignore you; magma is harmless underwater."; }
    @Override public boolean isOneTimeUse() { return false; }
    @Override public int getCooldownTicks() { return 0; }

    @Override public void activate(ServerPlayerEntity player) { }
    @Override public void tick(ServerPlayerEntity player) { }
    @Override public void deactivate(ServerPlayerEntity player) { }

    public static void registerEvents() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (!(entity instanceof ServerPlayerEntity player)) return true;

            if (!(KIPModComponents.ABILITIES.get(player).getAbility()
                    instanceof AquaAffinityAbility)) return true;

            // Sea creatures
            if (source.getAttacker() != null) {
                EntityType<?> type = source.getAttacker().getType();
                if (type == EntityType.GUARDIAN ||
                        type == EntityType.ELDER_GUARDIAN ||
                        type == EntityType.DROWNED ||
                        type == EntityType.DOLPHIN ||
                        type == EntityType.SQUID ||
                        type == EntityType.GLOW_SQUID ||
                        type == EntityType.PUFFERFISH ||
                        type == EntityType.TROPICAL_FISH ||
                        type == EntityType.SALMON ||
                        type == EntityType.COD ||
                        type == EntityType.TURTLE ||
                        type == EntityType.AXOLOTL) {
                    return false;
                }
            }

            // Magma underwater
            if (source.isOf(DamageTypes.HOT_FLOOR) && player.isSubmergedInWater()) {
                return false;
            }

            return true;
        });
    }
}