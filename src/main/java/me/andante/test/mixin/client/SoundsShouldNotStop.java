package me.andante.test.mixin.client;

import me.andante.test.api.client.config.TestClientConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

public class SoundsShouldNotStop {
    @Environment(EnvType.CLIENT)
    @Mixin(SoundManager.class)
    public static class SoundManagerMixin {
        @Redirect(method = "updateSoundVolume", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundManager;stopAll()V"))
        private void onStopAll(SoundManager manager) {
            if (!TestClientConfig.INSTANCE.soundsShouldNotStopSoundOptions.getValue()) manager.stopAll();
        }
    }

    @Environment(EnvType.CLIENT)
    @Mixin(MinecraftClient.class)
    private static class MinecraftClientMixin {
        @Redirect(method = "reset", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundManager;stopAll()V"))
        private void onStopAll(SoundManager manager) {
            if (!TestClientConfig.INSTANCE.soundsShouldNotStopResetScreen.getValue()) manager.stopAll();
        }
    }
}
