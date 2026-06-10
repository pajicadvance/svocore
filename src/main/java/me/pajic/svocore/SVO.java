package me.pajic.svocore;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.pajic.svocore.config.ModConfig;
import me.pajic.svocore.datapack.ModDatapacks;
import me.pajic.svocore.item.ModItems;
import me.pajic.svocore.mixson.MixsonInitializer;
import me.pajic.svocore.recipe.ModRecipes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SVO implements ModInitializer {
    public static final String MOD_ID = /*$ mod_id*/ "svocore";
    public static final Logger LOGGER = LoggerFactory.getLogger("SVO Core");
    public static final boolean DEBUG = FabricLoader.getInstance().isDevelopmentEnvironment();
    public static ModConfig CONFIG = ConfigApiJava.registerAndLoadConfig(ModConfig::new);

    @Override
    public void onInitialize() {
        MixsonInitializer.init();
        ModItems.init();
        ModRecipes.init();
        ModDatapacks.init();
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public static void debugLog(String message, Object ... args) {
        if (DEBUG || CONFIG.debug.logDebugMessages.get()) LOGGER.info(message, args);
    }
}