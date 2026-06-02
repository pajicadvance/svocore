package me.pajic.svocore.mixin.integration.reliable_gliders;

import com.evandev.reliable_gliders.registry.ModItems;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import me.pajic.svocore.SVO;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@IfModLoaded("reliable_gliders")
@Mixin(ModItems.class)
public class ModItemsMixin {

    @ModifyExpressionValue(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/Item$Properties;durability(I)Lnet/minecraft/world/item/Item$Properties;"
            )
    )
    private static Item.Properties modifyGliderComponents(Item.Properties original) {
        return SVO.CONFIG.misc.repairableReliableGlider.get() ? original.repairable(Items.PHANTOM_MEMBRANE) : original;
    }
}
