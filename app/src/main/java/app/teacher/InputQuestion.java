package app.teacher;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Кирилл on 01.07.2017.
 */
public class InputQuestion extends Challenge implements Parcelable{

    HashSet<String> rightAnswer;

    InputQuestion(){
        super();
    }

    InputQuestion(String question){
        super(question);
    }

    InputQuestion(String question, HashSet<String> rightAnswer){
        super(question);
        this.rightAnswer = rightAnswer;
    }

    protected InputQuestion(Parcel in) {
        super(in);
        ArrayList<String> data = new ArrayList<String>();
        in.readStringList(data);
        rightAnswer = new HashSet(data);
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeStringList(new ArrayList(rightAnswer));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<InputQuestion> CREATOR = new Creator<InputQuestion>() {
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
