package net.carmindy.kipmod;

import net.carmindy.kipmod.data.KIPModComponents;
import net.fabricmc.api.ModInitializer;

public class KnowledgeIsPowerMod implements ModInitializer {

    public static final String MOD_ID = "knowledge-is-power-mod";

    @Override
    public void onInitialize() {
        KIPModComponents.register();
    }

}

