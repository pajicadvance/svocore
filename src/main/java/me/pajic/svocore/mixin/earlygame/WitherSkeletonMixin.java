package me.pajic.svocore.mixin.earlygame;

import me.pajic.svocore.SVO;
import net.minecraft.world.entity.monster.skeleton.WitherSkeleton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(WitherSkeleton.class)
public class WitherSkeletonMixin {

    @ModifyArg(
            method = "populateDefaultEquipmentSlots",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/monster/skeleton/WitherSkeleton;setItemSlot(Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/ItemStack;)V"
            ),
            index = 1
    )
    private ItemStack changeSwordType(ItemStack original) {
        return SVO.CONFIG.earlyGameExtension.get() ? new ItemStack(Items.GOLDEN_SWORD) : original;
    }
}
