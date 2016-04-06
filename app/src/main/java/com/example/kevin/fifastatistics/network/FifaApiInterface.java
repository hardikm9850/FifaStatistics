package com.example.kevin.fifastatistics.network;


import com.example.kevin.fifastatistics.models.user.User;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * API Interface used for accessing the FifaStatistics REST API. Uses the
 * Retrofit library, and works with RxJava (follows the Observable pattern).
 */
public interface FifaApiInterface
{
    /**
     * Get the user with the specified ID.
     * @param id    the user's ID
     * @return the User Observable
     */
    @GET("users/{id}")
    Observable<User> getUser(@Path("id") String id);

    /**
     * Lists all users.
     * @return the User List Observable
     */
    @GET("users")
    Observable<List<User>> getUsers();

    /**
     * Lists all users with the specified name.
     * @param name the name being searched for
     * @return the User List Observable
     */
    @GET("users/search/findByName")
    Observable<List<User>> getUsersWithName(@Query("name") String name);

    /**
     * Find the user the with the specified googleId.
     * @param googleId the googleId associated with the user
     * @return the User Observable
     */
    @GET("users/search/findByGoogleId")
    Observable<User> getUserWithGoogleId(@Query("googleId") String googleId);

    /**
     * Creates a user.
     * <br> To retrieve the response from this request (the user that was
     * created, chain this call with <code>flatMap</code> to {@link
     * #lookupUser(String)} using the response's Location header. For example:
     * <pre>
     *
     * {@code api.createUser(user)
     *        .flatMap(response -> api.lookupUser(response.headers().get("Location")))
     * }
     * </pre>
     * Will create the user and grab the result from the location specified by
     * the Location header.
     *
     * @param user  The User object to be created
     * @return the Void Response
     */
    @POST("users")
    Observable<Response<Void>> createUser(@Body User user);

    /**
     * Looks up the User at the specified URL. Should be used to retrieve a
     * user after calling {@link #createUser(User)}.
     * @param url   The URL where the user can be found. This should be specified
     *              by <code>response.headers().get("Location")</code>, where
     *              <code>response</code> is the response of a call to
     *              <code>createUser()</code>.
     * @return The User Observable
     */
    @GET
    Observable<User> lookupUser(@Url String url);

//    @POST
//    @Headers("key: " + Constants.NOTIFICATION_KEY)
//    Observable<FriendRequestResponse> sendFriendRequest(
//            @Body FriendRequestBody friendRequestBody);
}
