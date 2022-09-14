package me.andante.nochickenrender.mixin.client;

import me.andante.nochickenrender.NoChickenRender;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.passive.ChickenEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(ChickenEntity.class)
public class ChickenEntityMixin {
    @Inject(method = "tickMovement", at = @At("HEAD"), cancellable = true)
    private void onTickMovement(CallbackInfo ci) {
        if (NoChickenRender.ENABLED) ci.cancel();
    }

    @Inject(method = "hasWings", at = @At("HEAD"), cancellable = true)
    private void onHasWings(CallbackInfoReturnable<Boolean> cir) {
        if (NoChickenRender.ENABLED) cir.setReturnValue(false);
    }

    @Inject(method = "addFlapEffects", at = @At("HEAD"), cancellable = true)
    private void onAddFlapEffects(CallbackInfo ci) {
        if (NoChickenRender.ENABLED) ci.cancel();
    }
}
