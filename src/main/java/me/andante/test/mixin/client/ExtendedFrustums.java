package me.andante.test.mixin.client;

import me.andante.test.api.client.config.TestClientConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class ExtendedFrustums {
    @Environment(EnvType.CLIENT)
    @Mixin(EntityRenderer.class)
    public static class EntityRendererMixin<T extends Entity> {
        @Inject(method = "shouldRender", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;ignoreCameraFrustum:Z", shift = At.Shift.BEFORE), cancellable = true)
        private void onShouldRender(T entity, Frustum frustum, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
            if (!TestClientConfig.INSTANCE.extendedFrustums.getValue()) return;
            if (entity instanceof AbstractDecorationEntity || entity instanceof ArmorStandEntity) cir.setReturnValue(true);
        }
    }
}
