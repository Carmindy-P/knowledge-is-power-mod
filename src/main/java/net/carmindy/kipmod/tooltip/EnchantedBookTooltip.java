package net.carmindy.kipmod.tooltip;

import net.carmindy.kipmod.abilities.AbilityRegistry;
import net.carmindy.kipmod.abilities.Abilities;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class EnchantedBookTooltip {

    public static void register() {

        ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> {

            if (!stack.isOf(Items.ENCHANTED_BOOK)) {
                return;
            }

            ItemEnchantmentsComponent stored =
                    stack.get(DataComponentTypes.STORED_ENCHANTMENTS);

            if (stored == null) {
                return;
            }

            for (RegistryEntry<Enchantment> entry : stored.getEnchantments()) {

                Identifier enchantmentId =
                        entry.getKey().get().getValue();

                String abilityId = enchantmentId.getPath();

                Abilities ability = AbilityRegistry.get(abilityId);

                if (ability == null) {
                    continue;
                }

                Text description = Text.literal(
                        ability.getDescription()
                ).formatted(Formatting.GRAY);

                /*
                 * Find the vanilla enchantment line.
                 */
                for (int i = 0; i < lines.size(); i++) {

                    String line = lines.get(i).getString();

                    if (line.toLowerCase().contains(abilityId.toLowerCase())) {

                        lines.add(i + 1, description);
                        return;
                    }
                }

                /*
                 * Fallback: add it to the bottom.
                 */
                lines.add(description);
                return;
            }
        });
    }
}