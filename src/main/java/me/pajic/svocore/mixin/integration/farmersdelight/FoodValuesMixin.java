package me.pajic.svocore.mixin.integration.farmersdelight;

import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import me.pajic.svocore.SVO;
import net.minecraft.world.food.FoodProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vectorwing.farmersdelight.common.FoodValues;

@IfModLoaded("farmersdelight")
@Mixin(FoodValues.class)
public class FoodValuesMixin {

    @Shadow @Mutable @Final public static FoodProperties CAKE_SLICE;

    @Inject(
            method = "<clinit>",
            at = @At("TAIL")
    )
    private static void buffCakeSlice(CallbackInfo ci) {
        CAKE_SLICE = (new FoodProperties.Builder())
                .nutrition(SVO.CONFIG.misc.cakeSliceFoodLevel.get())
                .saturationModifier(SVO.CONFIG.misc.cakeSliceSaturationLevel.get())
                .build();
    }
}
