package app.teacher;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Кирилл on 20.01.2018.
 */
public class ResultDescription {

    final static private String FILE_FOR_SAVE = "data_about_results";

    @SerializedName("student_name")
    @Expose
    String studentName;

    @SerializedName("date")
    @Expose
    Date date;

    @SerializedName("uuid")
    @Expose
    String resultID;

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Date getDate() {
        return date;
    }

    public String getStringDate(String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public String getStringDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss");
        return dateFormat.format(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getResultID() {
        return resultID;
    }

    public void setResultID(String resultID) {
        this.resultID = resultID;
    }


    public static void saveJSON(Context context, String json) {
        SharedPreferences sharedPref = context.getSharedPreferences(FILE_FOR_SAVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("result_descriptions", json);
        editor.apply();
    }

    public static String getJSON(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(FILE_FOR_SAVE, Context.MODE_PRIVATE);
        String json = sharedPref.getString("result_descriptions", "");
        return json;
    }

    public static ArrayList<ResultDescription> getResultDescriptions(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(FILE_FOR_SAVE, Context.MODE_PRIVATE);
        String json = sharedPref.getString("result_descriptions", "");
        GsonBuilder builder = new GsonBuilder().setDateFormat("dd.MM.yyyy, HH:mm:ss");
        Gson gson = builder.create();
        Type listType = new TypeToken<ArrayList<ResultDescription>>() {
        }.getType();
        ArrayList<ResultDescription> descriptions = gson.fromJson(json, listType);
        return descriptions;
    }
}
