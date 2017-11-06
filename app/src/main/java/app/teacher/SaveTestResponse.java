package app.teacher;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Кирилл on 05.11.2017.
 */
public class SaveTestResponse {

    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("testID")
    @Expose
    private String testID;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTestID() {
        return testID;
    }

    public void setTestID(String testID) {
        this.testID = testID;
    }

}