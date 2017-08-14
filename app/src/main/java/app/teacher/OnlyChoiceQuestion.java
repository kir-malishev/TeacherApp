package app.teacher;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashSet;

/**
 * Created by Кирилл on 01.07.2017.
 */
public class OnlyChoiceQuestion extends ChoiceQuestion implements Parcelable {

    String rightAnswer;

    OnlyChoiceQuestion(){
        super();
    }

    OnlyChoiceQuestion(String question){
        super(question);

    }

    OnlyChoiceQuestion(String question, HashSet<String> answers){
        super(question, answers);
    }

    OnlyChoiceQuestion(String question, HashSet<String> answers, String rightAnswer){
        super(question, answers);
        this.rightAnswer = rightAnswer;
    }

    protected OnlyChoiceQuestion(Parcel in) {
        super(in);
        rightAnswer = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(rightAnswer);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OnlyChoiceQuestion> CREATOR = new Creator<OnlyChoiceQuestion>() {
        @Override
        public OnlyChoiceQuestion createFromParcel(Parcel in) {
            return new OnlyChoiceQuestion(in);
        }

        @Override
        public OnlyChoiceQuestion[] newArray(int size) {
            return new OnlyChoiceQuestion[size];
        }
    };

    void setRightAnswer(String rightAnswer){
        this.rightAnswer = rightAnswer;
    }

    boolean checkAnswer(String answer){
        return this.rightAnswer.equals(answer);
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
