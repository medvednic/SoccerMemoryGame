package itworks.eddy.soccermemorygame.RESTaccess;

import itworks.eddy.soccermemorygame.Models.ServerResponse;
import itworks.eddy.soccermemorygame.Models.UsersList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by medve on 27/04/2016.
 */
public interface apiServices {

    @POST("login")
    Call<UsersList> login(@Header("username") String username, @Header("password") String password);

    @POST("register")
    Call<ServerResponse> register(@Header("username") String username, @Header("password") String password);

    @POST("updatescore")
    Call<ServerResponse> updateScore(@Header("username") String username, @Header("score") int score,
                                     @Header("level") int level);

    @GET("getscores")
    Call<UsersList> getScores(@Header("level") int level);

    @POST("resetscores")
    Call<ServerResponse> resetScores(@Header("username") String username);

    @POST("deleteuser")
    Call<ServerResponse> deleteUser(@Header("username") String username, @Header("password") String password);

}

