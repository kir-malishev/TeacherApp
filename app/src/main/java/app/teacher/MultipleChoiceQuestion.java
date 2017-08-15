package app.teacher;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Кирилл on 01.07.2017.
 */
public class MultipleChoiceQuestion extends Challenge implements Parcelable {

    HashSet<String> answers;
    HashSet<String> rightAnswer;
    static final int TYPE = 1;


    MultipleChoiceQuestion(){
        super(TYPE);
    }

    MultipleChoiceQuestion(String question){
        super(TYPE, question);

    }

    MultipleChoiceQuestion(String question, HashSet<String> answers){
        super(TYPE, question);
        this.answers = answers;
    }

    MultipleChoiceQuestion(String question, HashSet<String> answers, HashSet<String> rightAnswer){
        super(TYPE, question);
        this.answers = answers;
        this.rightAnswer = rightAnswer;
    }


    void setRightAnswer(HashSet<String> rightAnswer){
        this.rightAnswer = rightAnswer;
    }

    boolean checkAnswer(HashSet<String> answer){
        return this.rightAnswer.equals(answer);
    }

    HashSet<String> getRightAnswers(){
        return this.rightAnswer;
    }


    protected MultipleChoiceQuestion(Parcel in) {
        super(in);
        this.answers = (HashSet) in.readValue(HashSet.class.getClassLoader());
        this.rightAnswer = (HashSet) in.readValue(HashSet.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeValue(this.answers);
        parcel.writeValue(this.rightAnswer);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MultipleChoiceQuestion> CREATOR = new Parcelable.Creator<MultipleChoiceQuestion>() {
        @Override
        public MultipleChoiceQuestion createFromParcel(Parcel in) {
            return new MultipleChoiceQuestion(in);
        }

        @Override
        public MultipleChoiceQuestion[] newArray(int size) {
            return new MultipleChoiceQuestion[size];
        }
    };
}
