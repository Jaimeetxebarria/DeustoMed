package org.deustomed.gsonutils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class GsonUtils {
    public static String getStringOrNull(JsonObject jsonObject, String key) {
        JsonElement jsonElement = jsonObject.get(key);
        if (jsonElement == null || jsonElement.isJsonNull()) return null;
        return jsonElement.getAsString();
    }
}
