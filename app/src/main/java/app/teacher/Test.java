package app.teacher;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Кирилл on 04.07.2017.
 */
public class Test {
    ArrayList<Challenge> challenges;

    String name;

    Test(){
        challenges = new ArrayList<Challenge>();
    }

    Test(String name){
        challenges = new ArrayList<Challenge>();
        this.name = name;
    }

    Test(ArrayList<Challenge> challenges){
        this.challenges = challenges;
    }

    Test(ArrayList<Challenge> challenges, String name){
        this.challenges = challenges;
        this.name = name;
    }

    void addChallenge(Challenge challenge){
        challenges.add(challenge);
    }

    void setChallenges(ArrayList<Challenge> challenges){
        this.challenges = challenges;
    }

    void setName(String name){
        this.name = name;
    }

    Challenge getChallenge(int index){
        return challenges.get(index);
    }

    ArrayList<Challenge> getAllChallenges(){
        return challenges;
    }

    String getName(){
        return name;
    }

    void remove(int index){
        challenges.remove(index);
    }

    void remove(Challenge challenge){
        challenges.remove(challenge);
    }

    int size(){
        return challenges.size();
    }

}
