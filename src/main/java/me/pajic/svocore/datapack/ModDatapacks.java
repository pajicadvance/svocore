package me.pajic.svocore.datapack;

import me.pajic.svocore.CompatFlags;
import me.pajic.svocore.SVO;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.network.chat.Component;

public class ModDatapacks {

    private static final ModContainer CONTAINER = FabricLoader.getInstance().getModContainer(SVO.MOD_ID).orElseThrow();

    public static void init() {
        if (SVO.CONFIG.earlyGameExtension.get()) {
            registerPersistentPack("early_game_extension/vanilla", "Early game extension");
            if (CompatFlags.WILDER_WILD_LOADED) registerPersistentPack("early_game_extension/wilderwild", "Early game extension + Wilder Wild");
        }
        if (SVO.CONFIG.recipeChanges.get())
            registerPersistentPack("recipe_changes", "Recipe changes");
        if (CompatFlags.FD_LOADED && SVO.CONFIG.modIntegration.svoFDAddon.get())
            registerPersistentPack("fd_integration", "Farmer's Delight SVO integration");
        if (CompatFlags.MASTERCUTTER_LOADED && CompatFlags.WILDER_WILD_LOADED)
            registerPersistentPack("wwcutter", "Mastercutter recipes for Wilder Wild");
    }

    private static void registerPersistentPack(String id, String name) {
        ResourceLoader.registerBuiltinPack(SVO.id(id), CONTAINER, Component.literal(name), PackActivationType.ALWAYS_ENABLED);
    }
}
