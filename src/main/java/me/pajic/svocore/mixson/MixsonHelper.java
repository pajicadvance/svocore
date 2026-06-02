package me.pajic.svocore.mixson;

import com.google.gson.JsonElement;
import me.pajic.svocore.SVO;
import net.minecraft.server.packs.resources.Resource;
import net.ramixin.mixson.Mixson;
import net.ramixin.mixson.MixsonCodecs;
import net.ramixin.mixson.enums.ErrorPolicy;
import net.ramixin.mixson.enums.Lifetime;
import net.ramixin.mixson.util.Index;
import net.ramixin.mixson.util.functions.Event;
import net.ramixin.mixson.util.interfaces.MixsonCodec;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.function.Predicate;

public class MixsonHelper {

    private static final ErrorPolicy ERROR_POLICY = SVO.DEBUG ? ErrorPolicy.THROW : ErrorPolicy.LOG;

    public static final MixsonCodec<String> TEXT = new MixsonCodec<>() {
        public String extensionAndDot() {
            return ".txt";
        }

        @Override
        public String deserialize(Resource r) throws IOException {
            String contents;
            try (BufferedReader reader = r.openAsReader()) {
                contents = reader.readAllAsString();
            }
            return contents;
        }

        @Override
        public Resource serialize(Resource r, String s) {
            return new Resource(r.source(), () -> new ByteArrayInputStream(s.getBytes()), r::metadata);
        }

        @Override
        public ByteArrayOutputStream export(String s) throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(s.getBytes());
            return baos;
        }
    };

    public static void registerSingleJson(String eventName, String target, Event<JsonElement> event) {
        Mixson.registerEvent(
                MixsonCodecs.JSON_ELEMENT,
                Mixson.DEFAULT_PRIORITY,
                Lifetime.PERSISTENT,
                ERROR_POLICY,
                eventName,
                index -> index.idEquals(new Index(target)),
                event
        );
    }

    public static void registerMultiJson(String eventName, Predicate<Index> resourcePredicate, Event<JsonElement> event) {
        Mixson.registerEvent(
                MixsonCodecs.JSON_ELEMENT,
                Mixson.DEFAULT_PRIORITY,
                Lifetime.PERSISTENT,
                ERROR_POLICY,
                eventName,
                resourcePredicate,
                event
        );
    }

    public static void registerMultiText(String eventName, Predicate<Index> resourcePredicate, Event<String> event) {
        Mixson.registerEvent(
                TEXT,
                Mixson.DEFAULT_PRIORITY,
                Lifetime.PERSISTENT,
                ERROR_POLICY,
                eventName,
                resourcePredicate,
                event
        );
    }
}
