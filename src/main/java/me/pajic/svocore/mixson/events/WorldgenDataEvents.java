package me.pajic.svocore.mixson.events;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import me.pajic.svocore.SVO;
import me.pajic.svocore.mixson.MixsonHelper;

public class WorldgenDataEvents {

    public static void register() {
        MixsonHelper.registerSingleJson(
                "Fix ocelots being counted as monsters during spawning",
                "minecraft:worldgen/biome/jungle",
                context -> {
                    if (SVO.CONFIG.misc.fixOcelotSpawn.get()) {
                        JsonArray monsters = context.getFile().getAsJsonObject()
                                .getAsJsonObject("spawners")
                                .getAsJsonArray("monster");
                        JsonArray creatures = context.getFile().getAsJsonObject()
                                .getAsJsonObject("spawners")
                                .getAsJsonArray("creature");
                        int idToRemove = -1;
                        JsonObject ocelot = new JsonObject();
                        for (int i = 0; i < monsters.size(); i++) {
                            JsonObject monster = monsters.get(i).getAsJsonObject();
                            if (monster.get("type").getAsString().equals("minecraft:ocelot")) {
                                idToRemove = i;
                                ocelot.addProperty("type", "minecraft:ocelot");
                                ocelot.addProperty("maxCount", monster.get("maxCount").getAsInt());
                                ocelot.addProperty("minCount", monster.get("minCount").getAsInt());
                                ocelot.addProperty("weight", monster.get("weight").getAsInt());
                                break;
                            }
                        }
                        if (idToRemove != -1) monsters.remove(idToRemove);
                        if (!ocelot.isEmpty()) creatures.add(ocelot);
                    }
                }
        );
        MixsonHelper.registerSingleJson(
                "Patch Wilder Clifftree for latest Wilder Wild",
                "clifftree:worldgen/biome/lukewarm_caves",
                context -> {
                    JsonElement target1 = new JsonPrimitive("wilderwild:blue_mesoglea");
                    JsonElement target2 = new JsonPrimitive("wilderwild:purple_mesoglea");
                    JsonArray features = context.getFile().getAsJsonObject().getAsJsonArray("features");
                    features.forEach(e -> {
                        if (e.isJsonArray()) {
                            JsonArray array = e.getAsJsonArray();
                            array.remove(target1);
                            array.remove(target2);
                        }
                    });
                }
        );
        MixsonHelper.registerSingleJson(
                "Fix Wilder Clifftree feature cycle",
                "minecraft:worldgen/biome/snowy_taiga",
                context -> {
                    JsonElement first = new JsonPrimitive("wilderwild:maple_trees");
                    JsonElement second = new JsonPrimitive("minecraft:patch_large_fern");
                    JsonArray features = context.getFile().getAsJsonObject().getAsJsonArray("features");
                    features.forEach(e -> {
                        if (e.isJsonArray()) {
                            JsonArray array = e.getAsJsonArray();
                            int index1 = array.asList().indexOf(first);
                            int index2 = array.asList().indexOf(second);
                            if (index1 != -1 && index2 != -1 && index1 > index2) {
                                JsonElement temp = array.get(index1);
                                array.set(index1, array.get(index2));
                                array.set(index2, temp);
                            }
                        }
                    });
                }
        );
    }
}
