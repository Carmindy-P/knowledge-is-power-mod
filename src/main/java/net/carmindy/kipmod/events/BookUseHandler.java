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

            if (world.isClient()) {
                return TypedActionResult.pass(player.getStackInHand(hand));
            }

            ItemStack stack = player.getStackInHand(hand);

            if (!(stack.getItem() instanceof EnchantedBookItem)) {
                return TypedActionResult.pass(stack);
            }

            String abilityId = AbilityBookComponent.getAbility(stack);

            if (abilityId == null) {
                player.sendMessage(
                        Text.literal("No registered ability for this book."),
                        false
                );
                return TypedActionResult.pass(stack);
            }

            Abilities ability = AbilityRegistry.get(abilityId);

            if (ability == null) {
                player.sendMessage(
                        Text.literal("Ability not found: " + abilityId),
                        false
                );
                return TypedActionResult.pass(stack);
            }

            var component = KIPModComponents.ABILITIES.maybeGet(player);

            if (component.isEmpty()) {
                System.out.println(
                        "[KIPMod] ERROR: Player has no abilities component!"
                );

                player.sendMessage(
                        Text.literal("ERROR: Player abilities component is missing."),
                        false
                );

                return TypedActionResult.pass(stack);
            }

            component.get().setAbility(ability);

            System.out.println(
                    "[KIPMod] Learned ability: " + ability.getId()
            );

            player.sendMessage(
                    Text.literal("Ability learned: " + ability.getName()),
                    false
            );

            if (!player.isCreative() && !player.isSpectator()) {
                stack.decrement(1);
            }

            return TypedActionResult.success(stack);
        });
    }
}