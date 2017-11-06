package app.teacher;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Кирилл on 05.11.2017.
 */
public class TestDescription {

    final static private String FILE_FOR_SAVE = "data_about_tests";

    @SerializedName("test_name")
    @Expose
    String testName;

    @SerializedName("testID")
    @Expose
    String testID;

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTestID() {
        return testID;
    }

    public void setTestID(String testID) {
        this.testID = testID;
    }

    /*public String getJSON(){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(this);
    }*/

    public static void saveJSON(Context context, String json) {
        SharedPreferences sharedPref = context.getSharedPreferences(TestDescription.FILE_FOR_SAVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("test_descriptions", json);
        editor.apply();
    }

    public static ArrayList<TestDescription> getTestDescriptions(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(TestDescription.FILE_FOR_SAVE, Context.MODE_PRIVATE);
        String json = sharedPref.getString("test_descriptions", "");
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<ArrayList<TestDescription>>() {
        }.getType();
        ArrayList<TestDescription> descriptions = gson.fromJson(json, listType);
        return descriptions;
    }


}
