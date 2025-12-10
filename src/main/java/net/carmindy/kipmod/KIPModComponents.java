package net.carmindy.kipmod;

import net.carmindy.kipmod.data.AbilityComponent;
import net.carmindy.kipmod.data.PlayerAbilityComponent;
import net.minecraft.util.Identifier;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;

public class KIPModComponents {

    public static ComponentKey<AbilityComponent> ABILITY;

    public static void registerComponents() {
        ABILITY = ComponentRegistry.getOrCreate(
                Identifier.tryParse("kipmod:ability"),
                AbilityComponent.class // interface or abstract type
        );
    }
}
