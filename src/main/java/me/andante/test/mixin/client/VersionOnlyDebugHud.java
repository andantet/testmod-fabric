package me.andante.test.mixin.client;

import com.google.common.collect.Lists;
import me.andante.test.api.client.config.TestClientConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

public class VersionOnlyDebugHud {
    @Environment(EnvType.CLIENT)
    @Mixin(DebugHud.class)
    public static class DebugHudMixin {
        @Shadow @Final private MinecraftClient client;

        @Inject(method = "getLeftText", at = @At("HEAD"), cancellable = true)
        private void onGetLeftText(CallbackInfoReturnable<List<String>> cir) {
            if (!TestClientConfig.INSTANCE.versionOnlyDebugHud.getValue()) return;

            if (!this.client.options.debugEnabled) {
                cir.setReturnValue(Lists.newArrayList("Minecraft " + SharedConstants.getGameVersion().getName() + " (" + this.client.getGameVersion() + "/" + ClientBrandRetriever.getClientModName() + ")"));
            }
        }

        @Redirect(method = "renderLeftText", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
        private <T> boolean onRenderLeftTextAdd(List<T> list, T text) {
            if (TestClientConfig.INSTANCE.versionOnlyDebugHud.getValue()) {
                if (!this.client.options.debugEnabled) return false;
            }

            return list.add(text);
        }

        @Inject(method = "renderRightText", at = @At("HEAD"), cancellable = true)
        private void onRenderRightText(MatrixStack matrices, CallbackInfo ci) {
            if (!TestClientConfig.INSTANCE.versionOnlyDebugHud.getValue()) return;
            if (!this.client.options.debugEnabled) ci.cancel();
        }
    }

    @Environment(EnvType.CLIENT)
    @Mixin(InGameHud.class)
    public static class InGameHudMixin {
        @Shadow @Final private MinecraftClient client;

        @Redirect(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/client/option/GameOptions;debugEnabled:Z"))
        private boolean onRender(GameOptions options) {
            if (!TestClientConfig.INSTANCE.versionOnlyDebugHud.getValue()) return this.client.options.debugEnabled;
            return true;
        }
    }
}
