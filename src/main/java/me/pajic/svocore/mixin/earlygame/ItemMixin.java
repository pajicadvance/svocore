package me.pajic.svocore.mixin.earlygame;

import me.pajic.svocore.SVO;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Item.class)
public class ItemMixin {

    @Unique
    private static final List<String> EG_ANCIENT_TOOLS = List.of(
            "minecraft:wooden_pickaxe",
            "minecraft:wooden_shovel",
            "minecraft:wooden_sword",
            "minecraft:wooden_axe",
            "minecraft:wooden_hoe",
            "minecraft:wooden_spear"
    );

    @Inject(
            method = "mineBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V"
            )
    )
    private void woodBad(ItemStack itemStack, Level level, BlockState state, BlockPos pos, LivingEntity owner, CallbackInfoReturnable<Boolean> cir) {
        if (
                SVO.CONFIG.earlyGameExtension.get() &&
                EG_ANCIENT_TOOLS.stream().anyMatch(s -> itemStack.is(ResourceKey.create(Registries.ITEM, Identifier.parse(s))))
        ) {
            itemStack.setDamageValue(itemStack.getMaxDamage() - 1);
        }
    }
}
