package itworks.eddy.soccermemorygame.RESTaccess;

import itworks.eddy.soccermemorygame.Models.ServerResponse;
import itworks.eddy.soccermemorygame.Models.UsersList;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by medve on 27/04/2016.
 */
public interface apiServices {

    @POST("login")
    Call<UsersList> login(@Header("username") String username, @Header("password") String password);
    //Call<ServerResponse> login(@Body User user); //old post form log in

    @POST("register")
    Call<ServerResponse> register(@Header("username") String username, @Header("password") String password);
    //Call<User> register(@Body User user); //old post form based registration
}
