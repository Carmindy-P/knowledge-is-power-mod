package net.carmindy.kipmod.keybinds;

import net.carmindy.kipmod.hud.AbilityHud;
import net.carmindy.kipmod.network.AbilityUsePayload;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class AbilityKeybinds {

    private static KeyBinding abilityKey;

    public static void register() {

        abilityKey = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.knowledge-is-power-mod.ability",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_R,
                        "category.knowledge-is-power-mod"
                )
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            while (abilityKey.wasPressed()) {

                if (ClientPlayNetworking.canSend(
                        AbilityUsePayload.ID)) {

                    ClientPlayNetworking.send(
                            AbilityUsePayload.INSTANCE
                    );
                }
            }
        });
    }
}