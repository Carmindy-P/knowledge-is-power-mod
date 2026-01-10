package net.carmindy.kipmod.data;

import net.carmindy.kipmod.KnowledgeIsPowerMod;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.util.Identifier;

// KIPModComponents.java
public final class KIPModComponents {
    public static final ComponentKey<AbilityComponent> ABILITIES =
            ComponentRegistry.getOrCreate(
                    Identifier.of("knowledge-is-power-mod", "abilities"),
                    AbilityComponent.class
            );

    private KIPModComponents() {}
}
