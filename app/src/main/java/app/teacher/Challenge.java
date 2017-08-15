package app.teacher;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Кирилл on 01.07.2017.
 */
public class Challenge implements Parcelable {

    /**CHALLENGE                         TYPE
     *
     *
     * Unknown                             -1
     *
     * OnlyChoiceQuestion                   0
     * MultipleChoiceQuestion               1
     * InputQuestion                        2
     */


    String question;
    int type = -1;


    Challenge(int type){
        this.type = type;
    }

    Challenge(int type, String question){
        this.type = type;
        this.question = question;
    }

    String getQuestion(){
        return question;
    }

    int getType(){
        return type;
    }

    protected Challenge(Parcel in) {
        this.question = in.readString();
        this.type = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeInt(type);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Challenge> CREATOR = new Parcelable.Creator<Challenge>() {
        @Override
        public Challenge createFromParcel(Parcel in) {
            return new Challenge(in);
        }

        @Override
        public Challenge[] newArray(int size) {
            return new Challenge[size];
        }
    };
}
