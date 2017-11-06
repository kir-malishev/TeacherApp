package app.teacher;

/**
 * Created by Кирилл on 01.07.2017.
 */
public class Challenge {

    /**CHALLENGE                         TYPE
     *
     *
     * Unknown                             -1
     *
     * OnlyChoiceQuestion                   0
     * MultipleChoiceQuestion               1
     * InputQuestion                        2
     */


    String question = "";
    int points = 1;
    int type = -1;


    Challenge(int type){
        this.type = type;
    }

    Challenge(int type, String question){
        this.type = type;
        this.question = question;
    }

    Challenge(int type, int points){
        this.type = type;
        this.points = points;
    }

    Challenge(int type, String question, int points){
        this.type = type;
        this.question = question;
        this.points = points;
    }

    void setQuestion(String question){
        this.question = question;
    }

    void setPoints(int points){
        this.points = points;
    }

    String getQuestion(){
        return question;
    }

    int getType(){
        return type;
    }

    int getPoints(){
        return points;
    }

    boolean check() {
        return !question.isEmpty();
    }


}
