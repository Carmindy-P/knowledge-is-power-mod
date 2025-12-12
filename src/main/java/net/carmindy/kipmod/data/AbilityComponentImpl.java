package net.carmindy.kipmod.data;

import net.carmindy.kipmod.abilities.Abilities;
import net.carmindy.kipmod.abilities.AbilityRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

/**
 * Stores a player's learned ability, level, cooldown state, and handles syncing to the client.
 */
public class AbilityComponentImpl implements AbilityComponent, AutoSyncedComponent {

    private final PlayerEntity player;
    private int level = 0;

    @Nullable
    private Abilities learnedAbility = null;

    // server-side cooldown counter
    private int cooldown = 0;

    public AbilityComponentImpl(PlayerEntity player) {
        this.player = player;
    }

    // --- Level ---
    @Override
    public int getLevel() { return level; }

    @Override
    public void setLevel(int level) {
        this.level = level;
        KIPModComponents.ABILITIES.sync(player);
    }

    // --- Ability ---
    @Override
    @Nullable
    public Abilities getAbility() { return learnedAbility; }

    @Override
    public void setAbility(@Nullable Abilities ability) {
        this.learnedAbility = ability;
        if (ability != null && !player.getWorld().isClient()) {
            ability.onApply((net.minecraft.server.network.ServerPlayerEntity) player);
        }
        KIPModComponents.ABILITIES.sync(player);
    }

    // --- Cooldown ---
    @Override
    public int getCooldown() { return cooldown; }

    @Override
    public void setCooldown(int ticks) {
        this.cooldown = ticks;
        KIPModComponents.ABILITIES.sync(player);
    }

    @Override
    public void tickCooldown() {
        if (cooldown > 0) {
            cooldown--;
            KIPModComponents.ABILITIES.sync(player);
        }
    }

    // --- Use ability ---
    /** Tries to activate the current ability. Returns true if executed. */
    public boolean tryUseAbility() {
        if (player.getWorld().isClient) return false;
        if (learnedAbility == null) return false;
        if (cooldown > 0) return false;

        // Activate ability
        learnedAbility.activate((net.minecraft.server.network.ServerPlayerEntity) player);

        // Remove one-time abilities automatically
        if (learnedAbility.isOneTimeUse()) {
            setAbility(null);
        }

        // Apply cooldown
        setCooldown(learnedAbility.getCooldownTicks());
        return true;
    }

    // --- NBT Serialization ---
    @Override
    public void readFromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        this.level = nbt.getInt("Level");
        this.cooldown = nbt.getInt("Cooldown");
        if (nbt.contains("Ability")) {
            this.learnedAbility = AbilityRegistry.get(nbt.getString("Ability"));
        } else {
            this.learnedAbility = null;
        }
    }

    @Override
    public void writeToNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        nbt.putInt("Level", level);
        nbt.putInt("Cooldown", cooldown);
        if (learnedAbility != null) {
            nbt.putString("Ability", learnedAbility.getId());
        }
    }
}
