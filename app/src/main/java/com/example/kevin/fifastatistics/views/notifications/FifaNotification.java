package com.example.kevin.fifastatistics.views.notifications;

import static com.example.kevin.fifastatistics.models.Constants.APP_NAME;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.models.notifications.notificationbundles.NotificationBundle;
import com.example.kevin.fifastatistics.utils.BitmapUtils;

public abstract class FifaNotification
{
    private static final String CONTENT_TITLE = APP_NAME;
    private static final Uri SOUND_URI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    private static final int SMALL_ICON = R.mipmap.ic_launcher;

    protected static NotificationCompat.Builder mNotificationBuilder;

    private Context context;

    public FifaNotification(Context context) {
        this.context = context;
        mNotificationBuilder = new NotificationCompat.Builder(context);
    }

    public static NotificationCompat.Builder getNotifcationBuilder() {
        return mNotificationBuilder;
    }

    public final void build() {
        NotificationBundle nb = getNotificationBundle();
        setDefaultNotificationSettings(nb);
        setContentText(nb);
        setContentIntent();
        addActions();
        performPreSendActions();
        send(getNotificationId());
    }

    /**
     * Retrieve the specialized NotificationBundle from the subclass.
     */
    protected abstract NotificationBundle getNotificationBundle();

    /**
     * Sets default settings such as the icons, the title, and alert settings.
     */
    protected void setDefaultNotificationSettings(NotificationBundle nb)
    {
        Log.e("FIFA NOTIFICATION", nb.toString());
        Bitmap userIcon = BitmapUtils.getCircleBitmapFromUrl(nb.getImageUrl());

        mNotificationBuilder
                .setSmallIcon(SMALL_ICON)
                .setLargeIcon(userIcon)
                .setContentTitle(CONTENT_TITLE)
                .setOnlyAlertOnce(true)
                .setAutoCancel(true)
                .setSound(SOUND_URI)
                .setPriority(Notification.PRIORITY_HIGH);
    }

    /**
     * Sets the context text for the notification.
     */
    private void setContentText(NotificationBundle bundle) {
        if (bundle != null) mNotificationBuilder.setContentText(bundle.getBody());
    }

    /**
     * Sets the content intent that will be launched when the notification is
     * tapped.
     */
    protected abstract void setContentIntent();

    /**
     * Adds any extra actions to the notification (not required). This is a
     * 'hook' in the {@link #build()} template method.
     */
    protected void addActions() {}

    /**
     * Performs actions that should be sent prior to the notification being sent
     * (e.g.,
     */
    protected abstract void performPreSendActions();

    /**
     * Retrieves the notification id from the subclass, and uses it when sending
     * the notification.
     */
    protected abstract int getNotificationId();

    /**
     * Sends the notification. Can optionally be overriden by a subclass (e.g.,
     * the NullNotification class, so no notification sends).
     */
    protected void send(int notificationId) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Log.e("NOTIFICATION", String.valueOf(mNotificationBuilder.mActions.size()));
        nm.notify(notificationId, mNotificationBuilder.build());
    }
}
