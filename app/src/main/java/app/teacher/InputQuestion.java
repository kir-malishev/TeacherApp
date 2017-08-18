package app.teacher;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Кирилл on 01.07.2017.
 */
public class InputQuestion extends Challenge {

    ArrayList<String> rightAnswer;
    static final int TYPE = 2;

    InputQuestion(){
        super(TYPE);
        this.rightAnswer = new ArrayList<String>();
    }

    InputQuestion(String question){
        super(TYPE, question);
        this.rightAnswer = new ArrayList<String>();
    }

    InputQuestion(String question, ArrayList<String> rightAnswer){
        super(TYPE, question);
        this.rightAnswer = rightAnswer;
    }
}
