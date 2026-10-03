
package net.carmindy.kipmod;

import net.carmindy.kipmod.abilities.ModAbilities;
import net.carmindy.kipmod.hud.AbilityHud;
import net.carmindy.kipmod.keybinds.AbilityKeybinds;
import net.carmindy.kipmod.network.AbilityCooldownPayload;
import net.carmindy.kipmod.tooltip.EnchantedBookTooltip;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class KIPModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        // Register abilities on the client.
        ModAbilities.register();

        // Keybind
        AbilityKeybinds.register();

        // Enchanted book tooltip
        EnchantedBookTooltip.register();

        /*
         * Receive cooldowns ONLY when the server confirms
         * that an ability was actually activated.
         */
        ClientPlayNetworking.registerGlobalReceiver(
                AbilityCooldownPayload.ID,
                (payload, context) -> {

                    context.client().execute(() -> {

                        AbilityHud.startCooldown(
                                payload.ticks()
                        );
                    });
                }
        );

        // HUD
        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {

            AbilityHud.render(
                    drawContext,
                    tickCounter.getTickDelta(false)
            );
        });
    }
}

