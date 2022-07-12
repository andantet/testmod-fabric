package me.andante.test.mixin.client;

import me.andante.test.api.client.config.TestClientConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.projectile.FishingBobberEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class FishingBobberAccessibility {
    @Environment(EnvType.CLIENT)
    @Mixin(Entity.class)
    public static abstract class EntityMixin {
        @Inject(method = "isGlowing", at = @At("HEAD"), cancellable = true)
        private void onIsGlowing(CallbackInfoReturnable<Boolean> cir) {
            if (!TestClientConfig.INSTANCE.fishingBobberGlowing.getValue()) return;
            Entity that = Entity.class.cast(this);
            if (that instanceof FishingBobberEntity bobber && bobber.getDataTracker().get(FishingBobberEntityAccessor.getCAUGHT_FISH())) cir.setReturnValue(true);
        }
    }

    @Environment(EnvType.CLIENT)
    @Mixin(FishingBobberEntity.class)
    public interface FishingBobberEntityAccessor {
        @Accessor static TrackedData<Boolean> getCAUGHT_FISH() { throw new AssertionError(); }
    }
}
