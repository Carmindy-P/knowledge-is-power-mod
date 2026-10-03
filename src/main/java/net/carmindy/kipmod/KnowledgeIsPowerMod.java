package net.carmindy.kipmod;

import net.carmindy.kipmod.abilities.*;
import net.carmindy.kipmod.component.AbilityBookComponent;
import net.carmindy.kipmod.component.AbilityComponent;
import net.carmindy.kipmod.component.KIPModComponents;
import net.carmindy.kipmod.events.AbilityTickHandler;
import net.carmindy.kipmod.events.BookUseHandler;
import net.carmindy.kipmod.events.EffBreakHandler;
import net.carmindy.kipmod.network.AbilityCooldownPayload;
import net.carmindy.kipmod.network.AbilityUsePayload;
import net.carmindy.kipmod.network.KIPModNetwork;
import net.carmindy.kipmod.network.TryAbilityBookPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class KnowledgeIsPowerMod implements ModInitializer {

    public static final String MOD_ID = "knowledge-is-power-mod";

    private void registerDebugCommands() {

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                    CommandManager.literal("kipmod")

                            // /kipmod debugbook
                            .then(CommandManager.literal("debugbook")
                                    .executes(context -> {

                                        ServerPlayerEntity player =
                                                context.getSource().getPlayer();

                                        ItemStack mainHand =
                                                player.getMainHandStack();

                                        System.out.println("=== DETAILED BOOK ANALYSIS ===");
                                        System.out.println("Item: " + mainHand.getItem());
                                        System.out.println(
                                                "Item class: "
                                                        + mainHand.getItem().getClass().getName()
                                        );

                                        NbtCompound tag =
                                                AbilityBookComponent.readNbt(mainHand);

                                        System.out.println("NBT contents: " + tag);

                                        if (mainHand.getItem()
                                                instanceof net.minecraft.item.EnchantedBookItem) {

                                            System.out.println(
                                                    "This is an EnchantedBookItem!"
                                            );
                                        }

                                        String currentDetection =
                                                AbilityBookComponent.getAbility(mainHand);

                                        System.out.println(
                                                "Current detection result: "
                                                        + currentDetection
                                        );

                                        if (tag != null &&
                                                tag.contains(
                                                        AbilityBookComponent.STORED_ENCHANTMENTS_KEY
                                                )) {

                                            NbtList storedEnchantments =
                                                    tag.getList(
                                                            AbilityBookComponent.STORED_ENCHANTMENTS_KEY,
                                                            10
                                                    );

                                            System.out.println("Stored Enchantments:");

                                            for (int i = 0;
                                                 i < storedEnchantments.size();
                                                 i++) {

                                                NbtCompound enchant =
                                                        storedEnchantments.getCompound(i);

                                                System.out.println(
                                                        "  - ID: "
                                                                + enchant.getString("id")
                                                                + ", Level: "
                                                                + enchant.getInt("lvl")
                                                );
                                            }
                                        }

                                        context.getSource().sendFeedback(
                                                () -> Text.literal(
                                                        "Check console for detailed analysis"
                                                ),
                                                false
                                        );

                                        return 1;
                                    })
                            )

                            // /kipmod loyalty <player>
                            .then(CommandManager.literal("loyalty")
                                    .then(
                                            CommandManager.argument(
                                                            "player",
                                                            net.minecraft.command.argument.EntityArgumentType.player()
                                                    )
                                                    .executes(context -> {

                                                        ServerPlayerEntity protector =
                                                                context.getSource().getPlayer();

                                                        ServerPlayerEntity target =
                                                                net.minecraft.command.argument.EntityArgumentType
                                                                        .getPlayer(
                                                                                context,
                                                                                "player"
                                                                        );

                                                        LoyaltyAbility.setBond(
                                                                protector.getUuid(),
                                                                target.getUuid()
                                                        );

                                                        protector.sendMessage(
                                                                Text.literal(
                                                                        "You are now loyal to "
                                                                                + target.getName().getString()
                                                                ),
                                                                false
                                                        );

                                                        target.sendMessage(
                                                                Text.literal(
                                                                        protector.getName().getString()
                                                                                + " has sworn loyalty to you!"
                                                                ),
                                                                false
                                                        );

                                                        return 1;
                                                    })
                                    )
                            )
            );
        });
    }


    /**
     * Registers the server-side handlers for the payloads.
     * <p>
     * This MUST happen after registerPayloads().
     */
    public static void registerServerReceivers() {

        // ================================
        // TRY ENCHANTED BOOK
        // ================================

        ServerPlayNetworking.registerGlobalReceiver(
                TryAbilityBookPayload.ID,
                (payload, ctx) -> ctx.server().execute(() -> {

                    ServerPlayerEntity player = ctx.player();
                    ItemStack stack = player.getMainHandStack();

                    if (!(stack.getItem()
                            instanceof net.minecraft.item.EnchantedBookItem)) {

                        player.sendMessage(
                                Text.literal("Not an enchanted book."),
                                false
                        );

                        return;
                    }

                    String abilityId =
                            AbilityBookComponent.getAbility(stack);

                    System.out.println("[KIPMod] =======================");
                    System.out.println(
                            "[KIPMod] Book ability ID: "
                                    + abilityId
                    );
                    System.out.println(
                            "[KIPMod] Registry size: "
                                    + AbilityRegistry.size()
                    );

                    if (abilityId == null) {

                        player.sendMessage(
                                Text.literal(
                                        "No registered ability for this book."
                                ),
                                false
                        );

                        return;
                    }

                    Abilities ability =
                            AbilityRegistry.get(abilityId);

                    System.out.println(
                            "[KIPMod] Registry lookup: "
                                    + ability
                    );

                    if (ability == null) {

                        player.sendMessage(
                                Text.literal(
                                        "Ability not registered: "
                                                + abilityId
                                ),
                                false
                        );

                        return;
                    }

                    KIPModComponents.ABILITIES
                            .maybeGet(player)
                            .ifPresentOrElse(

                                    component -> {

                                        component.setAbility(ability);

                                        player.sendMessage(
                                                Text.literal(
                                                        "Ability learned: "
                                                                + ability.getName()
                                                ),
                                                false
                                        );

                                        System.out.println(
                                                "[KIPMod] Ability successfully applied: "
                                                        + abilityId
                                        );
                                    },

                                    () -> {

                                        player.sendMessage(
                                                Text.literal(
                                                        "ERROR: Player abilities component is missing."
                                                ),
                                                false
                                        );

                                        System.out.println(
                                                "[KIPMod] ERROR: No abilities component on player!"
                                        );
                                    }
                            );
                })
        );

        // ================================
        // USE ABILITY
        // ================================

        ServerPlayNetworking.registerGlobalReceiver(
                AbilityUsePayload.ID,
                (payload, ctx) -> ctx.server().execute(() -> {

                    ServerPlayerEntity player = ctx.player();

                    KIPModComponents.ABILITIES
                            .maybeGet(player)
                            .ifPresentOrElse(

                                    component -> {

                                        /*
                                         * This is the important part.
                                         *
                                         * tryUseAbility() returns true ONLY
                                         * when the server actually accepted
                                         * the ability use.
                                         *
                                         * If the player presses R while
                                         * cooldown > 0, it returns false,
                                         * so we send NOTHING to the HUD.
                                         */
                                        boolean used =
                                                component.tryUseAbility();

                                        if (used) {

                                            int cooldown =
                                                    component.getCooldown();

                                            /*
                                             * Tell only this player's
                                             * client that a real cooldown
                                             * has started.
                                             */
                                            if (cooldown > 0) {

                                                ServerPlayNetworking.send(
                                                        player,
                                                        new AbilityCooldownPayload(
                                                                cooldown
                                                        )
                                                );
                                            }
                                        }
                                    },

                                    () -> System.out.println(
                                            "[KIPMod] ERROR: Player abilities component missing!"
                                    )
                            );
                })
        );

        System.out.println("[KIPMod] Server payload receivers registered.");
    }

    @Override
    public void onInitialize() {
        System.out.println("KIP Mod initializing...");

        // Register all enchantment abilities FIRST
        ModAbilities.register();

        // Register all payload types
        KIPModNetwork.register();

        // Register server packet receivers
        registerServerReceivers();

        BookUseHandler.registerHandler();
        AbilityTickHandler.register();

        SharpnessAbility.registerEvents();
        CurseOfBindingAbility.registerEvents();
        AquaAffinityAbility.registerEvents();
        UnbreakingAbility.registerEvents();
        FortuneAbility.registerEvents();
        LoyaltyAbility.registerEvents();
        MultishotAbility.registerEvents();
        LootingAbility.registerEvents();
        BlastProtectionAbility.registerEvents();
        BreachAbility.registerEvents();
        EffBreakHandler.register();
        ProjectileProtectionAbility.registerEvents();

        registerDebugCommands();

        System.out.println(
                "[KIPMod] Registered "
                        + AbilityRegistry.size()
                        + " abilities."
        );

        System.out.println("Handlers registered.");
    }
}
