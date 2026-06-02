package me.pajic.svocore;

import net.fabricmc.loader.api.FabricLoader;

public class CompatFlags {
    public static final boolean IRIS_LOADED = FabricLoader.getInstance().isModLoaded("iris");
    public static final boolean FD_LOADED = FabricLoader.getInstance().isModLoaded("farmersdelight");
    public static final boolean WILDER_WILD_LOADED = FabricLoader.getInstance().isModLoaded("wilderwild");
    public static final boolean MASTERCUTTER_LOADED = FabricLoader.getInstance().isModLoaded("mr_mastercutter");
    public static final boolean HORSEMAN_LOADED = FabricLoader.getInstance().isModLoaded("horseman");
}
