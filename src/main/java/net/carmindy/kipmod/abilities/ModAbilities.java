package net.carmindy.kipmod.abilities;

public class ModAbilities {
    public static final FlameAbility FLAME = new FlameAbility();

    // Register all mod abilities to the AbilityRegistry
    public static void register() {
        AbilityRegistry.register(FLAME.getId(), FLAME);
        // Register other abilities here
    }
}