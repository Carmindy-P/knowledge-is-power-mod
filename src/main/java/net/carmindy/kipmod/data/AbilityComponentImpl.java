package net.carmindy.kipmod.data;

import net.carmindy.kipmod.abilities.AbilityRegistry;
import net.carmindy.kipmod.abilities.Abilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

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
        // sync automatically
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
        KIPModComponents.ABILITIES.sync(player);
    }

    // Called when saving/loading world
    @Override
    public void readFromNbt(NbtCompound tag) {
        this.level = tag.getInt("Level");
        if (tag.contains("Ability")) {
            this.learnedAbility = AbilityRegistry.get(tag.getString("Ability"));
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("Level", level);
        if (learnedAbility != null) {
            tag.putString("Ability", learnedAbility.getId());
        }
    }
}
