package com.example.kevin.fifastatistics.managers;

import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.models.notifications.NotificationResponse;
import com.example.kevin.fifastatistics.models.notifications.notificationrequestbodies.NewMatchBody;
import com.example.kevin.fifastatistics.network.FifaApi;
import com.example.kevin.fifastatistics.network.NotificationsApi;
import com.example.kevin.fifastatistics.utils.NetworkUtils;
import com.example.kevin.fifastatistics.utils.ObservableUtils;

import rx.Observable;

/**
 * Manages sending of notifications.
 */
public class NotificationSender {

    private static final NotificationsApi API = FifaApi.getNotificationsApi();

    public static Observable<NotificationResponse> addMatch(User user, String regToken, Match match) {
        NewMatchBody body = new NewMatchBody(user, regToken, match);
        return sendNotification(API.addNewMatch(body));
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
