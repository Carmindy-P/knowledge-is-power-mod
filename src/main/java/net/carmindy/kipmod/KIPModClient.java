package net.carmindy.kipmod;

import net.carmindy.kipmod.keybinds.AbilityKeybinds;
import net.fabricmc.api.ClientModInitializer;

public class KIPModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        AbilityKeybinds.register();
    }
}
