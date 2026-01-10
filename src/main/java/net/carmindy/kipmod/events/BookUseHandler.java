package net.carmindy.kipmod.events;

import net.carmindy.kipmod.abilities.Abilities;
import net.carmindy.kipmod.abilities.AbilityRegistry;
import net.carmindy.kipmod.config.KIPModAutoConfig;
import net.carmindy.kipmod.data.AbilityBookComponent;
import net.carmindy.kipmod.data.AbilityComponent;
import net.carmindy.kipmod.data.KIPModComponents;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.fabricmc.fabric.api.event.player.UseItemCallback;

public class BookUseHandler {

    public static void registerHandler() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            if (world.isClient) return TypedActionResult.pass(player.getStackInHand(hand));
            ItemStack stack = player.getStackInHand(hand);

            if (!(stack.getItem() instanceof EnchantedBookItem))
                return TypedActionResult.pass(stack);

            String abilityId = AbilityBookComponent.getAbility(stack);
            if (abilityId == null) {
                player.sendMessage(Text.literal("No registered ability for this book."), false);
                return TypedActionResult.pass(stack);
            }

            AbilityComponent abilityComponent = KIPModComponents.ABILITIES.get(player);

            switch (abilityId) {
                case "flame" -> {
                    if (!KIPModAutoConfig.CONFIG.enableFlame) return TypedActionResult.pass(stack);
                }
                case "efficiency" -> {
                    if (!KIPModAutoConfig.CONFIG.enableEfficiency) return TypedActionResult.pass(stack);
                }
                case "channeling" -> {
                    if (!KIPModAutoConfig.CONFIG.enableChanneling) return TypedActionResult.pass(stack);
                }
                case "feather_falling" -> {
                    if (!KIPModAutoConfig.CONFIG.enableFeatherFall) return TypedActionResult.pass(stack);
                }
                case "mending" -> {
                    if (!KIPModAutoConfig.CONFIG.enableMending) return TypedActionResult.pass(stack);
                }
            }

            AbilityBookComponent.setAbility(stack, abilityId);

            Abilities ability = AbilityRegistry.get(abilityId);
            if (ability != null) {
                abilityComponent.setAbility(ability);
                player.sendMessage(Text.literal("Ability learned: " + ability.getName()), false);

                if (!player.isCreative() && !player.isSpectator()) {
                    stack.setCount(0);
                }

                return TypedActionResult.success(stack);
            }

            return TypedActionResult.pass(stack);
        });
    }
}