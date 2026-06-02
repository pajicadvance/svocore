package me.pajic.svocore.config;

import me.pajic.svocore.iris.IrisHelper;
import me.pajic.svocore.iris.ShaderPresetSetup;
import net.caffeinemc.mods.sodium.api.config.ConfigEntryPoint;
import net.caffeinemc.mods.sodium.api.config.option.OptionFlag;
import net.caffeinemc.mods.sodium.api.config.structure.ConfigBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.OptionBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.Locale;

public class ModSodiumConfig implements ConfigEntryPoint {

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Override
    public void registerConfigLate(ConfigBuilder builder) {
        builder.registerOwnModOptions()
                .setName("!Simple Vanilla Overhaul")
                .setVersion(FabricLoader.getInstance().getModContainer("svocore").get().getMetadata().getVersion().getFriendlyString().substring(0, 4))
                .setColorTheme(builder.createColorTheme().setBaseThemeRGB(0xD0C5C0))
                .setNonTintedIcon(Identifier.parse("svocore:textures/config_icon.png"))
                .addPage(builder.createOptionPage()
                        .setName(Component.translatable("config.svo.settings"))
                        .addOption(setupShaderPresetOption(builder))
                        .addOption(builder.createBooleanOption(Identifier.parse("svocore:disable_lbg_layers"))
                                .setName(Component.translatable("config.svo.disableLBGLayers"))
                                .setTooltip(Component.translatable("config.svo.disableLBGLayers.tooltip"))
                                .setDefaultValue(false)
                                .setBinding(bl -> ModClientConfigHolder.options().disableLBGLayers = bl, () -> ModClientConfigHolder.options().disableLBGLayers)
                                .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD)
                                .setStorageHandler(() -> ModClientConfigHolder.options().writeChanges())
                        )
                );
    }

    @SuppressWarnings("DataFlowIssue")
    private static OptionBuilder setupShaderPresetOption(ConfigBuilder builder) {
        return ShaderPresetSetup.allPresetsCreated ?
                builder.createIntegerOption(Identifier.parse("svocore:shader_preset"))
                    .setName(Component.translatable("config.svo.shaderPreset"))
                    .setTooltip(Component.translatable("config.svo.shaderPreset.tooltip"))
                    .setRange(-1, ShaderPresetSetup.presets.length - 1, 1)
                    .setDefaultValue(-1)
                    .setValueFormatter(i -> {
                        if (i == -1) return Component.translatable("config.svo.shaderOff");
                        return Component.translatable("config.svo.shaderPreset." + ShaderPresetSetup.presets[i].toLowerCase(Locale.ROOT).replace(' ', '_'));
                    })
                    .setBinding(ModSodiumConfig::saveShaderPreset, ModSodiumConfig::getShaderPreset)
                    .setStorageHandler(() -> ModClientConfigHolder.options().writeChanges()) :
                builder.createBooleanOption(Identifier.parse("svocore:shader_preset_unavailable"))
                    .setName(Component.translatable("config.svo.shaderPreset"))
                    .setTooltip(Component.translatable("config.svo.shaderPresetUnavailable.tooltip"))
                    .setEnabled(false)
                    .setDefaultValue(false)
                    .setBinding(_ -> {}, () -> false)
                    .setStorageHandler(() -> {});
    }

    private static int getShaderPreset() {
        return IrisHelper.updatePreset(ModClientConfigHolder.options().shaderPreset);
    }

    private static void saveShaderPreset(int preset) {
        ModClientConfigHolder.options().shaderPreset = preset;
        IrisHelper.applyPreset(preset);
    }
}
