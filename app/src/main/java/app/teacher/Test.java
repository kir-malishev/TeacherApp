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
    String testId;

    Test(){
        this.challenges = new ArrayList<Challenge>();
        this.name = "";
        this.testId = "";
    }

    Test(String name){
        this.challenges = new ArrayList<Challenge>();
        this.name = name;
        this.testId = "";
    }

    Test(String name, String testId){
        this.challenges = new ArrayList<Challenge>();
        this.name = name;
        this.testId = testId;
    }

    Test(ArrayList<Challenge> challenges){
        this.challenges = challenges;
        this.name = "";
        this.testId = "";
    }

    Test(ArrayList<Challenge> challenges, String name){
        this.challenges = challenges;
        this.name = name;
        this.testId = "";
    }

    Test(ArrayList<Challenge> challenges, String name, String testId){
        this.challenges = challenges;
        this.name = name;
        this.testId = testId;
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

    void setTestId(String testId){
        this.testId = testId;
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

    String getTestId(){
        return this.testId;
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
