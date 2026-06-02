package me.pajic.svocore.mixson.events;

import me.pajic.svocore.SVO;
import me.pajic.svocore.config.ModClientConfigHolder;
import me.pajic.svocore.mixson.MixsonHelper;

public class AssetEvents {

    public static void register() {
        MixsonHelper.registerMultiJson(
                "Disable BetterGrass layer feature",
                index -> index.id().getPath().startsWith("bettergrass/layer_types/"),
                context -> {
                    if (ModClientConfigHolder.options().disableLBGLayers) context.markForDeletion(true);
                }
        );
        MixsonHelper.registerMultiText(
                "Cringe Modded Splash Text Obliterator",
                index -> !index.id().getNamespace().equals("minecraft") && index.id().getPath().contains("texts/splashes"),
                context -> {
                    if (ModClientConfigHolder.options().deleteModdedSplashTexts) {
                        SVO.debugLog("Removing splash texts from mod {}", context.getIndex().id().getNamespace());
                        context.setFile("");
                    }
                }
        );
    }
}
