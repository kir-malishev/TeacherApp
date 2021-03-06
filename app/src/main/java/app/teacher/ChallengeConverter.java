package app.teacher;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Кирилл on 15.08.2017.
 */
public class ChallengeConverter implements JsonSerializer<Challenge>, JsonDeserializer<Challenge> {

    @Override
    public JsonElement serialize(Challenge src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("type", src.getType());
        json.addProperty("question", src.getQuestion());
        json.addProperty("points", src.getPoints());
        JsonArray jsonAnswers = new JsonArray();
        JsonArray jsonRightAnswer = new JsonArray();
        switch(src.type){
            case Challenge.ONLY_CHOICE_QUESTION_TYPE:
                for(String answer: ((OnlyChoiceQuestion) src).answers){
                    jsonAnswers.add(answer);
                }
                json.add("answers", jsonAnswers);
                json.addProperty("rightAnswer", ((OnlyChoiceQuestion) src).getRightAnswer());
                break;
            case Challenge.MULTIPLE_CHOICE_QUESTION_TYPE:
                for(String answer: ((MultipleChoiceQuestion) src).answers){
                    jsonAnswers.add(answer);
                }
                json.add("answers", jsonAnswers);
                for(String rightAnswer: ((MultipleChoiceQuestion) src).getRightAnswer()){
                    jsonAnswers.add(rightAnswer);
                }
                json.add("rightAnswer", jsonRightAnswer);
                break;
            case Challenge.INPUT_QUESTION:
                for(String rightAnswer: ((InputQuestion) src).getRightAnswer()){
                    jsonAnswers.add(rightAnswer);
                }
                json.add("rightAnswer", jsonRightAnswer);
                break;
        }
        return json;
    }

    @Override
    public Challenge deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        String question = object.get("question").getAsString();
        int points = object.get("points").getAsInt();
        int type = object.get("type").getAsInt();
        Challenge challenge = null;
        ArrayList<String> answers = new ArrayList<String>();
        switch(type){
            case Challenge.UNKNOWN_TYPE:
                challenge = new Challenge(type, question, points);
                break;
            case Challenge.ONLY_CHOICE_QUESTION_TYPE:
                for(JsonElement answer: object.get("answers").getAsJsonArray()){
                    answers.add(answer.getAsString());
                }
                String rightAnswerType0 = object.get("rightAnswer").getAsString();
                challenge = new OnlyChoiceQuestion(question, answers, rightAnswerType0, points);
                break;
            case Challenge.MULTIPLE_CHOICE_QUESTION_TYPE:
                for(JsonElement answer: object.get("answers").getAsJsonArray()){
                    answers.add(answer.getAsString());
                }
                ArrayList<String> rightAnswerType1 = new ArrayList<String>();
                for(JsonElement answer: object.get("rightAnswer").getAsJsonArray()){
                    rightAnswerType1.add(answer.getAsString());
                }
                challenge = new MultipleChoiceQuestion(question, answers, rightAnswerType1, points);
                break;
            case Challenge.INPUT_QUESTION:
                ArrayList<String> rightAnswerType2 = new ArrayList<String>();
                for(JsonElement answer: object.get("rightAnswer").getAsJsonArray()){
                    rightAnswerType2.add(answer.getAsString());
                }
                challenge = new InputQuestion(question, rightAnswerType2, points);
                break;
        }

        return challenge;


    }
}
