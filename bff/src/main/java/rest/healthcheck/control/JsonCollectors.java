package rest.healthcheck.control;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.Map;
import java.util.stream.Collector;

public class JsonCollectors {
    public static <T> Collector<Map.Entry<T, T>, ?, JsonObjectBuilder> toJsonBuilder() {
        return Collector.of(Json::createObjectBuilder, (t, u) -> {
            t.add(String.valueOf(String.valueOf(u.getKey())), String.valueOf(u.getValue()));
        }, JsonCollectors::merge);
    }

    static JsonObjectBuilder merge(JsonObjectBuilder left, JsonObjectBuilder right) {
        JsonObjectBuilder retVal = Json.createObjectBuilder();
        JsonObject leftObject = left.build();
        JsonObject rightObject = right.build();
        leftObject.keySet().stream().forEach((key) -> retVal.add(key, leftObject.get(key)));
        rightObject.keySet().stream().forEach((key) -> retVal.add(key, rightObject.get(key)));
        return retVal;
    }
}
