package net.carmindy.kipmod.events;

import net.carmindy.kipmod.abilities.Abilities;
import net.carmindy.kipmod.data.KIPModComponents;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.TypedActionResult;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import org.ladysnake.cca.api.v3.item.ItemComponentInitializer;
import org.ladysnake.cca.api.v3.item.ItemComponentMigrationRegistry;


/**
 * Handles the event when a player uses an item.
 * Specifically handles learning abilities from enchanted books.
 */
public class BookUseHandler implements ItemComponentInitializer {

    public static void registerHandler() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            if (world.isClient) return TypedActionResult.pass(player.getStackInHand(hand));

            ItemStack stack = player.getStackInHand(hand);

            if (stack.getItem() instanceof EnchantedBookItem) {
                Abilities ability = net.carmindy.kipmod.items.AbilityBookFactory.getAbilityFromBook(stack);

                // Debug logging
                System.out.println("[KIPMod] Player " + player.getName().getString() + " used book: " + stack);
                if (ability != null) {
                    System.out.println("[KIPMod] Detected ability: " + ability.getId());
                } else {
                    System.out.println("[KIPMod] No ability detected on this book.");
                }

                if (ability != null) {
                    // Apply ability to player
                    var playerAbilities = KIPModComponents.ABILITIES.get(player);
                    playerAbilities.setAbility(ability);

                    player.sendMessage(Text.literal("You learned: " + ability.getName()), true);
                    stack.decrement(1);

                    return TypedActionResult.success(stack);
                } else {
                    player.sendMessage(Text.literal("This book has no power yet."), true);
                    return TypedActionResult.pass(stack);
                }
            }

            return TypedActionResult.pass(stack);
        });
    }

    @Override
    public void registerItemComponentMigrations(ItemComponentMigrationRegistry itemComponentMigrationRegistry) {
        // No migrations needed
    }
}
