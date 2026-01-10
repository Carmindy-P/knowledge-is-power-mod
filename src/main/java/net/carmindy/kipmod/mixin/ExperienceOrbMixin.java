package net.carmindy.kipmod.mixin;

import net.carmindy.kipmod.data.KIPModComponents;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExperienceOrbEntity.class)
public abstract class ExperienceOrbMixin {

    @Shadow
    public abstract int getExperienceAmount();

    @Inject(method = "onPlayerCollision", at = @At("HEAD"))
    private void kipmod$onOrbTouch(CallbackInfo ci) {
        ExperienceOrbEntity orb = (ExperienceOrbEntity) (Object) this;
        if (orb.getWorld().isClient) return;

        orb.getWorld().getEntitiesByClass(ServerPlayerEntity.class,
                        orb.getBoundingBox().expand(0.2),
                        p -> p.squaredDistanceTo(orb) < 1.0)
                .forEach(player ->
                        KIPModComponents.ABILITIES.get(player).onXpGain(getExperienceAmount())
                );
    }
}