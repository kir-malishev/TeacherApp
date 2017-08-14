package app.teacher;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Кирилл on 01.07.2017.
 */
public class MultipleChoiceQuestion extends ChoiceQuestion implements Parcelable {

    HashSet<String> rightAnswer;
    MultipleChoiceQuestion(){
        super();
    }

    MultipleChoiceQuestion(String question){
        super(question);

    }

    MultipleChoiceQuestion(String question, HashSet<String> answers){
        super(question, answers);
    }

    MultipleChoiceQuestion(String question, HashSet<String> answers, HashSet<String> rightAnswer){
        super(question, answers);
        this.rightAnswer = rightAnswer;
    }


    protected MultipleChoiceQuestion(Parcel in) {
        super(in);
        ArrayList<String> data = new ArrayList<String>();
        in.readStringList(data);
        rightAnswer = new HashSet(data);
    }


    public static final Creator<MultipleChoiceQuestion> CREATOR = new Creator<MultipleChoiceQuestion>() {
        @Override
        public MultipleChoiceQuestion createFromParcel(Parcel in) {
            return new MultipleChoiceQuestion(in);
        }

        @Override
        public MultipleChoiceQuestion[] newArray(int size) {
            return new MultipleChoiceQuestion[size];
        }
    };

    void setRightAnswer(HashSet<String> rightAnswer){
        this.rightAnswer = rightAnswer;
    }

    boolean checkAnswer(HashSet<String> answer){
        return this.rightAnswer.equals(answer);
    }

    HashSet<String> getRightAnswers(){
        return rightAnswer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeStringList(new ArrayList(rightAnswer));
    }
}
