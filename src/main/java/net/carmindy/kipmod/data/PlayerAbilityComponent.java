package net.carmindy.kipmod.data;

import net.carmindy.kipmod.abilities.Abilities;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class PlayerAbilityComponent implements AbilityComponent, AutoSyncedComponent {
    private int level = 0;
    private Abilities ability;

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public @Nullable Abilities getAbility() {
        return ability;
    }

    @Override
    public void setAbility(@Nullable Abilities ability) {
        this.ability = ability;
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        this.level = tag.getInt("Level");
        // read ability if you want
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.putInt("Level", level);
        // write ability if you want
    }
}
