package me.pajic.svocore.mixin.earlygame;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.pajic.svocore.SVO;
import net.minecraft.client.gui.components.toasts.TutorialToast;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.tutorial.PunchTreeTutorialStepInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

// turns this tutorial into a "Get flint" tutorial
@Mixin(PunchTreeTutorialStepInstance.class)
public class PunchTreeTutorialStepInstanceMixin {

    @ModifyExpressionValue(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/chat/Component;translatable(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;"
            )
    )
    private static MutableComponent modifyTutorialTitle(MutableComponent original) {
        return SVO.CONFIG.earlyGameExtension.get() ? Component.translatable("tutorial.svocore.get_flint.title") : original;
    }

    @ModifyExpressionValue(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/chat/Component;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/network/chat/MutableComponent;"
            )
    )
    private static MutableComponent modifyTutorialDescription(MutableComponent original) {
        return SVO.CONFIG.earlyGameExtension.get() ? Component.translatable("tutorial.svocore.get_flint.description") : original;
    }

    @ModifyExpressionValue(
            method = "tick",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/components/toasts/TutorialToast$Icons;TREE:Lnet/minecraft/client/gui/components/toasts/TutorialToast$Icons;",
                    opcode = Opcodes.GETSTATIC
            )
    )
    private TutorialToast.Icons modifyTutorialIcon(TutorialToast.Icons original) {
        return SVO.CONFIG.earlyGameExtension.get() ? TutorialToast.Icons.valueOf("SVO_CORE_GRAVEL") : original;
    }

    @ModifyExpressionValue(
            method = "tick",
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=600"
            )
    )
    private int reduceWait(int original) {
        return SVO.CONFIG.earlyGameExtension.get() ? 200 : original;
    }

    @WrapMethod(method = "onDestroyBlock")
    private void cancel(ClientLevel level, BlockPos pos, BlockState state, float percent, Operation<Void> original) {
        if (!SVO.CONFIG.earlyGameExtension.get()) original.call(level, pos, state, percent);
    }

    @WrapOperation(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Inventory;contains(Lnet/minecraft/tags/TagKey;)Z"
            )
    )
    private boolean checkFlint(Inventory instance, TagKey<Item> tag, Operation<Boolean> original) {
        return SVO.CONFIG.earlyGameExtension.get() ? instance.contains(itemStack -> itemStack.is(Items.FLINT)) : original.call(instance, tag);
    }

    @WrapOperation(
            method = "onGetItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/tags/TagKey;)Z"
            )
    )
    private boolean checkFlint(ItemStack instance, TagKey<Item> tagKey, Operation<Boolean> original) {
        return SVO.CONFIG.earlyGameExtension.get() ? instance.is(Items.FLINT) : original.call(instance, tagKey);
    }
}
