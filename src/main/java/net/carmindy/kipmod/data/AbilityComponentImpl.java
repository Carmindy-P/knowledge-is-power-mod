package net.carmindy.kipmod.data;

import net.carmindy.kipmod.abilities.AbilityRegistry;
import net.carmindy.kipmod.abilities.Abilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

/**
 * Implementation of AbilityComponent for players.
 * Stores the learned ability and its level, and handles syncing to the client.
 */
public class AbilityComponentImpl implements AbilityComponent, AutoSyncedComponent {

    private final PlayerEntity player;
    private int level = 0;
    @Nullable private Abilities learnedAbility = null;

    public AbilityComponentImpl(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
        // Automatically sync to client when level changes
        KIPModComponents.ABILITIES.sync(player);
    }

    @Override
    @Nullable
    public Abilities getAbility() {
        return learnedAbility;
    }

    @Override
    public void setAbility(@Nullable Abilities ability) {
        this.learnedAbility = ability;
        // Automatically sync to client when ability changes
        KIPModComponents.ABILITIES.sync(player);
    }

    /** Called when loading component data from NBT (disk load) */
    @Override
    public void readFromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        this.level = nbt.getInt("Level");

        if (nbt.contains("Ability")) {
            this.learnedAbility = AbilityRegistry.get(nbt.getString("Ability"));
        } else {
            this.learnedAbility = null;
        }
    }

    /** Called when saving component data to NBT (disk save) */
    @Override
    public void writeToNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        nbt.putInt("Level", level);

        if (learnedAbility != null) {
            nbt.putString("Ability", learnedAbility.getId());
        }
    }
}
