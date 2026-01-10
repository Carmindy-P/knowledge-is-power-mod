package net.carmindy.kipmod.data;

import net.carmindy.kipmod.abilities.AbilityRegistry;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.registry.RegistryWrapper;

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

        if (!stack.isEmpty() && stack.getItem() instanceof EnchantedBookItem) {
            var enchants = stack.get(DataComponentTypes.STORED_ENCHANTMENTS);
            if (enchants != null && !enchants.isEmpty()) {
                for (var entry : enchants.getEnchantmentEntries()) {
                    String id = entry.getKey().getKey().get().getValue().toString();

                    TagKey<Enchantment> flameTag =
                            TagKey.of(RegistryKeys.ENCHANTMENT, Identifier.of("kipmod","grants_flame_ability"));
                    if (entry.getKey().isIn(flameTag)) return "flame";

                    TagKey<Enchantment> efficiencyTag =
                            TagKey.of(RegistryKeys.ENCHANTMENT, Identifier.of("kipmod","grants_efficiency_ability"));
                    if (entry.getKey().isIn(efficiencyTag)) return "efficiency";

                    TagKey<Enchantment> channelingTag =
                            TagKey.of(RegistryKeys.ENCHANTMENT, Identifier.of("kipmod","grants_channeling_ability"));
                    if (entry.getKey().isIn(channelingTag)) return "channeling";

                    TagKey<Enchantment> featherTag =
                            TagKey.of(RegistryKeys.ENCHANTMENT, Identifier.of("kipmod","grants_feather_falling_ability"));
                    if (entry.getKey().isIn(featherTag)) return "feather_falling";

                    TagKey<Enchantment> mendingTag =
                            TagKey.of(RegistryKeys.ENCHANTMENT, Identifier.of("kipmod","grants_mending_ability"));
                    if (entry.getKey().isIn(mendingTag)) return "mending";

                    /* ---- fallback hard map ---- */
                    String mapped = ENCHANT_TO_ABILITY.get(id);
                    if (mapped != null && AbilityRegistry.get(mapped) != null) return mapped;
                }
            }
        }

        // custom books
        NbtCompound tag = readNbt(stack);
        if (tag != null && tag.contains("Ability")) return tag.getString("Ability");

        return null;
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