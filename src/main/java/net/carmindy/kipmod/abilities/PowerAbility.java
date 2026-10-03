package net.carmindy.kipmod.abilities;

import net.carmindy.kipmod.component.KIPModComponents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PowerAbility implements Abilities {

    @Override public String getId()   { return "power"; }
    @Override public String getName() { return "Power"; }
    @Override public String getDescription() { return "Gain one random ability."; }
    @Override public boolean isOneTimeUse() { return true; }
    @Override public int getCooldownTicks() { return 0; }

    private static final Random RANDOM = new Random();

    @Override
    public void activate(ServerPlayerEntity player) {
        if (player.getWorld().isClient()) return;

        List<Abilities> all = new ArrayList<>(AbilityRegistry.getAllAbilities());
        all.removeIf(a -> a instanceof PowerAbility);

        if (all.isEmpty()) {
            player.sendMessage(Text.literal("No other abilities available."), false);
            return;
        }

        Abilities chosen = all.get(RANDOM.nextInt(all.size()));
        KIPModComponents.ABILITIES.get(player).setAbility(chosen);
        player.sendMessage(Text.literal("Power granted: " + chosen.getName()), false);
    }

    @Override public void tick(ServerPlayerEntity player) { }
    @Override public void deactivate(ServerPlayerEntity player) { }
}