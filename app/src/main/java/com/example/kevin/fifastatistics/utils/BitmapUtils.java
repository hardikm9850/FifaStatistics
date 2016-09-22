package com.example.kevin.fifastatistics.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicConvolve3x3;

import com.example.kevin.fifastatistics.FifaApplication;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

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
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        image.eraseColor(android.graphics.Color.WHITE);
        return image;
    }

    public static Bitmap grayscale(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap bmOut = Bitmap.createScaledBitmap(bitmap, width, height, false);
        Canvas canvas = new Canvas(bmOut);
        ColorMatrix ma = new ColorMatrix();
        ma.setSaturation(0);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(ma));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        bitmap.recycle();
        return bmOut;

    }

    public static Bitmap contrast(Bitmap bitmap, float contrast) {
        ColorMatrix cm = new ColorMatrix(new float[]
                {
                        contrast, 0, 0, 0, 0,
                        0, contrast, 0, 0, 0,
                        0, 0, contrast, 0, 0,
                        0, 0, 0, 1, 0
                });

        Bitmap ret = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());

        Canvas canvas = new Canvas(ret);

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        bitmap.recycle();

        return ret;
    }

    public static Bitmap sharpenBitmap(Bitmap src) {
        float[] sharpenMatrix = {
                 0, -1,  0,
                -1,  1, -1,
                 0, -1,  0};

        return convolveBitmap(src, sharpenMatrix);
    }

    private static Bitmap convolveBitmap(Bitmap src, float[] coefficients) {

        Bitmap result = Bitmap.createBitmap(src.getWidth(),
                src.getHeight(), src.getConfig());

        RenderScript renderScript = RenderScript.create(FifaApplication.getContext());

        Allocation input = Allocation.createFromBitmap(renderScript, src);
        Allocation output = Allocation.createFromBitmap(renderScript, result);

        ScriptIntrinsicConvolve3x3 convolution = ScriptIntrinsicConvolve3x3
                .create(renderScript, Element.U8_4(renderScript));
        convolution.setInput(input);
        convolution.setCoefficients(coefficients);
        convolution.forEach(output);

        output.copyTo(result);
        renderScript.destroy();
        return result;
    }

    /**
     * Returns the bitmap with inverted colors
     * @param bitmap    the original bitmap
     * @return the inverted bitmap
     */
    public static Bitmap getInvertedBitmap(Bitmap bitmap) {
        float invertMatrix [] = {
                -1.0f,   0.0f,   0.0f,  1.0f,  0.0f,
                 0.0f,  -1.0f,   0.0f,  1.0f,  0.0f,
                 0.0f,   0.0f,  -1.0f,  1.0f,  0.0f,
                 1.0f,   1.0f,   1.0f,  1.0f,  0.0f
        };
        Canvas c = new Canvas(bitmap);
        Paint p = new Paint();
        ColorMatrix cm = new ColorMatrix(invertMatrix);
        p.setColorFilter(new ColorMatrixColorFilter(cm));

        c.drawBitmap(bitmap, 0, 0, p);
        return bitmap;
    }

    public static Bitmap getMutableBitmap(Bitmap imgIn) {
        final int width = imgIn.getWidth(), height = imgIn.getHeight();
        final Bitmap.Config type = imgIn.getConfig();
        File outputFile = null;
        final File outputDir = FifaApplication.getContext().getCacheDir();
        try {
            outputFile = File.createTempFile(Long.toString(System.currentTimeMillis()), null, outputDir);
            outputFile.deleteOnExit();
            final RandomAccessFile randomAccessFile = new RandomAccessFile(outputFile, "rw");
            final FileChannel channel = randomAccessFile.getChannel();
            final MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, imgIn.getRowBytes() * height);
            imgIn.copyPixelsToBuffer(map);
            imgIn.recycle();
            final Bitmap result = Bitmap.createBitmap(width, height, type);
            map.position(0);
            result.copyPixelsFromBuffer(map);
            channel.close();
            randomAccessFile.close();
            outputFile.delete();
            outputDir.delete();
            return result;
        } catch (final Exception e) {
        } finally {
            if (outputFile != null)
                outputFile.delete();
        }
        return null;
    }
}
