package net.carmindy.kipmod.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;

public class BaneOfArthropodsAbility implements Abilities {

    @Override public String getId()   { return "bane_of_arthropods"; }
    @Override public String getName() { return "Bane of Arthropods"; }
    @Override public String getDescription() { return "Arthropods flee your presence."; }
    @Override public boolean isOneTimeUse() { return false; }
    @Override public int getCooldownTicks() { return 0; }

    @Override public void activate(ServerPlayerEntity player) { }
    @Override public void deactivate(ServerPlayerEntity player) { }

    @Override
    public void tick(ServerPlayerEntity player) {
        if (player.getWorld().isClient()) return;

        int radius = AbilityRegistry.settings(getId()).radius();
        ServerWorld world = player.getServerWorld();
        Box box = new Box(
                player.getX() - radius, player.getY() - 2, player.getZ() - radius,
                player.getX() + radius, player.getY() + 3, player.getZ() + radius
        );

        for (Entity entity : world.getOtherEntities(player, box)) {
            if (!isArthropod(entity)) continue;
            if (!(entity instanceof LivingEntity living)) continue;

            living.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.SLOWNESS, 40, 2, false, false, false));
            living.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.WEAKNESS, 40, 1, false, false, false));
        }
    }

    private boolean isArthropod(Entity entity) {
        EntityType<?> type = entity.getType();
        return type == EntityType.SPIDER ||
                type == EntityType.CAVE_SPIDER ||
                type == EntityType.SILVERFISH ||
                type == EntityType.ENDERMITE;
    }
}