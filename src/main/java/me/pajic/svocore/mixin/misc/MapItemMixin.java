package me.pajic.svocore.mixin.misc;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.pajic.svocore.SVO;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MapItem.class)
public class MapItemMixin {

    @WrapMethod(method = "inventoryTick")
    private void alwaysUpdateMaps(ItemStack itemStack, ServerLevel level, Entity owner, EquipmentSlot slot, Operation<Void> original) {
        if (SVO.CONFIG.misc.alwaysUpdateMaps.get()) original.call(itemStack, level, owner, EquipmentSlot.MAINHAND);
        else original.call(itemStack, level, owner, slot);
    }
}
