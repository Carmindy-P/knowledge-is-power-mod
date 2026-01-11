package net.carmindy.kipmod.data;

import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;

public class KIPEntityComponents implements EntityComponentInitializer {

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(
                KIPModComponents.ABILITIES,
                player -> new AbilityComponentImpl(player)
        );
    }
}