package net.carmindy.kipmod.abilities;

import net.carmindy.kipmod.abilities.Abilities;
import net.carmindy.kipmod.abilities.AbilityRegistry;
import net.carmindy.kipmod.abilities.FlameAbility;

public class ModAbilities {
    public static final FlameAbility FLAME = new FlameAbility();

    public static void register() {
        System.out.println("Registering ability: " + FLAME.getId());
        AbilityRegistry.register(FLAME.getId(), FLAME);

        // Verify it was registered
        Abilities test = AbilityRegistry.get("flame");
        System.out.println("Verification - Ability retrieved: " + test);
    }
}