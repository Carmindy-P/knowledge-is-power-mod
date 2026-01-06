package net.carmindy.kipmod.events;

import net.carmindy.kipmod.abilities.Abilities;
import net.carmindy.kipmod.abilities.AbilityRegistry;
import net.carmindy.kipmod.data.AbilityBookComponent;
import net.carmindy.kipmod.data.KIPModComponents;
import net.carmindy.kipmod.items.ItemStackNbtCompat;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
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

            System.out.println("=== ABILITY DEBUG START ===");
            System.out.println("Player: " + player.getName().getString());
            System.out.println("Item: " + stack.getItem());
            System.out.println("Is enchanted book: " + (stack.getItem() instanceof EnchantedBookItem));
            System.out.println("Item ID: " + stack.getItem().toString());

            // Check NBT using the compatibility approach
            NbtCompound nbt = ItemStackNbtCompat.getNbt(stack);
            System.out.println("Book has NBT: " + (nbt != null));
            if (nbt != null) {
                System.out.println("Full NBT: " + nbt);

                // Check for StoredEnchantments specifically
                if (nbt.contains("StoredEnchantments")) {
                    NbtList enchantments = nbt.getList("StoredEnchantments", 10);
                    System.out.println("Found StoredEnchantments: " + enchantments.size() + " entries");
                    for (int i = 0; i < enchantments.size(); i++) {
                        NbtCompound enchant = enchantments.getCompound(i);
                        System.out.println("Enchantment " + i + ": " + enchant);
                        if (enchant.contains("id")) {
                            System.out.println("  Enchantment ID: " + enchant.getString("id"));
                        }
                    }
                } else {
                    System.out.println("No StoredEnchantments found");
                }

                // Check for regular Enchantments too
                if (nbt.contains("Enchantments")) {
                    NbtList enchantments = nbt.getList("Enchantments", 10);
                    System.out.println("Found regular Enchantments: " + enchantments.size() + " entries");
                    for (int i = 0; i < enchantments.size(); i++) {
                        NbtCompound enchant = enchantments.getCompound(i);
                        System.out.println("Enchantment " + i + ": " + enchant);
                        if (enchant.contains("id")) {
                            System.out.println("  Enchantment ID: " + enchant.getString("id"));
                        }
                    }
                }
            }

            // Now check what AbilityBookComponent finds
            String abilityId = AbilityBookComponent.getAbility(stack);
            System.out.println("Final detected ability ID: " + abilityId);

            // Also check what's in the ability registry
            System.out.println("Registered abilities in registry:");
            // We can't easily iterate the registry, but let's check if flame exists
            Abilities flameTest = AbilityRegistry.get("flame");
            System.out.println("Flame ability in registry: " + flameTest);

            System.out.println("=== ABILITY DEBUG END ===");

            if (abilityId == null) {
                player.sendMessage(Text.literal("No registered ability for this book."), false);
                return TypedActionResult.pass(stack);
            }

            // 2. inject tag so future uses are instant
            AbilityBookComponent.setAbility(stack, abilityId);

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