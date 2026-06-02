package me.pajic.svocore.mixin.misc;

import me.pajic.svocore.SVO;
import net.minecraft.world.item.enchantment.effects.ReplaceDisk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ReplaceDisk.class)
public class ReplaceDiskMixin {

    @ModifyArg(
            method = "apply",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/Mth;square(I)I"
            )
    )
    private int triggerFrostWalkerEarly(int i) {
        return SVO.CONFIG.misc.frostWalkerEarlyTrigger.get() ? i - 1 : i;
    }
}
