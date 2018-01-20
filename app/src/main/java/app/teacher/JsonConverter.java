package app.teacher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Кирилл on 13.11.2017.
 */
public class JsonConverter implements JsonDeserializer<ArrayList<String>> {


    // Указываем правила, по которым преобразуем json в нужный нам объект.
    @Override
    public ArrayList<String> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray array = json.getAsJsonArray();
        ArrayList<String> listOfUriStrings = new ArrayList<String>();
        for (JsonElement element : array) {
            String uriString = element.getAsJsonObject().get("uriString").getAsString();
            listOfUriStrings.add(uriString);
        }
        return listOfUriStrings;
    }

    // Получаем из json-строки список
    public ArrayList<String> getListFromJSON(String json) {
        GsonBuilder builder = new GsonBuilder();
        // import java.lang.reflect.Type;
        Type listType = new TypeToken<ArrayList<String>>() {
        }.getType();
        builder.registerTypeAdapter(listType, new JsonConverter());
        final Gson gson = builder.create();
        return gson.fromJson(json, listType);
    }
}