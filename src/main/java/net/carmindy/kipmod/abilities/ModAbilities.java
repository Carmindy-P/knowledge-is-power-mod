package net.carmindy.kipmod.abilities;

public class ModAbilities {
    public static final FlameAbility FLAME = new FlameAbility();
    public static final EfficiencyAbility EFFICIENCY = new EfficiencyAbility();

    public static void register() {
        AbilityRegistry.register(FLAME.getId(), FLAME);
        AbilityRegistry.register(EFFICIENCY.getId(), EFFICIENCY);
    }
}