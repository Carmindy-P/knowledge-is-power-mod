package net.carmindy.kipmod.component;

import net.carmindy.kipmod.abilities.Abilities;
import net.carmindy.kipmod.abilities.AbilityRegistry;
import net.carmindy.kipmod.abilities.MendingAbility;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;
import org.ladysnake.cca.api.v3.entity.RespawnableComponent;

public class AbilityComponentImpl
        implements AbilityComponent, AutoSyncedComponent, RespawnableComponent<AbilityComponent> {

    private final PlayerEntity player;

    private @Nullable Abilities learnedAbility = null;
    private int cooldown = 0;
    private boolean instamine = false;
    private int charges = 0;

    public AbilityComponentImpl(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public void setLevel(int level) {
    }

    @Override
    @Nullable
    public Abilities getAbility() {
        return learnedAbility;
    }

    @Override
    public void onXpGain(int orbValue) {
        if (player.getWorld().isClient()) {
            return;
        }

        if (learnedAbility instanceof MendingAbility m) {
            m.onXpGain((ServerPlayerEntity) player, orbValue);
        }
    }

    @Override
    public void setAbility(@Nullable Abilities ability) {

        if (this.learnedAbility != null && !player.getWorld().isClient()) {
            this.learnedAbility.deactivate((ServerPlayerEntity) player);
        }

        this.learnedAbility = ability;
        this.cooldown = 0;

        if (ability != null && !player.getWorld().isClient()) {
            ability.onApply((ServerPlayerEntity) player);
        }

        // Sync the new ability to the client.
        if (!player.getWorld().isClient()) {
            KIPModComponents.ABILITIES.sync(player);
        }
    }

    @Override
    public int getCooldown() {
        return cooldown;
    }

    @Override
    public void setCooldown(int ticks) {
        this.cooldown = ticks;

        if (!player.getWorld().isClient()) {
            KIPModComponents.ABILITIES.sync(player);
        }
    }

    @Override
    public void tickCooldown() {
        if (cooldown > 0) {
            cooldown--;
        }
    }

    @Override
    public boolean tryUseAbility() {

        if (player.getWorld().isClient()) {
            return false;
        }

        if (learnedAbility == null) {
            return false;
        }

        if (cooldown > 0) {
            return false;
        }

        Abilities ability = learnedAbility;

        ability.activate((ServerPlayerEntity) player);

        if (ability.isOneTimeUse()) {
            setAbility(null);
            return true;
        }

        setCooldown(ability.getCooldownTicks());

        return true;
    }

    @Override
    public int getCharges() {
        return charges;
    }

    @Override
    public void setCharges(int charges) {
        this.charges = charges;
    }

    @Override
    public void setInstamine(boolean value) {
        this.instamine = value;
    }

    @Override
    public boolean isInstamine() {
        return instamine;
    }

    @Override
    public void readFromNbt(
            NbtCompound nbt,
            RegistryWrapper.WrapperLookup registryLookup
    ) {
        if (nbt.contains("AbilityId")) {
            String id = nbt.getString("AbilityId");
            this.learnedAbility = AbilityRegistry.get(id);
        } else {
            this.learnedAbility = null;
        }

        this.cooldown = nbt.getInt("Cooldown");
        this.instamine = nbt.getBoolean("Instamine");
    }

    @Override
    public void writeToNbt(
            NbtCompound nbt,
            RegistryWrapper.WrapperLookup registryLookup
    ) {
        if (learnedAbility != null) {
            nbt.putString("AbilityId", learnedAbility.getId());
        } else {
            nbt.remove("AbilityId");
        }

        nbt.putInt("Cooldown", cooldown);
        nbt.putBoolean("Instamine", instamine);
    }

    @Override
    public void copyFrom(
            AbilityComponent original,
            RespawnCopyStrategy strategy
    ) {
        this.setLevel(original.getLevel());
        this.setCooldown(original.getCooldown());
        this.setInstamine(original.isInstamine());
        this.setCharges(original.getCharges());
        this.setAbility(original.getAbility());
    }
}