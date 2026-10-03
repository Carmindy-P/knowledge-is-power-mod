package net.carmindy.kipmod.abilities;

import net.carmindy.kipmod.component.KIPModComponents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class FortuneAbility implements Abilities {

    @Override public String getId()   { return "fortune"; }
    @Override public String getName() { return "Fortune"; }
    @Override public String getDescription() { return "Everything has a chance to drop twice."; }
    @Override public boolean isOneTimeUse() { return false; }
    @Override public int getCooldownTicks() { return 0; }

    private static final Random RANDOM = new Random();

    @Override public void activate(ServerPlayerEntity player) { }
    @Override public void tick(ServerPlayerEntity player) { }
    @Override public void deactivate(ServerPlayerEntity player) { }

    public static void registerEvents() {
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            if (world.isClient) return;
            if (!(player instanceof ServerPlayerEntity serverPlayer)) return;

            if (!(KIPModComponents.ABILITIES.get(serverPlayer).getAbility()
                    instanceof FortuneAbility)) return;

            double chance = AbilityRegistry.settings("fortune").heartsPerOrb();
            if (RANDOM.nextDouble() >= chance) return;

            var item = state.getBlock().asItem();
            if (item == null) return;

            world.spawnEntity(new ItemEntity(
                    world,
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    new ItemStack(item)
            ));
        });
    }
}