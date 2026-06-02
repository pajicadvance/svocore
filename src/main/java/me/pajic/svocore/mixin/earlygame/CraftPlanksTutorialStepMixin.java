package me.pajic.svocore.mixin.earlygame;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.pajic.svocore.SVO;
import me.pajic.svocore.item.ModItems;
import net.minecraft.client.gui.components.toasts.TutorialToast;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.tutorial.CraftPlanksTutorialStep;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

// turns this tutorial into a "Craft flint axe" tutorial
@Mixin(CraftPlanksTutorialStep.class)
public class CraftPlanksTutorialStepMixin {

    @ModifyExpressionValue(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/chat/Component;translatable(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;",
                    ordinal = 0
            )
    )
    private static MutableComponent modifyTutorialTitle(MutableComponent original) {
        return SVO.CONFIG.earlyGameExtension.get() ? Component.translatable("tutorial.svocore.craft_flint_axe.title") : original;
    }

    @ModifyExpressionValue(
            method = "tick",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/components/toasts/TutorialToast$Icons;WOODEN_PLANKS:Lnet/minecraft/client/gui/components/toasts/TutorialToast$Icons;",
                    opcode = Opcodes.GETSTATIC
            )
    )
    private TutorialToast.Icons modifyTutorialIcon(TutorialToast.Icons original) {
        return SVO.CONFIG.earlyGameExtension.get() ? TutorialToast.Icons.valueOf("SVO_CORE_FLINT_AXE") : original;
    }

    @WrapMethod(method = "hasCraftedPlanksPreviously")
    private static boolean cancel(LocalPlayer player, TagKey<Item> tag, Operation<Boolean> original) {
        return !SVO.CONFIG.earlyGameExtension.get() && original.call(player, tag);
    }

    @WrapOperation(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Inventory;contains(Lnet/minecraft/tags/TagKey;)Z"
            )
    )
    private boolean checkFlintAxe(Inventory instance, TagKey<Item> tag, Operation<Boolean> original) {
        return SVO.CONFIG.earlyGameExtension.get() ? instance.contains(itemStack -> itemStack.is(ModItems.FLINT_AXE)) : original.call(instance, tag);
    }

    @WrapOperation(
            method = "onGetItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/tags/TagKey;)Z"
            )
    )
    private boolean checkFlintAxe(ItemStack instance, TagKey<Item> tagKey, Operation<Boolean> original) {
        return SVO.CONFIG.earlyGameExtension.get() ? instance.is(ModItems.FLINT_AXE) : original.call(instance, tagKey);
    }
}
