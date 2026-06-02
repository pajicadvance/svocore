package me.pajic.svocore.mixin.earlygame;

import me.pajic.svocore.CompatFlags;
import me.pajic.svocore.SVO;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Mixin(CreativeModeTab.class)
public class CreativeModeTabMixin {

    @Unique private static final List<String> EG_HIDDEN_TOOLS = List.of(
            "minecraft:stone_pickaxe",
            "minecraft:stone_shovel",
            "minecraft:stone_sword",
            "minecraft:stone_axe",
            "minecraft:stone_hoe",
            "minecraft:stone_spear",
            "minecraft:wooden_pickaxe",
            "minecraft:wooden_shovel",
            "minecraft:wooden_sword",
            "minecraft:wooden_axe",
            "minecraft:wooden_hoe",
            "minecraft:wooden_spear"
    );

    @Shadow private Collection<ItemStack> displayItems;
    @Shadow private Set<ItemStack> displayItemsSearchTab;

    @Inject(
            method = "buildContents",
            at = @At("TAIL")
    )
    private void hideItems(CallbackInfo ci) {
        filter(displayItems);
        filter(displayItemsSearchTab);
    }

    @Unique
    private void filter(Collection<ItemStack> items) {
        items.removeIf(stack -> (SVO.CONFIG.earlyGameExtension.get() && EG_HIDDEN_TOOLS.contains(BuiltInRegistries.ITEM.getKey(stack.getItem()).toString())) ||
                (CompatFlags.HORSEMAN_LOADED && stack.is(ResourceKey.create(Registries.ITEM, Identifier.parse("thecopperierage:copper_horn")))));
    }
}
