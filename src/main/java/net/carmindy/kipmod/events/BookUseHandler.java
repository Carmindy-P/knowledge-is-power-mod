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
            if (world.isClient()) return TypedActionResult.pass(player.getStackInHand(hand));

            ItemStack stack = player.getStackInHand(hand);

            // Only allow enchanted books
            if (!(stack.getItem() instanceof EnchantedBookItem)) {
                return TypedActionResult.pass(stack);
            }

            // Fetch ability directly from the ItemStack NBT
            String abilityId = AbilityBookComponent.getAbility(stack);

            if (abilityId != null) {
                Abilities ability = AbilityRegistry.get(abilityId);
                if (ability != null) {
                    KIPModComponents.ABILITIES.get(player).setAbility(ability);
                    player.sendMessage(Text.literal("Ability applied: " + ability.getId()), false);
                    return TypedActionResult.success(stack);
                }
            }

            player.sendMessage(Text.literal("No registered ability for this book."), false);
            return TypedActionResult.pass(stack);
        });
    }
}
