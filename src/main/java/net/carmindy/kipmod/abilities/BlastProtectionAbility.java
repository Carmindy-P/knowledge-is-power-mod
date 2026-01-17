package net.carmindy.kipmod.abilities;

import net.carmindy.kipmod.component.KIPModComponents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;

public class BlastProtectionAbility implements Abilities {

    @Override
    public String getId() { return "blast_protection"; }

    @Override
    public String getName() { return "Blast Protection"; }

    @Override
    public String getDescription() { return "Absorbs some explosions."; }

    @Override
    public void activate(ServerPlayerEntity player) {
        int maxCharges = AbilityRegistry.settings(getId()).times();
        KIPModComponents.ABILITIES.get(player).setCharges(maxCharges);   // <- NEW
    }

    @Override
    public void tick(ServerPlayerEntity player) { }

    @Override
    public boolean isOneTimeUse() { return false; }

    @Override
    public int getCooldownTicks() {
        return AbilityRegistry.settings(getId()).cooldownTicks();
    }

    @Override
    public void deactivate(ServerPlayerEntity player) {
        KIPModComponents.ABILITIES.get(player).setCharges(0);   // <- NEW
    }

    public static void registerEvents() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (!(entity instanceof ServerPlayerEntity player)) return true;
            if (!source.isOf(DamageTypes.EXPLOSION) &&
                    !source.isOf(DamageTypes.PLAYER_EXPLOSION)) return true;

            /* only protect if blast-protection is the CURRENT ability */
            if (!(KIPModComponents.ABILITIES.get(player).getAbility()
                    instanceof BlastProtectionAbility)) return true;

            return handleExplosion(player);
        });

        ServerTickEvents.END_WORLD_TICK.register(world -> {
            if (world.getTime() % 5 != 0) return;
            world.getEntitiesByClass(CreeperEntity.class,
                    Box.of(world.getSpawnPos().toCenterPos(), 256, 256, 256),
                    c -> c.getFuseSpeed() > 0
            ).forEach(creeper ->
                    world.getEntitiesByClass(ServerPlayerEntity.class,
                                    creeper.getBoundingBox().expand(4),
                                    p -> KIPModComponents.ABILITIES.get(p).getAbility()
                                            instanceof BlastProtectionAbility)
                            .stream().findFirst().ifPresent(player -> {
                                handleExplosion(player);
                                creeper.discard();
                            })
            );
        });
    }

    private static boolean handleExplosion(ServerPlayerEntity player) {
        var comp = KIPModComponents.ABILITIES.get(player);
        int left = comp.getCharges() - 1;
        if (left > 0) {
            comp.setCharges(left);
            player.sendMessage(Text.literal("Explosion absorbed! Charges left: " + left), false);
            return false;
        }
        player.sendMessage(Text.literal("Blast Protection exhausted."), false);
        comp.setAbility(null);
        return false;
    }
}