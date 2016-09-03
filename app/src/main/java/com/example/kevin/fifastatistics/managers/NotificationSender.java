package com.example.kevin.fifastatistics.managers;

import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.models.notifications.NotificationResponse;
import com.example.kevin.fifastatistics.models.notifications.notificationrequestbodies.AcceptFriendRequestBody;
import com.example.kevin.fifastatistics.models.notifications.notificationrequestbodies.DeclineFriendRequestBody;
import com.example.kevin.fifastatistics.models.notifications.notificationrequestbodies.SendFriendRequestBody;
import com.example.kevin.fifastatistics.network.ApiAdapter;
import com.example.kevin.fifastatistics.network.NotificationsApi;
import com.example.kevin.fifastatistics.utils.NetworkUtils;
import com.example.kevin.fifastatistics.utils.ObservableUtils;

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
        SendFriendRequestBody body = new SendFriendRequestBody(currentUser, receiverRegistrationToken);
        return sendNotification(API.sendFriendRequest(body));
    }

    public static Observable<NotificationResponse> acceptFriendRequest(User user, String regToken) {
        AcceptFriendRequestBody body = new AcceptFriendRequestBody(user, regToken);
        return sendNotification(API.acceptFriendRequest(body));
    }

    public static Observable<NotificationResponse> declineFriendRequest(User user, String regToken) {
        DeclineFriendRequestBody body = new DeclineFriendRequestBody(user, regToken);
        return sendNotification(API.declineFriendRequest(body));
    }

    private static Observable<NotificationResponse> sendNotification(Observable<NotificationResponse> action) {
        if (NetworkUtils.isNotConnected()) {
            return Observable.just(NotificationResponse.ERROR_RESPONSE);
        } else {
            return action
                    .compose(ObservableUtils.applySchedulers())
                    .onErrorReturn(t -> NotificationResponse.ERROR_RESPONSE);
        }
    }
}
