package me.andante.test.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import me.andante.test.api.world.ExclusiveRaycastContext;
import me.andante.test.api.client.config.TestConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

public class BasicWAILA {
    @Environment(EnvType.CLIENT)
    @Mixin(InGameHud.class)
    public static abstract class InGameHudMixin {
        @Shadow @Final private MinecraftClient client;
        @Shadow private int scaledWidth;
        @Shadow public abstract TextRenderer getTextRenderer();

        @Inject(method = "render", at = @At("TAIL"))
        private void renderBasicWAILA(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
            if (!TestConfig.INSTANCE.waila.getValue()) return;

            if (this.client.options.debugEnabled) return;

            HitResult hitResult = this.client.crosshairTarget;
            Text targeted = this.getTargetedText(this.client.crosshairTarget);
            if (targeted == null) return;

            matrices.push();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            TextRenderer textRenderer = this.getTextRenderer();
            int height = textRenderer.fontHeight;
            matrices.translate(this.scaledWidth - 3, height - (height - 2), 0.0D);

            int wrapPoint = this.scaledWidth / 2;

            List<OrderedText> texts = new ArrayList<>(textRenderer.wrapLines(targeted, wrapPoint));
            int originalLength = texts.size() - 1;

            Text alternativeTargeted = this.compareHitResults(hitResult, this.getAlternativeCrosshairTarget(hitResult, tickDelta));
            if (alternativeTargeted != null) {
                Text text = Text.literal("(").append(alternativeTargeted).append(")");
                texts.addAll(textRenderer.wrapLines(text, wrapPoint));
            }

            for (int i = 0, l = texts.size(); i < l; i++) {
                OrderedText text = texts.get(i);
                matrices.push();
                matrices.translate(-textRenderer.getWidth(text), 0.0D, 0.0D);
                boolean isAlternative = i > originalLength;
                textRenderer.drawWithShadow(matrices, text, 0, (i * height) + (isAlternative ? 2 : 0), isAlternative ? Formatting.GRAY.getColorValue() : 0xFFFFFF);
                matrices.pop();
            }

            RenderSystem.disableBlend();
            matrices.pop();
        }

        private @Unique Text compareHitResults(HitResult normal, HitResult alt) {
            if (normal instanceof EntityHitResult) {
                if (alt instanceof EntityHitResult) return null;
            } else if (normal instanceof BlockHitResult blockNormal && alt instanceof BlockHitResult blockAlt) {
                if (this.getTargetedBlock(blockNormal) == this.getTargetedBlock(blockAlt)) return null;
            }
            return this.getTargetedText(alt);
        }

        private @Unique HitResult getAlternativeCrosshairTarget(HitResult other, float tickDelta) {
            Entity camera = this.client.getCameraEntity();
            double reachDistance = this.client.interactionManager.getReachDistance();
            double reachDistanceSqr = reachDistance * reachDistance;

            Vec3d cameraPosVec = camera.getCameraPosVec(tickDelta);
            Vec3d rotationVec = camera.getRotationVec(tickDelta);
            Vec3d squaredMax = cameraPosVec.add(rotationVec.x * reachDistanceSqr, rotationVec.y * reachDistanceSqr, rotationVec.z * reachDistanceSqr);

            // entity hit result
            Box box = camera.getBoundingBox().stretch(rotationVec.multiply(reachDistanceSqr)).expand(1.0D);
            EntityHitResult entityHitResult = ProjectileUtil.raycast(camera, cameraPosVec, squaredMax, box, entity -> !entity.isSpectator(), reachDistanceSqr);
            if (entityHitResult != null) return entityHitResult;

            // block hit result
            double extendedReachDistance = reachDistance + 2;
            Vec3d max = cameraPosVec.add(rotationVec.x * extendedReachDistance, rotationVec.y * extendedReachDistance, rotationVec.z * extendedReachDistance);
            Block block = other instanceof BlockHitResult otherBlock ? this.getTargetedBlock(otherBlock) : null;
            return camera.world.raycast(new ExclusiveRaycastContext(cameraPosVec, max, camera, block));
        }

        private @Unique Text getTargetedText(HitResult hitResult) {
            if (hitResult instanceof BlockHitResult blockHitResult) {
                Block block = this.getTargetedBlock(blockHitResult);
                if (block != null) return Text.translatable(block.getTranslationKey());
            } else if (hitResult instanceof EntityHitResult entityHitResult) {
                Entity entity = entityHitResult.getEntity();
                return Text.translatable(entity.getType().getTranslationKey());
            }
            return null;
        }

        private @Unique Block getTargetedBlock(BlockHitResult hitResult) {
            BlockPos pos = hitResult.getBlockPos();
            BlockState state = this.client.world.getBlockState(pos);
            return state.isAir() ? null : state.getBlock();
        }
    }
}
