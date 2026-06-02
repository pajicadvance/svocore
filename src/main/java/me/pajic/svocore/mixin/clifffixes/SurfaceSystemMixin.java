package me.pajic.svocore.mixin.clifffixes;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.svocore.SVO;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.SurfaceSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(SurfaceSystem.class)
public class SurfaceSystemMixin {

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @ModifyReturnValue(
            method = "topMaterial",
            at = @At("RETURN")
    )
    private Optional<BlockState> modifyTopMaterial(Optional<BlockState> original, @Local(name = "context") SurfaceRules.Context context) {
        return SVO.CONFIG.cliffFixes.get() ? original.map(state -> state.is(BlockTags.DIRT) && context.steep.test() ? Blocks.STONE.defaultBlockState() : state) : original;
    }

    @ModifyExpressionValue(
            method = "buildSurface",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/levelgen/SurfaceRules$SurfaceRule;tryApply(III)Lnet/minecraft/world/level/block/state/BlockState;"
            )
    )
    private BlockState buildSurface(BlockState original, @Local(name = "context") SurfaceRules.Context context) {
        return SVO.CONFIG.cliffFixes.get() ? original != null && original.is(BlockTags.DIRT) && context.steep.test() ? Blocks.STONE.defaultBlockState() : original : original;
    }
}
