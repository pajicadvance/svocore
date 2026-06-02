package me.pajic.svocore.mixin.integration.wilderwild.bbe;

import betterblockentities.client.render.immediate.blockentity.extentions.BlockEntityExt;
import betterblockentities.client.render.immediate.blockentity.manager.InstancedBlockEntityManager;
import betterblockentities.client.render.immediate.blockentity.misc.RenderingMode;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import com.moulberry.mixinconstraints.annotations.IfModLoadeds;
import net.frozenblock.wilderwild.block.entity.StoneChestBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@IfModLoadeds({@IfModLoaded("wilderwild"), @IfModLoaded("betterblockentities")})
@Mixin(StoneChestBlockEntity.class)
public class StoneChestBlockEntityMixin {

    @SuppressWarnings("RedundantCast")
    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void init(BlockPos pos, BlockState state, CallbackInfo ci) {
        BlockEntityExt ext = (BlockEntityExt) (Object) this;
        ext.renderingMode(RenderingMode.IMMEDIATE);
        ext.optKind(InstancedBlockEntityManager.OptKind.NONE);
    }
}
