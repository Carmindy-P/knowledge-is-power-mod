package net.carmindy.kipmod.events;

/**
 * Centralized class for registering all mod events.
 */
public class KIPModEvents {

    public static void register() {
        BookUseHandler.registerHandler(); // Register book use event
    }
}
