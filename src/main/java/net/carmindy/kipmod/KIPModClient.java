package net.carmindy.kipmod;

import net.fabricmc.api.ClientModInitializer;

/**
 * Client-side initializer for the mod.
 * Only runs on the client and is used for client-specific tasks
 * such as rendering, client events, or key bindings.
 */
public class KIPModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Client-specific setup here
        // For example, register client-side renderers or HUD elements
    }
}
