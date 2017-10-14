package app.teacher;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Кирилл on 01.07.2017.
 */
public class MultipleChoiceQuestion extends Challenge {

    ArrayList<String> answers;
    ArrayList<String> rightAnswer;
    static final int TYPE = 1;


    MultipleChoiceQuestion(){
        super(TYPE);
        this.answers = new ArrayList<String>();
        this.rightAnswer = new ArrayList<String>();
    }

    MultipleChoiceQuestion(int points){
        super(TYPE, points);
        this.answers = new ArrayList<String>();
        this.rightAnswer = new ArrayList<String>();
    }

    MultipleChoiceQuestion(String question){
        super(TYPE, question);
        this.answers = new ArrayList<String>();
        this.rightAnswer = new ArrayList<String>();
    }

    MultipleChoiceQuestion(String question, int points){
        super(TYPE, question, points);
        this.answers = new ArrayList<String>();
        this.rightAnswer = new ArrayList<String>();
    }

    MultipleChoiceQuestion(String question, ArrayList<String> answers){
        super(TYPE, question);
        this.answers = answers;
        this.rightAnswer = new ArrayList<String>();
    }

    MultipleChoiceQuestion(String question, ArrayList<String> answers, int points){
        super(TYPE, question, points);
        this.answers = answers;
        this.rightAnswer = new ArrayList<String>();
    }

    MultipleChoiceQuestion(String question, ArrayList<String> answers, ArrayList<String> rightAnswer){
        super(TYPE, question);
        this.answers = answers;
        this.rightAnswer = rightAnswer;
    }

    MultipleChoiceQuestion(String question, ArrayList<String> answers, ArrayList<String> rightAnswer, int points){
        super(TYPE, question, points);
        this.answers = answers;
        this.rightAnswer = rightAnswer;
    }


    void setRightAnswer(ArrayList<String> rightAnswer){
        this.rightAnswer = rightAnswer;
    }

    boolean checkAnswer(ArrayList<String> answer){
        return this.rightAnswer.equals(answer);
    }

    ArrayList<String> getRightAnswers(){
        return this.rightAnswer;
    }
}
