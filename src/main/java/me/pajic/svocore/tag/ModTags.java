package me.pajic.svocore.tag;

import me.pajic.svocore.SVO;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {

    public static final TagKey<Item> FLINT_TOOL_MATERIALS = TagKey.create(
            Registries.ITEM,
            SVO.id("flint_tool_materials")
    );
}
