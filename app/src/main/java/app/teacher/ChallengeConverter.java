package app.teacher;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import java.util.HashSet;

/**
 * Created by Кирилл on 15.08.2017.
 */
public class ChallengeConverter implements JsonSerializer<Challenge>, JsonDeserializer<Challenge> {

    @Override
    public JsonElement serialize(Challenge src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("type", src.type);
        json.addProperty("question", src.question);
        JsonArray jsonAnswers = new JsonArray();
        JsonArray jsonRightAnswer = new JsonArray();
        switch(src.type){
            case 0:
                for(String answer: ((ChoiceQuestion) src).answers){
                    jsonAnswers.add(answer);
                }
                json.add("answers", jsonAnswers);
                json.addProperty("right_answer", ((OnlyChoiceQuestion) src).rightAnswer);
                break;
            case 1:
                for(String answer: ((ChoiceQuestion) src).answers){
                    jsonAnswers.add(answer);
                }
                json.add("answers", jsonAnswers);
                for(String rightAnswer: ((MultipleChoiceQuestion) src).rightAnswer){
                    jsonAnswers.add(rightAnswer);
                }
                json.add("right_answer", jsonRightAnswer);
                break;
            case 2:
                for(String rightAnswer: ((InputQuestion) src).rightAnswer){
                    jsonAnswers.add(rightAnswer);
                }
                json.add("right_answer", jsonRightAnswer);
                break;
        }
        return json;
    }

    @Override
    public Challenge deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        String question = object.get("question").getAsString();
        int type = object.get("type").getAsInt();
        Challenge challenge = new Challenge();
        HashSet<String> answers = new HashSet<String>();
        switch(type){
            case -1:
                challenge = new Challenge(type, question);
                break;
            case 0:
                for(JsonElement answer: object.get("answers").getAsJsonArray()){
                    answers.add(answer.getAsString());
                }
                String rightAnswerType0 = object.get("right_answer").getAsString();
                challenge = new OnlyChoiceQuestion(question, answers, rightAnswerType0);
                break;
            case 1:
                for(JsonElement answer: object.get("answers").getAsJsonArray()){
                    answers.add(answer.getAsString());
                }
                HashSet<String> rightAnswerType1 = new HashSet<String>();
                for(JsonElement answer: object.get("right_answer").getAsJsonArray()){
                    rightAnswerType1.add(answer.getAsString());
                }
                challenge = new MultipleChoiceQuestion(question, answers, rightAnswerType1);
                break;
            case 2:
                HashSet<String> rightAnswerType2 = new HashSet<String>();
                for(JsonElement answer: object.get("right_answer").getAsJsonArray()){
                    rightAnswerType2.add(answer.getAsString());
                }
                challenge = new InputQuestion(question, rightAnswerType2);
                break;
        }

        return challenge;


    }
}
