package net.carmindy.kipmod.events;

import net.carmindy.kipmod.abilities.Abilities;
import net.carmindy.kipmod.abilities.AbilityRegistry;
import net.carmindy.kipmod.component.AbilityBookComponent;
import net.carmindy.kipmod.component.KIPModComponents;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.TypedActionResult;
import net.fabricmc.fabric.api.event.player.UseItemCallback;

public class BookUseHandler {

    public static void registerHandler() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            if (world.isClient()) return TypedActionResult.pass(player.getStackInHand(hand));
            ItemStack stack = player.getStackInHand(hand);

            if (!(stack.getItem() instanceof EnchantedBookItem))
                return TypedActionResult.pass(stack);

            String abilityId = AbilityBookComponent.getAbility(stack);
            if (abilityId == null) {
                player.sendMessage(Text.literal("No registered ability for this book."), false);
                return TypedActionResult.pass(stack);
            }

            Abilities ability = AbilityRegistry.get(abilityId);
            if (ability != null) {
                KIPModComponents.ABILITIES.get(player).setAbility(ability);
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