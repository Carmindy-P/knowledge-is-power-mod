package net.carmindy.kipmod.events;

import net.carmindy.kipmod.abilities.Abilities;
import net.carmindy.kipmod.abilities.AbilityRegistry;
import net.carmindy.kipmod.data.AbilityBookComponent;
import net.carmindy.kipmod.data.KIPModComponents;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.TypedActionResult;
import net.fabricmc.fabric.api.event.player.UseItemCallback;

public class BookUseHandler {

    public static void registerHandler() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            if (world.isClient) return TypedActionResult.pass(player.getStackInHand(hand));
            ItemStack stack = player.getStackInHand(hand);

            if (!(stack.getItem() instanceof EnchantedBookItem))
                return TypedActionResult.pass(stack);

            // 1. detect
            String abilityId = AbilityBookComponent.getAbility(stack);   // reads enchant list
            if (abilityId == null) {
                player.sendMessage(Text.literal("No registered ability for this book."), false);
                return TypedActionResult.pass(stack);
            }

            // 2. inject tag so future uses are instant
            AbilityBookComponent.setAbility(stack, abilityId);   // new static helper below

            // 3. give player the ability
            Abilities ability = AbilityRegistry.get(abilityId);
            if (ability != null) {
                KIPModComponents.ABILITIES.get(player).setAbility(ability);
                player.sendMessage(Text.literal("Ability learned: " + ability.getName()), false);
                return TypedActionResult.success(stack);
            }

            return TypedActionResult.pass(stack);
        });
}
}