package com.example.kevin.fifastatistics.views.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Random;

public class NotificationCancelingReceiver extends BroadcastReceiver {

    private static final String TAG = "CANCEL_RECEIVER";
    private static final String ID_EXTRA = "notification_id";
    private static final String INTENT_EXTRA = "activity_intent";

    public static PendingIntent getIntent(Context context, PendingIntent activityIntent,
                                          int notificationId) {
        Bundle receiverData = new Bundle();
        receiverData.putParcelable(INTENT_EXTRA, activityIntent);
        receiverData.putInt(ID_EXTRA, notificationId);
        Intent receiverIntent = new Intent(context, NotificationCancelingReceiver.class);
        receiverIntent.putExtras(receiverData);
        receiverIntent.setAction(getRandomActionToEnsureExtrasAreNotLost(notificationId));
        return PendingIntent.getBroadcast(context, 0, receiverIntent, 0);
    }

    /** https://stackoverflow.com/a/3128271/6164423 */
    private static String getRandomActionToEnsureExtrasAreNotLost(int notificationId) {
        int id = Math.abs(notificationId);
        return String.valueOf(id + new Random().nextInt(id));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data = intent.getExtras();
        cancelNotification(data, context);
        handleContentIntent(data);
        closeNotificationDrawer(context);
    }

    @SuppressWarnings("ConstantConditions")
    private void handleContentIntent(Bundle extras) {
        PendingIntent intent = extras.getParcelable(INTENT_EXTRA);
        try {
            intent.send();
        } catch (PendingIntent.CanceledException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    private void cancelNotification(Bundle extras, Context context) {
        int notificationId = extras.getInt(ID_EXTRA);
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(notificationId);
    }

    private void closeNotificationDrawer(Context context) {
        context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }
}
