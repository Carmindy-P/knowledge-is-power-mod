package net.carmindy.kipmod.data;

import net.carmindy.kipmod.abilities.Abilities;
import net.carmindy.kipmod.abilities.AbilityRegistry;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.WeakHashMap;

public class AbilityBookComponent implements Component, AutoSyncedComponent {

    private static final Map<ItemStack, Abilities> STACK_ABILITIES = new WeakHashMap<>();

    @Nullable
    private Abilities ability;

    public void setAbility(@Nullable Abilities ability) {
        this.ability = ability;
    }

    @Nullable
    public Abilities getAbility() {
        return ability;
    }

    // Associate an ability with an ItemStack in memory
    public static void attachToStack(ItemStack stack, Abilities ability) {
        STACK_ABILITIES.put(stack, ability);
    }

    // Get the ability from an ItemStack
    @Nullable
    public static Abilities getFromStack(ItemStack stack) {
        return STACK_ABILITIES.get(stack);
    }

    // Check if an ItemStack has an associated ability
    public static boolean hasAbility(ItemStack stack) {
        return STACK_ABILITIES.containsKey(stack);
    }

    // Learn the ability from the ItemStack
    public void learnAbilityFromStack(ItemStack stack) {
        Abilities stackAbility = STACK_ABILITIES.get(stack);
        if (stackAbility != null) {
            this.ability = stackAbility;
        }
    }
    @Override
    public void readFromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup) {
        if (nbt.contains("Ability")) {
            this.ability = AbilityRegistry.get(nbt.getString("Ability"));
        }
    }

    @Override
    public void writeToNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup) {
        if (ability != null) {
            nbt.putString("Ability", ability.getId());
        }
    }

}
