package com.example.kevin.fifastatistics.managers;

import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.models.notifications.NotificationResponse;
import com.example.kevin.fifastatistics.models.notifications.notificationrequestbodies.FriendRequestBody;
import com.example.kevin.fifastatistics.network.ApiAdapter;
import com.example.kevin.fifastatistics.network.NotificationsApi;
import com.example.kevin.fifastatistics.utils.NetworkUtils;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Manages sending of notifications.
 */
public class NotificationSender {

    private static final NotificationsApi API = ApiAdapter.getNotificationsApi();

    /**
     * Send a friend request notification and return the response. Returns an unsuccessful
     * response if the notification did not successfully send for any reason.
     */
    public static Observable<NotificationResponse> sendFriendRequest(User currentUser, String receiverRegistrationToken) {
        if (NetworkUtils.isNotConnected()) return Observable.just(NotificationResponse.ERROR_RESPONSE);
        FriendRequestBody body = new FriendRequestBody(currentUser, receiverRegistrationToken);
        return sendNotification(API.sendFriendRequest(body));
    }

    public static Observable<NotificationResponse> acceptFriendRequest(User user, String regToken) {
        // TODO
    }

    public static Observable<NotificationResponse> declineFriendRequest(User user, String regToken) {
        // TODO
    }

    private static Observable<NotificationResponse> sendNotification(Observable<NotificationResponse> action) {
        return action
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .onErrorReturn(t -> NotificationResponse.ERROR_RESPONSE);
    }
}
