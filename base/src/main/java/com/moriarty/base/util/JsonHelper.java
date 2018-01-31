package com.moriarty.base.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;

public class JsonHelper {


    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        JsonDeserializer<Boolean> booleanDeserializer = new JsonDeserializer<Boolean>() {
            @Override
            public Boolean deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                    throws JsonParseException {
                if (typeOfT != boolean.class && typeOfT != Boolean.class) {
                    throw new IllegalArgumentException(getClass() + " cannot deserialize to " + typeOfT);
                }

                String value = json.getAsString();
                if ("true".equals(value) || "1".equals(value)) {
                    return true;
                } else if ("false".equals(value) || "0".equals(value)) {
                    return false;
                }
                return false;
            }
        };
        gsonBuilder.registerTypeAdapter(boolean.class, booleanDeserializer);
        gsonBuilder.registerTypeAdapter(Boolean.class, booleanDeserializer);
        return gsonBuilder.create();
    }


    public <T> T fromJson(Class<T> clazz, String jsonStr) throws IOException {
        Gson gson = getGson();
        TypeAdapter<T> adapter = gson.getAdapter(TypeToken.get(clazz));
        return adapter.fromJson(jsonStr);
    }
}
