package me.pajic.svocore.mixin.earlygame;

import me.pajic.svocore.SVO;
import net.minecraft.client.gui.components.toasts.TutorialToast;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TutorialToast.Icons.class)
public enum TutorialToastIconsMixin {
    SVO_CORE_LEAVES(SVO.id("toast/leaves")),
    SVO_CORE_GRAVEL(SVO.id("toast/gravel")),
    SVO_CORE_FLINT_AXE(SVO.id("toast/flint_axe"));

    @Shadow TutorialToastIconsMixin(Identifier sprite) {}
}
