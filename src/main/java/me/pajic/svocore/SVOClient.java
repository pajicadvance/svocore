package me.pajic.svocore;

import me.pajic.svocore.iris.ShaderPresetSetup;
import me.pajic.svocore.mixson.MixsonClientInitializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SVOClient implements ClientModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("SVO Core Client");

    @Override
    public void onInitializeClient() {
        MixsonClientInitializer.init();
        ClientLifecycleEvents.CLIENT_STARTED.register(_ -> {
            if (CompatFlags.IRIS_LOADED) ShaderPresetSetup.init();
        });
    }
}
