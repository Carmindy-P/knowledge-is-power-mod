package net.carmindy.kipmod.mixin;

import net.carmindy.kipmod.component.KIPModComponents;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    @Inject(method = "addExperience(I)V", at = @At("HEAD"), require = 1)
    private void kipmod$onXp(int amount, CallbackInfo ci) {
        if (amount <= 0) return;
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        System.out.println("[MENDING-DEBUG] addExperience called, amount=" + amount +
                "  player=" + player.getName().getString());

        KIPModComponents.ABILITIES.maybeGet(player).ifPresentOrElse(
                comp -> {
                    System.out.println("[MENDING-DEBUG] component found, calling onXpGain");
                    comp.onXpGain(amount);
                },
                () -> System.out.println("[MENDING-DEBUG] NO component on player")
        );
    }
}