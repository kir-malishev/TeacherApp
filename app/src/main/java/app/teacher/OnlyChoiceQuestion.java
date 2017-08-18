package app.teacher;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * Created by Кирилл on 01.07.2017.
 */
public class OnlyChoiceQuestion extends Challenge{

    ArrayList<String> answers;
    String rightAnswer;
    static final int TYPE = 0;

    OnlyChoiceQuestion(){
        super(TYPE);
        this.answers = new ArrayList<String>();
        this.rightAnswer = "";
    }

    OnlyChoiceQuestion(String question){
        super(TYPE, question);
        this.answers = new ArrayList<String>();
        this.rightAnswer = "";

    }

    OnlyChoiceQuestion(String question, ArrayList<String> answers){
        super(TYPE, question);
        this.answers = answers;
        this.rightAnswer = "";
    }

    OnlyChoiceQuestion(String question, ArrayList<String> answers, String rightAnswer){
        super(TYPE, question);
        this.answers = answers;
        this.rightAnswer = rightAnswer;
    }


    void swap(int firstPos, int secondPos){
        Collections.swap(answers, firstPos, secondPos);
    }

    void remove(int position){
        answers.remove(position);
    }
    void setRightAnswer(String rightAnswer){
        this.rightAnswer = rightAnswer;
    }

    boolean checkAnswer(String answer){
        return this.rightAnswer.equals(answer);
    }

    int size(){
        return answers.size();
    }

    String getRightAnswers(){
        return rightAnswer;
    }

    String toJSON(){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(this);
    }


}
