package me.pajic.svocore.item;

import me.pajic.svocore.SVO;
import me.pajic.svocore.tag.ModTags;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ToolMaterial;

public class ModItems {

    private static final ToolMaterial FLINT = new ToolMaterial(
            BlockTags.INCORRECT_FOR_WOODEN_TOOL,
            ToolMaterial.WOOD.durability(),
            ToolMaterial.WOOD.speed(),
            ToolMaterial.WOOD.attackDamageBonus(),
            ToolMaterial.WOOD.enchantmentValue(),
            ModTags.FLINT_TOOL_MATERIALS
    );

    public static final Item FLINT_PICKAXE = registerEarlyGameChangesItem(
            "flint_pickaxe", new Item.Properties().pickaxe(FLINT, 1.0F, -2.8F)
    );
    public static final Item FLINT_AXE = registerEarlyGameChangesItem(
            "flint_axe", new Item.Properties().axe(FLINT, 6.0F, -3.2F)
    );
    public static final Item FLINT_SWORD = registerEarlyGameChangesItem(
            "flint_sword", new Item.Properties().sword(FLINT, 3.0F, -2.4F)
    );
    public static final Item FLINT_SHOVEL = registerEarlyGameChangesItem(
            "flint_shovel", new Item.Properties().shovel(FLINT, 1.5F, -3.0F)
    );
    public static final Item FLINT_HOE = registerEarlyGameChangesItem(
            "flint_hoe", new Item.Properties().hoe(FLINT, 0.0F, -3.0F)
    );
    public static final Item FLINT_AXE_HEAD = registerEarlyGameChangesItem("flint_axe_head", new Item.Properties());

    public static void init() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries ->
                entries.insertBefore(Items.COPPER_SHOVEL, FLINT_AXE_HEAD, FLINT_SHOVEL, FLINT_PICKAXE, FLINT_AXE, FLINT_HOE)
        );
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT).register(entries -> {
            entries.insertBefore(Items.COPPER_AXE, FLINT_AXE);
            entries.insertBefore(Items.COPPER_SWORD, FLINT_SWORD);
        });
    }

    private static Item registerEarlyGameChangesItem(String name, Item.Properties properties) {
        return registerModItem(name, properties, SVO.CONFIG.earlyGameExtension.get());
    }

    private static Item registerModItem(String name, Item.Properties properties, boolean enabled) {
        return Registry.register(BuiltInRegistries.ITEM, SVO.id(name), new ModItem(properties, name, enabled));
    }
}
