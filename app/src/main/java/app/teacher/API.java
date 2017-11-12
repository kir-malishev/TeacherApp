package app.teacher;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Кирилл on 04.11.2017.
 */
public interface API {
    String USER_AGENT = "User-Agent: Project By Kirill Malyshev 1.0";

    @FormUrlEncoded
    @Headers(USER_AGENT)
    @POST("/api/scripts/savetest.php")
    Call<SaveTestResponse> sendTest(@Field("testJSON") String testJSON, @Field("login") String login);


    @FormUrlEncoded
    @Headers(USER_AGENT)
    @POST("/api/scripts/gettestreview.php")
    Call<ResponseBody> getTestReview(@Field("login") String login);

    @FormUrlEncoded
    @Headers(USER_AGENT)
    @POST("/api/scripts/gettest.php")
    Call<GetTestResponse> getTest(@Field("testID") String testID);


}
