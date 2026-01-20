package net.carmindy.kipmod.component;

import net.carmindy.kipmod.KnowledgeIsPowerMod;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.util.Identifier;

public class KIPModComponents implements EntityComponentInitializer {

    public static ComponentKey<AbilityComponent> ABILITIES =
            ComponentRegistry.getOrCreate(
                    Identifier.of(KnowledgeIsPowerMod.MOD_ID, "abilities"),
                    AbilityComponent.class
            );

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(
                ABILITIES,
                AbilityComponentImpl::new,
                RespawnCopyStrategy.ALWAYS_COPY
        );
    }
}