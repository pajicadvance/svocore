package me.pajic.svocore.config;

import me.fzzyhmstrs.fzzy_config.annotations.Version;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import me.pajic.svocore.SVO;

@Version(version = 1)
public class ModConfig extends Config {
    public ModConfig() {
        super(SVO.id("config"));
    }

    public ValidatedBoolean earlyGameExtension = new ValidatedBoolean(false);
    public ValidatedBoolean cliffFixes = new ValidatedBoolean(false);
    public ValidatedBoolean recipeChanges = new ValidatedBoolean(false);
    public ModIntegration modIntegration = new ModIntegration();
    public Misc misc = new Misc();
    public Debug debug = new Debug();

    public static class Misc extends ConfigSection {
        public ValidatedInt cakeSliceFoodLevel = new ValidatedInt(2);
        public ValidatedFloat cakeSliceSaturationLevel = new ValidatedFloat(0.1F);
        public ValidatedInt caveAmbientSoundFrequency = new ValidatedInt(6000, Integer.MAX_VALUE, 0);
        public ValidatedBoolean hoglinSpawnOnlyOnCrimsonNylium = new ValidatedBoolean(false);
        public ValidatedBoolean alwaysUpdateMaps = new ValidatedBoolean(false);
        public ValidatedBoolean frostWalkerEarlyTrigger = new ValidatedBoolean(false);
        public ValidatedBoolean fixOcelotSpawn = new ValidatedBoolean(false);
        public ValidatedInt coffinActivationRange = new ValidatedInt(48, 48, 0);
        public ValidatedBoolean repairableReliableGlider = new ValidatedBoolean(false);
        public ValidatedInt revaultTime = new ValidatedInt(720000);
        public ValidatedInt ominousRevaultTime = new ValidatedInt(864000);
    }

    public static class ModIntegration extends ConfigSection {
        public ValidatedBoolean svoFDAddon = new ValidatedBoolean(false);
    }

    public static class Debug extends ConfigSection {
        public ValidatedBoolean logDebugMessages = new ValidatedBoolean(false);
        public ValidatedBoolean exportJsonPatches = new ValidatedBoolean(false);
    }
}