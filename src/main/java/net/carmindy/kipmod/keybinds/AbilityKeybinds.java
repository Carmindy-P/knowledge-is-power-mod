package net.carmindy.kipmod.keybinds;

import net.carmindy.kipmod.data.KIPModComponents;
import net.carmindy.kipmod.network.AbilityUsePayload;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class AbilityKeybinds {

    public static KeyBinding USE_ABILITY;

    public static void register() {
        USE_ABILITY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.kipmod.use_ability",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category.kipmod"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            while (USE_ABILITY.wasPressed()) {
                String abilityId = KIPModComponents.ABILITIES.get(client.player).getAbility() != null
                        ? KIPModComponents.ABILITIES.get(client.player).getAbility().getId()
                        : "";

                ClientPlayNetworking.send(new AbilityUsePayload(abilityId));
            }
        });
    }
}
