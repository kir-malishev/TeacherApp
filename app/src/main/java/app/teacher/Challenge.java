package app.teacher;

import android.os.Parcel;
import android.os.Parcelable;

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


}
