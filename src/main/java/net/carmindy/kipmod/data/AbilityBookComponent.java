package net.carmindy.kipmod.data;

import net.carmindy.kipmod.abilities.AbilityRegistry;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import org.jetbrains.annotations.Nullable;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;

import java.util.Map;

public class AbilityBookComponent {

    private static final String NBT_KEY = "Ability";
    public static final String STORED_ENCHANTMENTS_KEY = "StoredEnchantments";

    private static final Map<String, String> ENCHANT_TO_ABILITY = Map.of(
            "minecraft:flame", "flame",
            "minecraft:efficiency", "efficiency",
            "minecraft:feather_falling", "feather_falling",
            "minecraft:channeling", "channeling",
            "minecraft:mending", "mending"
    );

    private Enchantment ability;

    public void setAbility(Enchantment ench) {
        this.ability = ench;
    }

    public Enchantment getAbility() {
        return this.ability;
    }

    public static boolean hasAbility(ItemStack stack) {
        NbtCompound tag = readNbt(stack);
        if (tag != null && tag.contains(NBT_KEY)) return true;
        return getAbilityFromEnchants(stack) != null;
    }


    @Nullable
    public static String getAbility(ItemStack stack) {
        NbtCompound tag = readNbt(stack);
        if (tag != null && tag.contains(NBT_KEY)) return tag.getString(NBT_KEY);
        return getAbilityFromEnchants(stack);
    }

    public static void removeAbility(ItemStack stack) {
        NbtCompound tag = readNbt(stack);
        if (tag == null) return;
        tag.remove(NBT_KEY);
        writeNbt(stack, tag.isEmpty() ? null : tag);
    }

    @Nullable
    private static String getAbilityFromEnchants(ItemStack stack) {
        ItemEnchantmentsComponent comp = stack.get(DataComponentTypes.STORED_ENCHANTMENTS);
        if (comp != null && !comp.isEmpty()) {
            for (var entry : comp.getEnchantmentEntries()) {
                String id = entry.getKey().getKey().get().getValue().toString();
                String mapped = ENCHANT_TO_ABILITY.get(id);
                if (mapped != null && AbilityRegistry.get(mapped) != null) return mapped;
                if (AbilityRegistry.get(id)      != null) return id;
            }
        }
        NbtCompound tag = readNbt(stack);
        if (tag == null) return null;

        String ability = checkEnchantList(tag.getList("StoredEnchantments", 10));
        if (ability != null) return ability;

        if (tag.contains("Enchantments")) {
            ability = checkEnchantList(tag.getList("Enchantments", 10));
            if (ability != null) return ability;
        }

        // Check custom component format
        ComponentMap components = stack.getComponents();
        NbtCompound storedEnchantments = (NbtCompound) components.getOrDefault(DataComponentTypes.CUSTOM_DATA, new NbtCompound());
        if (storedEnchantments.contains("minecraft:stored_enchantments")) {
            NbtCompound enchantments = storedEnchantments.getCompound("minecraft:stored_enchantments");
            if (enchantments.contains("levels")) {
                NbtCompound levels = enchantments.getCompound("levels");
                for (String enchantId : levels.getKeys()) {
                    String mapped = ENCHANT_TO_ABILITY.get(enchantId);
                    if (mapped != null && AbilityRegistry.get(mapped) != null)
                        return mapped;
                    if (AbilityRegistry.get(enchantId) != null)
                        return enchantId;
                }
            }
        }

        return null;
    }

    private static String checkEnchantList(NbtList enchants) {
        for (int i = 0; i < enchants.size(); i++) {
            NbtCompound ench = enchants.getCompound(i);
            if (!ench.contains("id")) continue;

            String enchantId = ench.getString("id");
            if (enchantId == null || enchantId.isEmpty()) continue;

            String mapped = ENCHANT_TO_ABILITY.get(enchantId);
            if (mapped != null && AbilityRegistry.get(mapped) != null)
                return mapped;

            if (AbilityRegistry.get(enchantId) != null)
                return enchantId;

            int colon = enchantId.indexOf(':');
            if (colon != -1) {
                String stripped = enchantId.substring(colon + 1);
                mapped = ENCHANT_TO_ABILITY.get(stripped);
                if (mapped != null && AbilityRegistry.get(mapped) != null)
                    return mapped;
                if (AbilityRegistry.get(stripped) != null)
                    return stripped;
            }
        }
        return null;
    }

    public static void setAbility(ItemStack stack, String abilityId) {
        NbtCompound tag = readNbt(stack);
        if (tag == null) tag = new NbtCompound();
        tag.putString(NBT_KEY, abilityId);
        writeNbt(stack, tag);
    }

    @Nullable
    public static NbtCompound readNbt(ItemStack stack) {
        if (stack.isEmpty()) return null;
        NbtComponent comp = stack.get(DataComponentTypes.CUSTOM_DATA);
        return comp != null ? comp.copyNbt() : new NbtCompound();   // <- safe
    }

    private static void writeNbt(ItemStack stack, @Nullable NbtCompound tag) {
        if (tag == null || tag.isEmpty()) {
            stack.remove(DataComponentTypes.CUSTOM_DATA);
        } else {
            stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(tag));
        }
    }
}