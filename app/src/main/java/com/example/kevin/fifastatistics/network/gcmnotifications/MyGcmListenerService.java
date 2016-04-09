package com.example.kevin.fifastatistics.network.gcmnotifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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
import com.example.kevin.fifastatistics.activities.MainActivity;
import com.example.kevin.fifastatistics.fragments.friendsfragment.FriendsFragment;
import com.example.kevin.fifastatistics.models.Constants;
import com.example.kevin.fifastatistics.models.user.Friend;
import com.example.kevin.fifastatistics.models.user.User;
import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.google.android.gms.gcm.GcmListenerService;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    private static final String IMAGE_URL = "imageUrl";
    private static final String TITLE = "gcm.notification.title";
    private static final String BODY = "gcm.notification.body";
    private static final String NEW_REQUEST = "New Friend Request";
    private static final String DEFAULT_TAG = "default";
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

        String tag = (getTag(bundle) == null) ? DEFAULT_TAG : getTag(bundle);
        switch (tag) {
            case Constants.FRIEND_REQUEST_TAG :
                addFriendRequestToUser(bundle);
                sendFriendRequestNotification(bundle);
                break;
            default :
                break;
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
        intent.putExtra(MainActivity.FRAGMENT_EXTRA, Constants.FRIENDS_FRAGMENT);
        intent.putExtra(MainActivity.PAGE_EXTRA, FriendsFragment.requestsView);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Intent declineRequestIntent = new Intent(this, FriendRequestDeclineService.class);
        PendingIntent declineRequestPendingIntent = PendingIntent.getService(
                this, 0, declineRequestIntent, PendingIntent.FLAG_ONE_SHOT);

        Intent acceptRequestIntent = new Intent(this, FriendRequestAcceptService.class);
        PendingIntent acceptRequestPendingIntent = PendingIntent.getService(
                this, 0, acceptRequestIntent, PendingIntent.FLAG_ONE_SHOT);

        Resources res = getResources();
        String body = res.getString(R.string.new_friend_request);
        try {
            body += " " + notification.getString(BODY);
        }
        catch (NullPointerException e) {
            body = res.getString(R.string.new_friend_request_npe);
            Log.e(TAG, "NPE!");
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(getCircleBitmap(largeIcon))
                .setContentTitle(Constants.APP_NAME)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .addAction(
                        R.drawable.ic_menu_share,
                        res.getString(R.string.notification_accept),
                        acceptRequestPendingIntent)
                .addAction(
                        R.drawable.ic_action_navigation_close,
                        res.getString(R.string.notification_decline),
                        declineRequestPendingIntent)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void addFriendRequestToUser(Bundle data)
    {
        SharedPreferencesManager handler = SharedPreferencesManager.getInstance(
                getApplicationContext());
        User user = handler.getUser();

        int level = Integer.parseInt(data.getString("level"));
        Friend friend = new Friend.Builder()
                .withId(data.getString("id"))
                .withImageUrl(data.getString(IMAGE_URL))
                .withLevel(level)
                .withName(data.getString("name"))
                .withRegistrationToken(data.getString("RegistrationToken"))
                .build();

        user.addIncomingRequest(friend);
        handler.storeUser(user);
    }

    private static String getTag(Bundle data)
    {
        try {
            return data.getString("tag");
        }
        catch (NullPointerException e) {
            return DEFAULT_TAG;
        }
    }

    private void initializeImageLoader()
    {
        DisplayImageOptions notificationOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(false).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();

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