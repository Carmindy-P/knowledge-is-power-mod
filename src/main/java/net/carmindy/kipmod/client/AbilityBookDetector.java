// net.carmindy.kipmod.client.AbilityBookDetector
package net.carmindy.kipmod.client;

import net.carmindy.kipmod.network.TryAbilityBookPayload;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Items;

@Environment(EnvType.CLIENT)
public class AbilityBookDetector {
    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            while (client.options.useKey.wasPressed()) {
                var stack = client.player.getMainHandStack();
                if (stack.isOf(Items.ENCHANTED_BOOK)) {
                    ClientPlayNetworking.send(new TryAbilityBookPayload());
                }
            }
        });
    }
}