package net.carmindy.kipmod;

import net.carmindy.kipmod.abilities.Abilities;
import net.carmindy.kipmod.abilities.AbilityRegistry;
import net.carmindy.kipmod.abilities.ModAbilities;
import net.carmindy.kipmod.config.KIPModAutoConfig;
import net.carmindy.kipmod.component.AbilityBookComponent;
import net.carmindy.kipmod.component.AbilityComponent;
import net.carmindy.kipmod.component.KIPModComponents;
import net.carmindy.kipmod.events.AbilityTickHandler;
import net.carmindy.kipmod.events.BookUseHandler;
import net.carmindy.kipmod.events.EffBreakHandler;
import net.carmindy.kipmod.network.AbilityUsePayload;
import net.carmindy.kipmod.network.TryAbilityBookPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;

public class KnowledgeIsPowerMod implements ModInitializer {

    public static final String MOD_ID = "knowledge-is-power-mod";

    private void registerDebugCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("kipmod")
                    .then(CommandManager.literal("debugbook")
                            .executes(context -> {
                                ServerPlayerEntity player = context.getSource().getPlayer();
                                ItemStack mainHand = player.getMainHandStack();

                                System.out.println("=== DETAILED BOOK ANALYSIS ===");
                                System.out.println("Item: " + mainHand.getItem());
                                System.out.println("Item class: " + mainHand.getItem().getClass().getName());

                                NbtCompound tag = AbilityBookComponent.readNbt(mainHand);
                                System.out.println("NBT contents: " + tag);

                                if (mainHand.getItem() instanceof net.minecraft.item.EnchantedBookItem) {
                                    System.out.println("This is an EnchantedBookItem!");
                                }

                                String currentDetection = AbilityBookComponent.getAbility(mainHand);
                                System.out.println("Current detection result: " + currentDetection);

                                if (tag != null && tag.contains(AbilityBookComponent.STORED_ENCHANTMENTS_KEY)) {
                                    NbtList storedEnchantments = tag.getList(AbilityBookComponent.STORED_ENCHANTMENTS_KEY, 10);
                                    System.out.println("Stored Enchantments:");
                                    for (int i = 0; i < storedEnchantments.size(); i++) {
                                        NbtCompound enchant = storedEnchantments.getCompound(i);
                                        System.out.println("  - ID: " + enchant.getString("id") + ", Level: " + enchant.getInt("lvl"));
                                    }
                                }

                                context.getSource().sendFeedback(() ->
                                        Text.literal("Check console for detailed analysis"), false);
                                return 1;
                            })
                    )
            );
        });
    }


    public static void registerPackets() {


        PayloadTypeRegistry.playC2S().register(
                TryAbilityBookPayload.ID,
                PacketCodec.of(TryAbilityBookPayload::encode, TryAbilityBookPayload::decode)
        );

        ServerPlayNetworking.registerGlobalReceiver(
                TryAbilityBookPayload.ID,
                (payload, ctx) -> ctx.server().execute(() -> {
                    ServerPlayerEntity player = ctx.player();
                    ItemStack stack = player.getMainHandStack();

                    if (!(stack.getItem() instanceof net.minecraft.item.EnchantedBookItem)) return;

                    String abilityId = AbilityBookComponent.getAbility(stack);
                    if (abilityId == null) {
                        player.sendMessage(Text.literal("No registered ability for this book."), false);
                        return;
                    }

                    Abilities ability = AbilityRegistry.get(abilityId);
                    if (ability != null) {
                        KIPModComponents.ABILITIES.get(player).setAbility(ability);
                        player.sendMessage(Text.literal("Ability learned: " + ability.getName()), false);
                    }
                })
        );


        PayloadTypeRegistry.playC2S().register(
                AbilityUsePayload.ID,
                PacketCodec.of(AbilityUsePayload::encode, AbilityUsePayload::decode)
        );

        ServerPlayNetworking.registerGlobalReceiver(
                AbilityUsePayload.ID,
                (payload, ctx) -> ctx.server().execute(() -> {
                    var comp = KIPModComponents.ABILITIES.maybeGet(ctx.player());
                    if (comp == null) {
                        ctx.player().sendMessage(Text.literal("Error: Abilities component not found!"), false);
                        return;
                    }
                    comp.get().tryUseAbility();

                })
        );
    }

    @Override
    public void onInitialize() {
        System.out.println("KIP Mod initializing...");
        KIPModComponents.ABILITIES = ComponentRegistry.getOrCreate(
                Identifier.of(MOD_ID, "abilities"),
                AbilityComponent.class
        );
        KIPModAutoConfig.init();
        ModAbilities.register();
        BookUseHandler.registerHandler();
        AbilityTickHandler.register();
        EffBreakHandler.register();
        registerPackets();
        registerDebugCommands();
        System.out.println("Handlers registered.");
    }

}
