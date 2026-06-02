package me.pajic.svocore.mixin.clifffixes;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.svocore.SVO;
import net.minecraft.world.level.levelgen.SurfaceRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SurfaceRules.Context.SteepMaterialCondition.class)
public abstract class SteepMaterialConditionMixin extends SurfaceRules.LazyXZCondition {

    protected SteepMaterialConditionMixin(SurfaceRules.Context context) {
        super(context);
    }

    @Definition(id = "heightSouth", local = @Local(type = int.class, name = "heightSouth"))
    @Definition(id = "heightNorth", local = @Local(type = int.class, name = "heightNorth"))
    @Expression("heightSouth >= heightNorth + 4")
    @ModifyExpressionValue(
            method = "compute",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private boolean modifyCondition1(
            boolean original,
            @Local(name = "heightSouth") int heightSouth,
            @Local(name = "heightNorth") int heightNorth
    ) {
        return SVO.CONFIG.cliffFixes.get() ? Math.abs(heightNorth - heightSouth) >= 4 : original;
    }

    @Definition(id = "heightWest", local = @Local(type = int.class, name = "heightWest"))
    @Definition(id = "heightEast", local = @Local(type = int.class, name = "heightEast"))
    @Expression("heightWest >= heightEast + 4")
    @ModifyExpressionValue(
            method = "compute",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private boolean modifyCondition2(
            boolean original,
            @Local(name = "heightWest") int heightWest,
            @Local(name = "heightEast") int heightEast
    ) {
        return SVO.CONFIG.cliffFixes.get() ? Math.abs(heightWest - heightEast) >= 4 : original;
    }
}
