package app.teacher;

import java.util.ArrayList;

/**
 * Created by Кирилл on 01.07.2017.
 */
public class InputQuestion extends Challenge {

    ArrayList<String> rightAnswer;
    static final int TYPE = 2;


    InputQuestion(){
        super(TYPE);
        this.rightAnswer = new ArrayList<String>();
    }

    InputQuestion(int points){
        super(TYPE, points);
        this.rightAnswer = new ArrayList<String>();
    }

    InputQuestion(String question){
        super(TYPE, question);
        this.rightAnswer = new ArrayList<String>();
    }

    InputQuestion(String question,int  points){
        super(TYPE, question, points);
        this.rightAnswer = new ArrayList<String>();
    }

    InputQuestion(String question, ArrayList<String> rightAnswer){
        super(TYPE, question);
        this.rightAnswer = rightAnswer;
    }

    InputQuestion(String question, ArrayList<String> rightAnswer, int points){
        super(TYPE, question, points);
        this.rightAnswer = rightAnswer;
    }

    void setRightAnswer(ArrayList<String> rightAnswer){
        this.rightAnswer = rightAnswer;
    }

    ArrayList<String> getRightAnswer(){
        return rightAnswer;
    }

    void addRightAnswer(String answer){
        rightAnswer.add(answer);
    }

    int numberOfRightAnswers(){
        return rightAnswer.size();
    }

    void clear(){
        rightAnswer = new ArrayList<String>();
        question = "";
        points = 1;
    }
}
