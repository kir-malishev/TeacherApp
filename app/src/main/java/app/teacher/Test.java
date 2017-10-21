package app.teacher;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Кирилл on 04.07.2017.
 */
public class Test {

    ArrayList<Challenge> challenges;
    String name;
    String testId;
    static final String FILE_FOR_SAVE = "data_for_test";

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

    void clear(){
        challenges.clear();
        name = "";
        testId = "";
    }
    void swap(int firstPos, int secondPos){
        Collections.swap(challenges, firstPos, secondPos);
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

    boolean isEmpty(){
        return challenges.isEmpty() && name.isEmpty() && testId.isEmpty();
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

    public static void clearTestFromMemory(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(Test.FILE_FOR_SAVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }

    public static void showJsonInLog(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(Test.FILE_FOR_SAVE, Context.MODE_PRIVATE);
        Log.d("DEBUG_TEST", sharedPref.getString("test", ""));
    }


    void saveTest(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(Test.FILE_FOR_SAVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        CompatibleWithJSON<Test> converter = new TestConverter();
        String json = converter.getJSON(this);
        editor.putString("test", json);
        editor.apply();
    }


    public static Test getTest(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(Test.FILE_FOR_SAVE, Context.MODE_PRIVATE);
        String json = sharedPref.getString("test", "");
        CompatibleWithJSON<Test> converter = new TestConverter();
        return converter.getFromJSON(json);
    }


}
