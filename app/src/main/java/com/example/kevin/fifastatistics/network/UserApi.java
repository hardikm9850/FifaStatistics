package com.example.kevin.fifastatistics.network;

import com.example.kevin.fifastatistics.models.apiresponses.ApiListResponse;
import com.example.kevin.fifastatistics.models.databasemodels.match.Series;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

public interface UserApi {

    @GET("users/{id}")
    Observable<User> getUser(@Path("id") String id);

    @GET("users")
    Observable<ApiListResponse<User>> getUsers();

    @GET("users/search/findByName")
    Observable<ApiListResponse<User>> getUsersWithName(@Query("name") String name);


    @GET("users/search/findByNameStartingWithIgnoreCase")
    Observable<ApiListResponse<User>> getUsersWithNameStartingWith(@Query("name") String name);

    @GET("users/search/findByGoogleId")
    Observable<User> getUserWithGoogleId(@Query("googleId") String googleId);

    /**
     * Creates a user (Makes a POST request to /users).
     * <br> To retrieve the response from this request (the user that was
     * created), chain this call with <code>flatMap</code> to {@link
     * #lookupUser(String)} using the response's Location header. For example:
     * <pre>
     *
     * {@code api.createUser(user)
     *        .flatMap(response -> api.lookupUser(response.headers().get("Location")))
     * }
     * </pre>
     * This will create the user and grab the result from the location specified
     * by the Location header.
     *
     * @param user  The User object to be created
     * @return the Void Response
     */
    @POST("users")
    Observable<Response<Void>> createUser(@Body User user);

    @GET
    Observable<User> lookupUser(@Url String url);

    @PUT("users/{id}")
    Observable<Response<Void>> updateUser(@Path("id") String id, @Body User user);

    @PATCH("users/{id}")
    Observable<User> patchUser(@Path("id") String id, @Body String body);
}
