package net.carmindy.kipmod.hud;

import net.carmindy.kipmod.component.KIPModComponents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class AbilityHud {

    private static int displayedCooldown = 0;
    private static long lastTime = 0;
    private static String previousAbility = null;

    /**
     * Called by the server cooldown packet.
     *
     * This represents a REAL successful ability activation.
     */
    public static void startCooldown(int ticks) {

        if (ticks <= 0) {
            displayedCooldown = 0;
            lastTime = 0;
            return;
        }

        displayedCooldown = ticks;
        lastTime = System.currentTimeMillis();
    }

    public static void render(
            DrawContext drawContext,
            float tickDelta
    ) {

        MinecraftClient client =
                MinecraftClient.getInstance();

        if (client.player == null) {
            return;
        }

        KIPModComponents.ABILITIES
                .maybeGet(client.player)
                .ifPresent(component -> {

                    /*
                     * No ability = no HUD cooldown.
                     */
                    if (component.getAbility() == null) {

                        displayedCooldown = 0;
                        previousAbility = null;
                        lastTime = 0;

                        return;
                    }

                    String abilityId =
                            component.getAbility().getId();

                    /*
                     * If the player learned/switched to a
                     * different ability, reset the HUD.
                     */
                    if (!abilityId.equals(previousAbility)) {

                        previousAbility = abilityId;
                        displayedCooldown = 0;
                        lastTime = 0;
                    }

                    /*
                     * Visually count down.
                     */
                    if (displayedCooldown > 0) {

                        long now =
                                System.currentTimeMillis();

                        if (lastTime == 0) {
                            lastTime = now;
                        }

                        long elapsed =
                                now - lastTime;

                        if (elapsed >= 50) {

                            int ticksPassed =
                                    (int) (elapsed / 50);

                            displayedCooldown =
                                    Math.max(
                                            0,
                                            displayedCooldown
                                                    - ticksPassed
                                    );

                            lastTime +=
                                    ticksPassed * 50L;
                        }
                    }

                    String text;

                    if (displayedCooldown > 0) {

                        double seconds =
                                displayedCooldown / 20.0;

                        text =
                                component.getAbility().getName()
                                        + "  •  "
                                        + String.format(
                                        "%.1fs",
                                        seconds
                                );

                    } else {

                        text =
                                component.getAbility().getName()
                                        + "  •  READY";
                    }

                    int screenWidth =
                            client.getWindow()
                                    .getScaledWidth();

                    int screenHeight =
                            client.getWindow()
                                    .getScaledHeight();

                    int textWidth =
                            client.textRenderer
                                    .getWidth(text);

                    int x =
                            screenWidth
                                    - textWidth
                                    - 10;

                    int y =
                            screenHeight - 30;

                    drawContext.drawText(
                            client.textRenderer,
                            Text.literal(text),
                            x,
                            y,
                            0xFFFFFF,
                            true
                    );
                });
    }
}
