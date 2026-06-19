package net.carmindy.kipmod.abilities;

import net.carmindy.kipmod.component.KIPModComponents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.server.network.ServerPlayerEntity;

public class ProjectileProtectionAbility implements Abilities {

    @Override public String getId()   { return "projectile_protection"; }
    @Override public String getName() { return "Projectile Protection"; }
    @Override public String getDescription() { return "Projectiles cannot hit you."; }
    @Override public boolean isOneTimeUse() { return false; }
    @Override public int getCooldownTicks() { return 0; }

    @Override public void activate(ServerPlayerEntity player) { }
    @Override public void tick(ServerPlayerEntity player) { }
    @Override public void deactivate(ServerPlayerEntity player) { }

    public static void registerEvents() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (!(entity instanceof ServerPlayerEntity player)) return true;

            if (!(KIPModComponents.ABILITIES.get(player).getAbility()
                    instanceof ProjectileProtectionAbility)) return true;

            // Cancel common projectile damage types
            if (source.isOf(DamageTypes.ARROW) ||
                    source.isOf(DamageTypes.TRIDENT) ||
                    source.isOf(DamageTypes.MOB_PROJECTILE) ||
                    source.isOf(DamageTypes.FIREBALL) ||
                    source.isOf(DamageTypes.WITHER_SKULL)) {
                return false; // absorb the hit
            }

            return true;
        });
    }
}