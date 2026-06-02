package me.pajic.svocore.mixson.events;

import me.pajic.svocore.CompatFlags;
import me.pajic.svocore.SVO;
import me.pajic.svocore.mixson.MixsonHelper;

import java.util.List;

public class RecipeEvents {

    private static final List<String> EARLY_GAME_DISABLED_TOOLS = List.of(
            "minecraft:recipe/stone_pickaxe",
            "minecraft:recipe/stone_shovel",
            "minecraft:recipe/stone_sword",
            "minecraft:recipe/stone_axe",
            "minecraft:recipe/stone_hoe",
            "minecraft:recipe/stone_spear",
            "minecraft:recipe/wooden_pickaxe",
            "minecraft:recipe/wooden_shovel",
            "minecraft:recipe/wooden_sword",
            "minecraft:recipe/wooden_axe",
            "minecraft:recipe/wooden_hoe",
            "minecraft:recipe/wooden_spear"
    );

    public static void register() {
        if (CompatFlags.FD_LOADED) {
            if (SVO.CONFIG.modIntegration.svoFDAddon.get()) MixsonHelper.registerSingleJson(
                    "Remove cake recipe",
                    "minecraft:recipe/cake",
                    context -> context.markForDeletion(true)
            );
            if (SVO.CONFIG.earlyGameExtension.get()) MixsonHelper.registerSingleJson(
                    "Modify cooking pot recipe",
                    "farmersdelight:recipe/cooking_pot",
                    context -> context.getFile().getAsJsonObject()
                            .getAsJsonObject("key")
                            .addProperty("S", "#minecraft:shovels")
            );
        }
        if (CompatFlags.HORSEMAN_LOADED) {
            MixsonHelper.registerMultiJson(
                    "Remove copper horn recipes",
                    index -> index.id().toString().startsWith("thecopperierage:recipe/") && index.id().toString().endsWith("copper_horn"),
                    context -> context.markForDeletion(true)
            );
        }
        if (SVO.CONFIG.earlyGameExtension.get()) EARLY_GAME_DISABLED_TOOLS.forEach(s -> MixsonHelper.registerSingleJson(
                "Remove recipes",
                s,
                context -> context.markForDeletion(true)
        ));
    }
}
