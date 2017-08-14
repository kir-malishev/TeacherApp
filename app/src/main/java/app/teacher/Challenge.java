package app.teacher;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Кирилл on 01.07.2017.
 */
public class Challenge implements Parcelable{

    String question;

    Challenge(){}


    Challenge(String question){
        this.question = question;
    }

    protected Challenge(Parcel in) {
        question = in.readString();
    }

    public static final Creator<Challenge> CREATOR = new Creator<Challenge>() {
        @Override
        public Challenge createFromParcel(Parcel in) {
            return new Challenge(in);
        }

        @Override
        public Challenge[] newArray(int size) {
            return new Challenge[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(question);

    }

    String getQuestion(){
        return question;
    }
}
