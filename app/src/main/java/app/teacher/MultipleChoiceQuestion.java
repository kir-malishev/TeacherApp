package app.teacher;

import java.util.ArrayList;

/**
 * Created by Кирилл on 01.07.2017.
 */
public class MultipleChoiceQuestion extends Challenge {

    ArrayList<String> answers;
    ArrayList<String> rightAnswer;
    static final int TYPE = Challenge.MULTIPLE_CHOICE_QUESTION_TYPE;


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

    void setAnswers(ArrayList<String> answers){
        this.answers = answers;
    }

    void addAnswer(String answer){
        answers.add(answer);
    }

    void addAnswer(String answer, boolean isRight){
        answers.add(answer);
        if(isRight) rightAnswer.add(answer);
    }


    boolean checkAnswer(ArrayList<String> answer){
        return this.rightAnswer.equals(answer);
    }

    boolean isContainsToRight(String answer){
        return rightAnswer.contains(answer);
    }

    ArrayList<String> getRightAnswer(){
        return this.rightAnswer;
    }

    ArrayList<String> getAnswers(){
        return this.answers;
    }

    int size(){
        return answers.size();
    }
    void clear(){
        answers = new ArrayList<String>();
        rightAnswer = new ArrayList<String>();
        question = "";
        points = 1;
    }

    @Override
    boolean check() {
        return super.check() && answers.size() >= 2 && !rightAnswer.isEmpty();
    }
}
