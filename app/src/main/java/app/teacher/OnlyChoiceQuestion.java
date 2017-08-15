package app.teacher;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashSet;

/**
 * Created by Кирилл on 01.07.2017.
 */
public class OnlyChoiceQuestion extends Challenge implements Parcelable {

    HashSet<String> answers;
    String rightAnswer;
    static final int TYPE = 0;

    OnlyChoiceQuestion(){
        super(TYPE);
        this.answers = new HashSet<String>();
        this.rightAnswer = "";
    }

    OnlyChoiceQuestion(String question){
        super(TYPE, question);
        this.answers = new HashSet<String>();
        this.rightAnswer = "";

    }

    OnlyChoiceQuestion(String question, HashSet<String> answers){
        super(TYPE, question);
        this.answers = answers;
        this.rightAnswer = "";
    }

    OnlyChoiceQuestion(String question, HashSet<String> answers, String rightAnswer){
        super(TYPE, question);
        this.answers = answers;
        this.rightAnswer = rightAnswer;
    }

    protected OnlyChoiceQuestion(Parcel in) {
        super(in);
        answers = (HashSet) in.readValue(HashSet.class.getClassLoader());
        rightAnswer = in.readString();
    }


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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(answers);
        dest.writeString(rightAnswer);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<OnlyChoiceQuestion> CREATOR = new Parcelable.Creator<OnlyChoiceQuestion>() {
        @Override
        public OnlyChoiceQuestion createFromParcel(Parcel in) {
            return new OnlyChoiceQuestion(in);
        }

        @Override
        public OnlyChoiceQuestion[] newArray(int size) {
            return new OnlyChoiceQuestion[size];
        }
    };
}
