package net.carmindy.kipmod;

import net.carmindy.kipmod.component.AbilityComponent;
import net.carmindy.kipmod.component.KIPModComponents;
import net.carmindy.kipmod.network.AbilityUsePayload;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.codec.PacketCodec;

import static net.carmindy.kipmod.KnowledgeIsPowerMod.registerServerReceivers;

public class KIPModServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        registerServerReceivers();
    }
}