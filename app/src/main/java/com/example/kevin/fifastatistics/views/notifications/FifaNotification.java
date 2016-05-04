package com.example.kevin.fifastatistics.views.notifications;

import static com.example.kevin.fifastatistics.models.Constants.APP_NAME;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.utils.BitmapUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public abstract class FifaNotification
{
    private static final String CONTENT_TITLE = APP_NAME;
    private static final Uri SOUND_URI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    private static final int SMALL_ICON = R.mipmap.ic_launcher;

    private Context context;
    private Bundle notification;

    protected static int notificationId;
    protected NotificationCompat.Builder notificationBuilder;

    public FifaNotification(Context context, Bundle notification)
    {
        this.context = context;
        this.notification = notification;
        buildAbstractNotification();
    }

    private Bitmap getUserBitmap()
    {
        ImageLoader imageLoader = ImageLoader.getInstance();
        return imageLoader.loadImageSync(notification.getString("imageUrl"));
    }

    private void buildAbstractNotification()
    {
        Bitmap userIcon = BitmapUtils.getCircleBitmap(getUserBitmap());

        notificationBuilder = GlobalNotificationBuilder.getInstance(context)
                .setSmallIcon(SMALL_ICON)
                .setLargeIcon(userIcon)
                .setContentTitle(CONTENT_TITLE)
                .setOnlyAlertOnce(true)
                .setAutoCancel(true)
                .setSound(SOUND_URI)
                .setPriority(Notification.PRIORITY_HIGH);
    }

    public final void finalizeAndSend()
    {
        setContentText(notification);
        setContentIntent();

        NotificationManager nm = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);

        nm.notify(notificationId, notificationBuilder.build());
    }

    public abstract void performPreSendActions();

    protected abstract void setContentText(Bundle notification);
    protected abstract void setContentIntent();
}
