package me.pajic.svocore.mixson;

import me.pajic.svocore.SVO;
import me.pajic.svocore.mixson.events.RecipeEvents;
import me.pajic.svocore.mixson.events.TagEvents;
import me.pajic.svocore.mixson.events.WorldgenDataEvents;
import net.ramixin.mixson.Mixson;
import net.ramixin.mixson.enums.DebugOption;

public class MixsonInitializer {

    public static void init() {
        if (SVO.DEBUG || SVO.CONFIG.debug.exportJsonPatches.get()) {
            Mixson.enableDebugOption(DebugOption.BASIC_LOGGING);
            Mixson.enableDebugOption(DebugOption.EXTRA_LOGGING);
            Mixson.enableDebugOption(DebugOption.EXPORT_PATCHED_FILE);
        }

        RecipeEvents.register();
        TagEvents.register();
        WorldgenDataEvents.register();
    }
}
