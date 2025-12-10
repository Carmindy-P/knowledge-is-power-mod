package net.carmindy.kipmod.abilities;

/**
 * Holds all abilities for easy registration.
 */
public class ModAbilities {
    public static final FlameAbility FLAME = new FlameAbility();

    // Register all mod abilities to the AbilityRegistry
    public static void register() {
        AbilityRegistry.register(FLAME.getId(), FLAME);
    }
}
