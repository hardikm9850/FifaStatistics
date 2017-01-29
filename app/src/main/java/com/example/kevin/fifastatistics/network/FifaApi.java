package com.example.kevin.fifastatistics.network;

import com.example.kevin.fifastatistics.models.apiresponses.ApiListResponse;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchProjection;
import com.example.kevin.fifastatistics.models.databasemodels.match.Series;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;

import lombok.Getter;
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

/**
 * API Interface used for accessing the FifaStatistics REST API. Uses the
 * Retrofit library, and works with RxJava (follows the Observable pattern).
 */
public interface FifaApi
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
    Observable<ApiListResponse<User>> getUsers();

    /**
     * Lists all users with the specified name.
     * @param name the name being searched for
     * @return the User List Observable
     */
    @GET("users/search/findByName")
    Observable<ApiListResponse<User>> getUsersWithName(@Query("name") String name);

    /**
     * Lists all users who have getNameSet starting with the specified name.
     * <br> e.g. name=ke would return users with name 'Kevin', 'kelsey', etc.
     * @param name the name being searched for
     * @return the User List Observable
     */
    @GET("users/search/findByNameStartingWithIgnoreCase")
    Observable<ApiListResponse<User>> getUsersWithNameStartingWith(
            @Query("name") String name);

    /**
     * Find the user the with the specified googleId.
     * @param googleId the googleId associated with the user
     * @return the User Observable
     */
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

    /**
     * Looks up the User at the specified URL. Should be used to retrieve a
     * user after calling {@link #createUser(User)} or {@link #updateUser(String, User)}.
     * @param url   The URL where the user can be found. This should be specified
     *              by <code>response.headers().get("Location")</code>, where
     *              <code>response</code> is the response of a call to createUser() or updateUser().
     * @return The User Observable
     */
    @GET
    Observable<User> lookupUser(@Url String url);

    /**
     * Make a PUT request to the server to update the specified user.
     * @param id    The user's ID
     * @param user  The user to be updated
     * @return the Retrofit response. The desired code is 204 No Content.
     */
    @PUT("users/{id}")
    Observable<Response<Void>> updateUser(@Path("id") String id, @Body User user);

    @PATCH("users/{id}")
    Observable<User> patchUser(@Path("id") String id, @Body String body);

    // MATCHES

    @GET("matches")
    Observable<ApiListResponse<MatchProjection>> getMatches();

    @GET("matches/{id}")
    Observable<Match> getMatch(@Path("id") String id);

    @POST("matches")
    Observable<Response<Void>> createMatch(@Body Match match);

    @GET
    Observable<Match> lookupMatch(@Url String url);

    @GET("series")
    Observable<ApiListResponse<Series>> getSeries();

    @POST("series")
    Observable<Response<Void>> createSeries(@Body Series series);

    @GET
    Observable<Series> lookupSeries(@Url String url);
}
