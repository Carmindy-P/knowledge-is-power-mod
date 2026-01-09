package net.carmindy.kipmod.abilities;

public class ModAbilities {
    public static final FlameAbility FLAME = new FlameAbility();
    public static final EfficiencyAbility EFFICIENCY = new EfficiencyAbility();
    public static final FeatherFallingAbility FEATHERFALLING = new FeatherFallingAbility();
    public static final ChannelingAbility CHANNELING = new ChannelingAbility();


    public static void register() {
        AbilityRegistry.register(FLAME.getId(), FLAME);
        AbilityRegistry.register(EFFICIENCY.getId(), EFFICIENCY);
        AbilityRegistry.register(FEATHERFALLING.getId(), FEATHERFALLING);
        AbilityRegistry.register(CHANNELING.getId(), CHANNELING);
    }
}