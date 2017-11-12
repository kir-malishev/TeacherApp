package app.teacher;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Кирилл on 12.11.2017.
 */
public class GetTestResponse {

    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("test_json")
    @Expose
    private String testJSON;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTestJSON() {
        return testJSON;
    }

    public void setTestJSON(String testJSON) {
        this.testJSON = testJSON;
    }
}
