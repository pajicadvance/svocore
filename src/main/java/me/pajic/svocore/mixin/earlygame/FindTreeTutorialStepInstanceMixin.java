package me.pajic.svocore.mixin.earlygame;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.pajic.svocore.SVO;
import net.minecraft.client.gui.components.toasts.TutorialToast;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.tutorial.FindTreeTutorialStepInstance;
import net.minecraft.client.tutorial.TutorialSteps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.phys.HitResult;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

// turns this tutorial into a "Get sticks" tutorial
@Mixin(FindTreeTutorialStepInstance.class)
public class FindTreeTutorialStepInstanceMixin {

    @ModifyExpressionValue(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/chat/Component;translatable(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;",
                    ordinal = 0
            )
    )
    private static MutableComponent modifyTutorialTitle(MutableComponent original) {
        return SVO.CONFIG.earlyGameExtension.get() ? Component.translatable("tutorial.svocore.get_sticks.title") : original;
    }

    @ModifyExpressionValue(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/chat/Component;translatable(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;",
                    ordinal = 1
            )
    )
    private static MutableComponent modifyTutorialDescription(MutableComponent original) {
        return SVO.CONFIG.earlyGameExtension.get() ? Component.translatable("tutorial.svocore.get_sticks.description") : original;
    }

    @ModifyExpressionValue(
            method = "tick",
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=6000"
            )
    )
    private int reduceWait(int original) {
        return SVO.CONFIG.earlyGameExtension.get() ? 200 : original;
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
        return SVO.CONFIG.earlyGameExtension.get() ? TutorialToast.Icons.valueOf("SVO_CORE_LEAVES") : original;
    }

    @ModifyExpressionValue(
            method = "tick",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/tutorial/TutorialSteps;CRAFT_PLANKS:Lnet/minecraft/client/tutorial/TutorialSteps;",
                    opcode = Opcodes.GETSTATIC
            )
    )
    private TutorialSteps noSkip1(TutorialSteps original) {
        return SVO.CONFIG.earlyGameExtension.get() ? TutorialSteps.PUNCH_TREE : original;
    }

    @ModifyExpressionValue(
            method = "onGetItem",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/tutorial/TutorialSteps;CRAFT_PLANKS:Lnet/minecraft/client/tutorial/TutorialSteps;",
                    opcode = Opcodes.GETSTATIC
            )
    )
    private TutorialSteps noSkip2(TutorialSteps original) {
        return SVO.CONFIG.earlyGameExtension.get() ? TutorialSteps.PUNCH_TREE : original;
    }

    @WrapMethod(method = "hasPunchedTreesPreviously")
    private static boolean noSkip3(LocalPlayer player, Operation<Boolean> original) {
        return !SVO.CONFIG.earlyGameExtension.get() && original.call(player);
    }

    @WrapMethod(method = "onLookAt")
    private void noSkip4(ClientLevel level, HitResult hit, Operation<Void> original) {
        if (!SVO.CONFIG.earlyGameExtension.get()) original.call(level, hit);
    }
}
