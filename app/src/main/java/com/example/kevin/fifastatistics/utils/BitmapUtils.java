package com.example.kevin.fifastatistics.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import com.nostra13.universalimageloader.core.ImageLoader;

import lombok.experimental.UtilityClass;

/**
 * Utility class for bitmap operations.
 */
@UtilityClass
public class BitmapUtils
{
    /**
     * Creates a new circular bitmap from the source bitmap.
     * @param bitmap    The bitmap source
     * @return  a new, circular version of the source bitmap
     */
    public static Bitmap getCircleBitmap(Bitmap bitmap)
    {
        final Bitmap output =
                Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

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

        return output;
    }

    /**
     * Retrieves a Bitmap from the specified image URL.
     * This method must not be called on the UI Thread, as it loads the image synchronously and thus
     * blocks the calling thread.
     * @param url   the image URL
     * @return the bitmap
     */
    public static Bitmap getBitmapFromUrl(String url) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        return imageLoader.loadImageSync(url);
    }

    /**
     * Retrieves a Bitmap from the specified image URL, and formats it into a circle.
     * This method must not be called on the UI Thread, as it loads the image synchronously and thus
     * blocks the calling thread.
     * @param url   the image URL
     * @return the circular bitmap
     */
    public static Bitmap getCircleBitmapFromUrl(String url) {
        Bitmap bitmap = getBitmapFromUrl(url);
        return getCircleBitmap(bitmap);
    }

    /**
     * Returns a plain white bitmap.
     * @param width     The width of the bitmap
     * @param height    THe height of the bitmap
     * @return  the plain white bitmap
     */
    public static Bitmap getBlankBitmap(int width, int height) {
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        image.eraseColor(android.graphics.Color.WHITE);
        return image;
    }
}
