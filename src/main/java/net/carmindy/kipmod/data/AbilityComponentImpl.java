package net.carmindy.kipmod.data;

import net.carmindy.kipmod.abilities.Abilities;
import net.carmindy.kipmod.abilities.MendingAbility;
import net.carmindy.kipmod.data.AbilityComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;
import org.ladysnake.cca.api.v3.entity.RespawnableComponent;

public class AbilityComponentImpl implements AbilityComponent, AutoSyncedComponent, RespawnableComponent<AbilityComponent> {
    private final PlayerEntity player;
    private @Nullable Abilities learnedAbility = null;
    private int cooldown = 0;
    private boolean instamine = false;

    public AbilityComponentImpl(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public int getLevel() {
        return 0; // Implement as needed
    }

    @Override
    public void setLevel(int level) {
        // Implement as needed
    }

    @Override
    @Nullable
    public Abilities getAbility() {
        return learnedAbility;
    }

    @Override
    public void onXpGain(int orbValue) {
        if (player.getWorld().isClient()) return;
        if (!(learnedAbility instanceof MendingAbility)) return;

        int hearts = orbValue / 10;
        if (hearts <= 0) return;

        player.heal(hearts * 2);
        player.sendMessage(Text.literal("Mending healed " + hearts + " ♥"), false);
    }

    /* AbilityComponentImpl.java */
    @Override
    public void setAbility(@Nullable Abilities ability) {
        if (this.learnedAbility != null && !player.getWorld().isClient()) {
            this.learnedAbility.deactivate((ServerPlayerEntity) player);
        }
        this.learnedAbility = ability;
        this.cooldown = 0;          // optional: reset cooldown
        if (ability != null && !player.getWorld().isClient()) {
            ability.onApply((ServerPlayerEntity) player);
        }
    }
    @Override
    public int getCooldown() {
        return cooldown;
    }

    @Override
    public void setCooldown(int ticks) {
        this.cooldown = ticks;
    }

    @Override
    public void tickCooldown() {
        if (cooldown > 0) {
            cooldown--;
        }
    }

    @Override
    public boolean tryUseAbility() {
        if (player.getWorld().isClient) return false;
        if (learnedAbility == null) return false;
        if (cooldown > 0) return false;

        learnedAbility.activate((ServerPlayerEntity) player);

        if (learnedAbility.isOneTimeUse()) {
            setAbility(null);
        }

        setCooldown(learnedAbility.getCooldownTicks());
        return true;
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
    public void readFromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        if (nbt.contains("AbilityId")) {
            String id = nbt.getString("AbilityId");
            this.learnedAbility = net.carmindy.kipmod.abilities.AbilityRegistry.get(id);
        } else {
            this.learnedAbility = null;
        }

        this.cooldown = nbt.getInt("Cooldown");
        this.instamine = nbt.getBoolean("Instamine");
    }

    @Override
    public void writeToNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        if (learnedAbility != null) {
            nbt.putString("AbilityId", learnedAbility.getId());
        }

        nbt.putInt("Cooldown", cooldown);
        nbt.putBoolean("Instamine", instamine);
    }

    public void copyFrom(AbilityComponent original, RespawnCopyStrategy strategy) {
        this.setLevel(original.getLevel());
        this.setCooldown(original.getCooldown());
        this.setInstamine(original.isInstamine());
        this.setAbility(original.getAbility());
    }
}