package me.andante.test.mixin.client;

import me.andante.test.api.client.config.TestClientConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.Sprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class AnimationsStopOnPause {
    @Environment(EnvType.CLIENT)
    @Mixin(Sprite.Animation.class)
    public static class SpriteAnimationMixin {
        @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
        private void onTick(CallbackInfo ci) {
            if (!TestClientConfig.INSTANCE.animationsStopOnPause.getValue()) return;
            if (MinecraftClient.getInstance().isPaused()) ci.cancel();
        }
    }
}
