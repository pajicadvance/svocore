package me.pajic.svocore.mixson.events;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import me.pajic.svocore.CompatFlags;
import me.pajic.svocore.SVO;
import me.pajic.svocore.mixson.MixsonHelper;

import java.util.List;

public class TagEvents {

    private static final List<String> EARLY_GAME_DISABLED_TOOLS = List.of(
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

    public static void register() {
        if (SVO.CONFIG.earlyGameExtension.get()) MixsonHelper.registerSingleJson(
                "Allow mining copper ore with flint tools",
                "minecraft:tags/block/needs_stone_tool",
                context -> {
                    JsonArray values = context.getFile().getAsJsonObject().getAsJsonArray("values");
                    values.remove(new JsonPrimitive("minecraft:copper_ore"));
                    values.remove(new JsonPrimitive("universal_ores:granite_copper_ore"));
                    values.remove(new JsonPrimitive("universal_ores:diorite_copper_ore"));
                    values.remove(new JsonPrimitive("universal_ores:andesite_copper_ore"));
                    values.remove(new JsonPrimitive("universal_ores:tuff_copper_ore"));
                    values.remove(new JsonPrimitive("universal_ores:calcite_copper_ore"));
                }
        );
        MixsonHelper.registerSingleJson(
                "Hide disabled items from recipe viewers",
                "c:tags/item/hidden_from_recipe_viewers",
                context -> {
                    if (SVO.CONFIG.earlyGameExtension.get()) EARLY_GAME_DISABLED_TOOLS.forEach(s -> {
                        JsonObject o = new JsonObject();
                        o.addProperty("id", s);
                        o.addProperty("required", false);
                        context.getFile().getAsJsonObject().getAsJsonArray("values").add(o);
                    });
                    if (CompatFlags.HORSEMAN_LOADED) {
                        JsonObject entry = new JsonObject();
                        entry.addProperty("id", "thecopperierage:copper_horn");
                        entry.addProperty("required", false);
                        context.getFile().getAsJsonObject().getAsJsonArray("values").add(entry);
                    }
                }
        );
    }
}
