package app.teacher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by Кирилл on 15.08.2017.
 */
public class TestConverter implements JsonSerializer<Test>, JsonDeserializer<Test> {

    @Override
    public JsonElement serialize(Test src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("test_name", src.getName());
        json.addProperty("test_id", src.getTestId());
        final GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Challenge.class, new ChallengeConverter());
        final Gson gson = builder.create();
        json.add("challenges", gson.toJsonTree(src.getAllChallenges()).getAsJsonArray());
        return json;
    }

    @Override
    public Test deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        String testName = object.get("test_name").getAsString();
        String testId = object.get("test_id").getAsString();
        Test test = new Test(testName, testId);
        for(JsonElement jsonChallenge: object.get("challenges").getAsJsonArray()){
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Challenge.class, new ChallengeConverter());
            final Gson gson = builder.create();
            Challenge challenge = gson.fromJson(jsonChallenge, Challenge.class);
            test.addChallenge(challenge);
        }
        return test;
    }
}
