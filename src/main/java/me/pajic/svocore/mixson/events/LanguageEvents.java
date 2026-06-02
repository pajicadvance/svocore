package me.pajic.svocore.mixson.events;

import com.google.gson.JsonObject;
import me.pajic.svocore.SVO;
import me.pajic.svocore.mixson.MixsonHelper;

public class LanguageEvents {
    public static void register() {
        MixsonHelper.registerSingleJson(
                "Modify vanilla item names",
                "minecraft:lang/en_us",
                context -> {
                    JsonObject file = context.getFile().getAsJsonObject();
                    file.remove("block.minecraft.stonecutter");
                    file.remove("container.stonecutter");
                    file.remove("stat.minecraft.interact_with_stonecutter");
                    file.remove("subtitles.ui.stonecutter.take_result");
                }
        );
        MixsonHelper.registerSingleJson(
                "Modify item descriptions",
                "item_descriptions:lang/en_us",
                context -> {
                    JsonObject file = context.getFile().getAsJsonObject();
                    file.remove("lore.minecraft.lantern");
                    file.remove("lore.minecraft.stonecutter");
                    file.remove("lore.minecraft.compass");
                }
        );
        MixsonHelper.registerSingleJson(
                "Modify item descriptions",
                "svocore:lang/en_us",
                context -> {
                    JsonObject file = context.getFile().getAsJsonObject();
                    if (SVO.CONFIG.earlyGameExtension.get()) {
                        String txt = file.get("lore.wooden_tools.custom").getAsString();
                        file.addProperty("lore.minecraft.wooden_pickaxe", txt);
                        file.addProperty("lore.minecraft.wooden_shovel", txt);
                        file.addProperty("lore.minecraft.wooden_sword", txt);
                        file.addProperty("lore.minecraft.wooden_hoe", txt);
                        file.addProperty("lore.minecraft.wooden_axe", txt);
                        file.addProperty("lore.minecraft.wooden_spear", txt);
                    }
                }
        );
    }
}
