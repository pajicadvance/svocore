package me.pajic.svocore.iris;

import me.pajic.svocore.SVOClient;
import net.irisshaders.iris.Iris;

import java.io.IOException;
import java.util.Optional;

public class IrisHelper {

    public static void applyPreset(int preset) {
        if (preset == -1) Iris.getIrisConfig().setShadersEnabled(false);
        else {
            Iris.getIrisConfig().setShaderPackName("SVO_" + ShaderPresetSetup.presets[preset] + ".zip");
            Iris.getIrisConfig().setShadersEnabled(true);
        }
        try {
            Iris.getIrisConfig().save();
            Iris.reload();
        } catch (IOException e) {
            SVOClient.LOGGER.error("Error while reloading Shaders for Iris!", e);
        }
    }

    public static int updatePreset(int preset) {
        if (preset != -1) {
            Optional<String> opt = Iris.getIrisConfig().getShaderPackName();
            if (opt.isPresent() && !opt.get().startsWith("SVO_")) return -1;
            return preset;
        }
        return -1;
    }
}
