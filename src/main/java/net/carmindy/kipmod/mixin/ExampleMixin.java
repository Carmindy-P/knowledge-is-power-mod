package net.carmindy.kipmod.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Example Mixin demonstrating how to inject code into MinecraftServer.
 * This mixin targets the MinecraftServer.loadWorld() method and injects
 * at the start of the method (HEAD). Useful for initialization logic.
 */
@Mixin(MinecraftServer.class)
public class ExampleMixin {

    @Inject(at = @At("HEAD"), method = "loadWorld")
    private void init(CallbackInfo info) {
        // This code runs at the very start of loadWorld()
        // Example: you could initialize mod-specific world data here
    }
}
