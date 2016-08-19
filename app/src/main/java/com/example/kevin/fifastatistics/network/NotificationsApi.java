package com.example.kevin.fifastatistics.network;

import com.example.kevin.fifastatistics.models.notifications.NotificationResponse;
import com.example.kevin.fifastatistics.models.notifications.notificationrequestbodies.AcceptFriendRequestBody;
import com.example.kevin.fifastatistics.models.notifications.notificationrequestbodies.DeclineFriendRequestBody;
import com.example.kevin.fifastatistics.models.notifications.notificationrequestbodies.SendFriendRequestBody;

import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Retrofit interface for sending notifications.
 */
public interface NotificationsApi {

    String NOTIFICATION_KEY = "AIzaSyDjCHksoGamhWxeNsaDN-DW5v3p9IcJNFE";

    @Headers("Authorization:key=" + NOTIFICATION_KEY)
    @POST("send")
    Observable<NotificationResponse> sendFriendRequest(@Body SendFriendRequestBody body);

    @Headers("Authorization:key=" + NOTIFICATION_KEY)
    @POST("send")
    Observable<NotificationResponse> acceptFriendRequest(@Body AcceptFriendRequestBody body);

    @Headers("Authorization:key=" + NOTIFICATION_KEY)
    @POST("send")
    Observable<NotificationResponse> declineFriendRequest(@Body DeclineFriendRequestBody body);
}
