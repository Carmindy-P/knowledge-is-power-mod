package net.carmindy.kipmod.data;

import net.carmindy.kipmod.abilities.Abilities;
import net.carmindy.kipmod.abilities.AbilityRegistry;
import net.carmindy.kipmod.abilities.MendingAbility;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class AbilityComponentImpl implements AbilityComponent, AutoSyncedComponent {

    private final PlayerEntity player;
    private int level = 0;
    private @Nullable Abilities learnedAbility = null;
    private int cooldown = 0;
    private boolean instamine = false;

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
        KIPModComponents.ABILITIES.sync(player);
    }

    @Override
    @Nullable
    public Abilities getAbility() {
        return learnedAbility;
    }
    @Override
    public void onXpGain(int orbValue) {
        if (player.getWorld().isClient()) return;          // safety
        if (!(learnedAbility instanceof MendingAbility)) return;

        // 1 heart (2 × half-hearts) per 10 xp – tune as you like
        int hearts = orbValue / 10;
        if (hearts <= 0) return;

        player.heal(hearts * 2);
        player.sendMessage(
                Text.literal("Mending healed " + hearts + " ♥"),
                false
        );
    }

    @Override
    public void setAbility(@Nullable Abilities ability) {
        this.learnedAbility = ability;
        if (ability != null && !player.getWorld().isClient()) {
            ability.onApply((ServerPlayerEntity) player);
        }
        KIPModComponents.ABILITIES.sync(player);
    }

    @Override
    public int getCooldown() {
        return cooldown;
    }

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
        KIPModComponents.ABILITIES.sync(player);
    }

    @Override
    public boolean isInstamine() {
        return instamine;
    }

    @Override
    public void readFromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        this.level = nbt.getInt("Level");
        this.cooldown = nbt.getInt("Cooldown");
        this.instamine = nbt.getBoolean("Instamine");
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
        nbt.putBoolean("Instamine", instamine);
        if (learnedAbility != null) {
            nbt.putString("Ability", learnedAbility.getId());
        }
    }

}