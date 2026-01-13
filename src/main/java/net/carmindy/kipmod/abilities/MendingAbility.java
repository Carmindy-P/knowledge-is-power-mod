package net.carmindy.kipmod.abilities;

import net.carmindy.kipmod.abilities.AbilityRegistry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class MendingAbility implements Abilities {
    @Override public String getId() { return "mending"; }
    @Override public String getName() { return "Mending"; }
    @Override public String getDescription() { return "Heals hearts when gaining XP orbs."; }

    @Override
    public void activate(ServerPlayerEntity player) { }

    @Override
    public void tick(ServerPlayerEntity player) { }

    @Override public boolean isOneTimeUse() { return false; }
    @Override public int getCooldownTicks() { return 0; }

    public void onXpGain(ServerPlayerEntity player, int orbValue) {
        var cfg = AbilityRegistry.settings(getId());
        int hearts = orbValue / cfg.orbDivisor();
        if (hearts <= 0) return;
        player.heal((float)(hearts * cfg.heartsPerOrb() * 2));
        player.sendMessage(Text.literal("Mending healed " + hearts + " ♥"), false);
    }

    @Override public void deactivate(ServerPlayerEntity player) { }
}