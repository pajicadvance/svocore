package me.pajic.svocore.mixson;

import me.pajic.svocore.mixson.events.AssetEvents;
import me.pajic.svocore.mixson.events.LanguageEvents;

public class MixsonClientInitializer {

    public static void init() {
        LanguageEvents.register();
        AssetEvents.register();
    }
}
