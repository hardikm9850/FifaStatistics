package com.example.kevin.fifastatistics.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.overview.MainActivity;
import com.example.kevin.fifastatistics.user.User;
import com.example.kevin.fifastatistics.utils.PreferenceHandler;
import com.google.android.gms.gcm.GcmListenerService;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    private static final String IMAGE_URL = "imageUrl";
    private static final String TITLE = "gcm.notification.title";
    private static final String BODY = "gcm.notification.body";

    private static final String NEW_REQUEST = "New Friend Request";

    private static final String DEFAULT_TITLE = "FIFA Statistics";
    private static final String DEFAULT_BODY = "";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param bundle Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    @Override
    public void onMessageReceived(String from, Bundle bundle)
    {
        Log.d(TAG, "From: " + from);
        initializeImageLoader();

        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }

        String tag = getTag(bundle);
        Log.i(TAG, "tag: " + tag);
        if (tag == null) {
            return;
        }
        else if (tag.equals(NotificationTypesEnum.FRIEND_REQUEST.name()))
        {
            addFriendRequestToUser(bundle);
            sendFriendRequestNotification(bundle);
        }
        else {
            // TODO OTHER NOTIFICATIONS
        }

    }

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param notification GCM message received.
     */
    private void sendFriendRequestNotification(Bundle notification)
    {
        ImageLoader imageLoader = ImageLoader.getInstance();
        Bitmap largeIcon = imageLoader.loadImageSync(notification.getString(IMAGE_URL));

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("fragment", "friends");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        // TODO HANDLE NPE
        String title = NEW_REQUEST;
        String body = DEFAULT_BODY;
        try
        {
            title = notification.getString(TITLE);
            body = notification.getString(BODY);
        }
        catch (NullPointerException e)
        {
            Log.e(TAG, "NPE!");
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(getCircleBitmap(largeIcon))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void addFriendRequestToUser(Bundle data)
    {
        PreferenceHandler handler = PreferenceHandler.getInstance(getApplicationContext());
        User user = handler.getUser();
        user.addIncomingRequest(
                data.getString("name"), data.getString("id"), data.getString(IMAGE_URL));
        handler.storeUserAsync(user);
    }

    private static String getTag(Bundle data)
    {
        try
        {
            return data.getString("tag");
        }
        catch (NullPointerException e)
        {
            return null;
        }
    }

    private void initializeImageLoader()
    {
        DisplayImageOptions notificationOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(false).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new RoundedBitmapDisplayer(256)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .defaultDisplayImageOptions(notificationOptions)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
    }

    private Bitmap getCircleBitmap(Bitmap bitmap)
    {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }
}