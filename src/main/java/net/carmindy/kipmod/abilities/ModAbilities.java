package net.carmindy.kipmod.abilities;

public class ModAbilities {
    public static final FlameAbility FLAME = new FlameAbility();
    public static final EfficiencyAbility EFFICIENCY = new EfficiencyAbility();
    public static final FeatherFallingAbility FEATHERFALLING = new FeatherFallingAbility();
    public static final ChannelingAbility CHANNELING = new ChannelingAbility();
    public static final MendingAbility MENDING = new MendingAbility();
    public static final CurseOfVanishingAbility CURSEOFVANISHING = new CurseOfVanishingAbility();
    public static final UnbreakingAbility UNBREAKING = new UnbreakingAbility();
    public static final ProtectionAbility PROTECTION = new ProtectionAbility();
    public static final BlastProtectionAbility BLASTPROTECTION = new BlastProtectionAbility();
    public static final FireProtectionAbility FIREPROTECTION = new FireProtectionAbility();

    public static void register() {
        AbilityRegistry.register(FLAME.getId(), FLAME);
        AbilityRegistry.register(EFFICIENCY.getId(), EFFICIENCY);
        AbilityRegistry.register(FEATHERFALLING.getId(), FEATHERFALLING);
        AbilityRegistry.register(CHANNELING.getId(), CHANNELING);
        AbilityRegistry.register(MENDING.getId(), MENDING);
        AbilityRegistry.register(CURSEOFVANISHING.getId(), CURSEOFVANISHING);
        AbilityRegistry.register(UNBREAKING.getId(), UNBREAKING);
        AbilityRegistry.register(PROTECTION.getId(), PROTECTION);
        AbilityRegistry.register(BLASTPROTECTION.getId(), BLASTPROTECTION);
        AbilityRegistry.register(FIREPROTECTION.getId(), FIREPROTECTION);
    }
}