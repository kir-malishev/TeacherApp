package app.teacher;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Кирилл on 01.07.2017.
 */
public class ChoiceQuestion extends Challenge implements Parcelable{

    HashSet<String> answers;

    ChoiceQuestion(){
        super();
        this.answers = new HashSet<String>();
    }
    ChoiceQuestion(String question) {
        super(question);
        this.answers = new HashSet<String>();
    }

    ChoiceQuestion(String question, HashSet<String> answers) {
        super(question);
        this.answers = answers;
    }

    protected ChoiceQuestion(Parcel in) {
        super(in);
        ArrayList<String> data = new ArrayList<String>();
        in.readStringList(data);
        answers = new HashSet(data);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ChoiceQuestion> CREATOR = new Creator<ChoiceQuestion>() {
        @Override
        public ChoiceQuestion createFromParcel(Parcel in) {
            return new ChoiceQuestion(in);
        }

        @Override
        public ChoiceQuestion[] newArray(int size) {
            return new ChoiceQuestion[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeStringList(new ArrayList(answers));

    }

    void addAnswers(String... newAnswers){
        this.answers.addAll(Arrays.asList(newAnswers));
    }

    void setAnswers(HashSet<String> answers){
        this.answers = answers;
    }

    HashSet<String> getAnswers(){
        return answers;
    }

    void removeAnswers(String... answersForRemove){
        this.answers.remove(answersForRemove);
    }

}
