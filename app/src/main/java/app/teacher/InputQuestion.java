package app.teacher;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Кирилл on 01.07.2017.
 */
public class InputQuestion extends Challenge implements Parcelable {

    HashSet<String> rightAnswer;
    static final int TYPE = 2;

    InputQuestion(){
        super(TYPE);
        this.rightAnswer = new HashSet<String>();
    }

    InputQuestion(String question){
        super(TYPE, question);
        this.rightAnswer = new HashSet<String>();
    }

    InputQuestion(String question, HashSet<String> rightAnswer){
        super(TYPE, question);
        this.rightAnswer = rightAnswer;
    }


    protected InputQuestion(Parcel in) {
        super(in);
        rightAnswer = (HashSet) in.readValue(HashSet.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(rightAnswer);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<InputQuestion> CREATOR = new Parcelable.Creator<InputQuestion>() {
        @Override
        public InputQuestion createFromParcel(Parcel in) {
            return new InputQuestion(in);
        }

        @Override
        public InputQuestion[] newArray(int size) {
            return new InputQuestion[size];
        }
    };
}
