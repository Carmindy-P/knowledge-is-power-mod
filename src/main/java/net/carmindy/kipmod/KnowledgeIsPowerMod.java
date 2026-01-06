package net.carmindy.kipmod;

import net.carmindy.kipmod.abilities.Abilities;
import net.carmindy.kipmod.abilities.AbilityRegistry;
import net.carmindy.kipmod.abilities.ModAbilities;
import net.carmindy.kipmod.data.AbilityBookComponent;
import net.carmindy.kipmod.data.KIPModComponents;
import net.carmindy.kipmod.events.AbilityTickHandler;
import net.carmindy.kipmod.events.BookUseHandler;
import net.carmindy.kipmod.network.AbilityPackets;

import net.carmindy.kipmod.network.AbilityUsePayload;
import net.carmindy.kipmod.network.TryAbilityBookPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class KnowledgeIsPowerMod implements ModInitializer {

    public static final String MOD_ID = "knowledge-is-power-mod";
    // KnowledgeIsPowerMod.java (or a dedicated NetworkSetup class)
    public static void registerPackets() {
        // codec
        PayloadTypeRegistry.playC2S().register(
                TryAbilityBookPayload.ID,
                PacketCodec.of(TryAbilityBookPayload::encode, TryAbilityBookPayload::decode)
        );

        // handler
        ServerPlayNetworking.registerGlobalReceiver(
                TryAbilityBookPayload.ID,
                (payload, ctx) -> {
                    ctx.server().execute(() -> {
                        ServerPlayerEntity player = ctx.player();
                        ItemStack stack = player.getMainHandStack();
                        if (!stack.isOf(Items.ENCHANTED_BOOK)) return;

                        String abilityId = AbilityBookComponent.getAbility(stack);
                        if (abilityId == null) {
                            player.sendMessage(Text.literal("No registered ability for this book."), false);
                            return;
                        }

                        AbilityBookComponent.setAbility(stack, abilityId);
                        Abilities ability = AbilityRegistry.get(abilityId);
                        if (ability != null) {
                            KIPModComponents.ABILITIES.get(player).setAbility(ability);
                            player.sendMessage(Text.literal("Ability learned: " + ability.getName()), false);
                        }
                    });
                }
        );
    }
    public static void register() {
        // 1. codec first
        PayloadTypeRegistry.playC2S().register(
                AbilityUsePayload.ID,
                PacketCodec.of(AbilityUsePayload::encode, AbilityUsePayload::decode)
        );

        // 2. handler second
        ServerPlayNetworking.registerGlobalReceiver(
                AbilityUsePayload.ID,
                (payload, ctx) -> {
                    ctx.server().execute(() -> {
                        var comp = KIPModComponents.ABILITIES.get(ctx.player());
                        comp.tryUseAbility();
                    });
                }
        );
    }
    @Override
    public void onInitialize() {
        System.out.println("KIP Mod initializing...");
        registerPackets();
        ModAbilities.register();
        BookUseHandler.registerHandler();

        // 1. codec first, handler second – both in one call
        KnowledgeIsPowerMod.register();

        AbilityTickHandler.register();
        System.out.println("Handlers registered.");
    }
}
