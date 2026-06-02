package me.pajic.svocore.iris;

import me.pajic.svocore.SVOClient;
import me.pajic.svocore.config.ModClientConfigHolder;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// sets up shader presets for use in SVO when shader config files are provided in a correct format
// inside the `shaderpacks/svo_presets` directory and a shader file is present whose name contains
// the target shader string configured in the client config
public class ShaderPresetSetup {

    private static final Path SHADER_DIR = FabricLoader.getInstance().getGameDir().resolve("shaderpacks");
    private static final Path SHADER_PRESETS_DIR = SHADER_DIR.resolve("svo_presets");
    private static final Path EP_VERSION_PATH = SHADER_DIR.resolve("svo_ep_ver");
    public static @Nullable String[] presets = null;
    public static boolean allPresetsCreated = false;

    @SuppressWarnings("DataFlowIssue") // listFiles() is always called on files that are guaranteed to be directories
    public static void init() {
        File shaderDir = SHADER_DIR.toFile();
        File shaderPresetsDir = SHADER_PRESETS_DIR.toFile();
        SemanticVersion epVersion = null;
        if (shaderDir.exists() && shaderPresetsDir.exists() && shaderDir.isDirectory() && shaderPresetsDir.isDirectory()) {
            // check if the preset files are correctly named and add them
            presets = new String[shaderPresetsDir.listFiles().length];
            try (Stream<Path> fileStream = Files.walk(SHADER_PRESETS_DIR, 1)) {
                fileStream.filter(path -> !path.toFile().getName().equals("svo_presets"))
                        .map(path -> path.toFile().getName().split("_", 2))
                        .forEach(split -> {
                    try {
                        presets[Integer.parseInt(split[0])] = split[1].split("\\.")[0];
                    } catch (NumberFormatException e) {
                        SVOClient.LOGGER.error("Invalid preset file name format, skipping", e);
                    }
                });
            } catch (IOException e) {
                SVOClient.LOGGER.error("Can't access file, did not create shader presets", e);
            }
            // if all presets are present, move on
            if (Arrays.stream(presets).noneMatch(Objects::isNull)) {
                // check if presets were generated previously and if yes get the EP version
                try (Stream<String> fileStream = Files.lines(EP_VERSION_PATH)) {
                    Optional<String> opt = fileStream.findFirst();
                    if (opt.isPresent()) {
                        epVersion = SemanticVersion.parse(opt.get());
                        SVOClient.LOGGER.info("Found existing presets for EP {}", epVersion.getFriendlyString());
                    }
                } catch (IOException | VersionParsingException e) {
                    SVOClient.LOGGER.info("No existing presets found");
                }
                // scan shader directory for EP shader files and compare versions
                File targetShader = null;
                try (Stream<Path> fileStream = Files.walk(SHADER_DIR, 1)) {
                    Set<File> files = fileStream.map(Path::toFile).collect(Collectors.toUnmodifiableSet());
                    for (File shader : files) {
                        String target = ModClientConfigHolder.options().targetShaderString;
                        String name = shader.getName();
                        if (!target.isBlank() && name.contains(target) && !name.endsWith(".txt")) {
                            try {
                                int beginIndex = name.lastIndexOf(target) + target.length();
                                SemanticVersion version = shader.isDirectory() ?
                                        SemanticVersion.parse(name.substring(beginIndex)) :
                                        SemanticVersion.parse(name.substring(beginIndex, name.lastIndexOf(".")));
                                if (epVersion == null || version.compareTo((Version) epVersion) > 0) {
                                    epVersion = version;
                                    targetShader = shader;
                                }
                            } catch (VersionParsingException e) {
                                SVOClient.LOGGER.info("Couldn't parse version for {}, skipping", name);
                            }
                        }
                    }
                } catch (IOException e) {
                    SVOClient.LOGGER.error("Can't access file, did not create shader presets", e);
                }
                // if no existing presets are found or a newer EP version is found, update shader presets
                if (epVersion != null && targetShader != null) {
                    SVOClient.LOGGER.info("Updating SVO shader presets to EP {}", epVersion.getFriendlyString());
                    for (int i = 0; i < presets.length; i++) {
                        String preset = presets[i];
                        File shaderCopy = SHADER_DIR.resolve("SVO_" + preset + ".zip").toFile();
                        try {
                            if (shaderCopy.exists() && shaderCopy.isDirectory()) FileUtils.deleteDirectory(shaderCopy);
                            if (targetShader.isDirectory()) FileUtils.copyDirectory(targetShader, shaderCopy);
                            else FileUtils.copyFile(targetShader, shaderCopy);
                            FileUtils.copyFile(
                                    SHADER_PRESETS_DIR.resolve(i + "_" + preset + ".txt").toFile(),
                                    SHADER_DIR.resolve("SVO_" + preset + ".zip.txt").toFile()
                            );
                            SVOClient.LOGGER.info("Created shader preset {}", preset);
                        } catch (IOException e) {
                            SVOClient.LOGGER.error("Couldn't copy preset {}, skipping", preset, e);
                        }
                    }
                }
            } else {
                SVOClient.LOGGER.warn("Shader preset file name indices not in order, did not create shader presets");
            }
            // check if all presets got created
            if (
                    presets != null && Arrays.stream(presets).noneMatch(Objects::isNull) &&
                    Arrays.stream(presets).allMatch(preset -> Arrays.stream(shaderDir.listFiles()).anyMatch(file -> file.getName().equals("SVO_" + preset + ".zip.txt")))
            ) {
                // let the Sodium config option know that shader presets can be used
                SVOClient.LOGGER.info("Shader presets ready");
                allPresetsCreated = true;
                // write SVO EP preset version info to file
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(EP_VERSION_PATH.toFile()))) {
                    writer.write(epVersion.getFriendlyString());
                } catch (IOException e) {
                    SVOClient.LOGGER.error("Couldn't write version info (how), skipping", e);
                }
            }
        }
    }
}
