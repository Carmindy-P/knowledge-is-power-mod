package net.carmindy.kipmod.events;

import net.carmindy.kipmod.abilities.AbilityRegistry;
import net.carmindy.kipmod.abilities.Abilities;
import net.carmindy.kipmod.data.AbilityBookComponent;
import net.carmindy.kipmod.data.AbilityComponent;
import net.carmindy.kipmod.data.KIPModComponents;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.TypedActionResult;
import net.fabricmc.fabric.api.event.player.UseItemCallback;

public class BookUseHandler {

    public static void register() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getStackInHand(hand);

            if (!(stack.getItem() instanceof EnchantedBookItem)) {
                return TypedActionResult.pass(stack);
            }

            if (world.isClient) return TypedActionResult.pass(stack);

            if (!AbilityBookComponent.hasAbility(stack)) {
                player.sendMessage(Text.literal("This book has no ability!"), true);
                return TypedActionResult.pass(stack);
            }

            String abilityId = AbilityBookComponent.getAbility(stack);
            Abilities ability = AbilityRegistry.get(abilityId);

            if (ability == null) {
                player.sendMessage(Text.literal("Unknown ability: " + abilityId), true);
                return TypedActionResult.pass(stack);
            }

            AbilityComponent comp = player.getComponent(KIPModComponents.ABILITIES);
            comp.setAbility(ability);

            stack.decrement(1);
            player.sendMessage(Text.literal("You learned: " + ability.getName()), true);
            return TypedActionResult.success(stack);
        });
    }
}
