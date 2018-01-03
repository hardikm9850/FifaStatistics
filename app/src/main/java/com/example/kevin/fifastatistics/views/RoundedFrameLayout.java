package com.example.kevin.fifastatistics.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.example.kevin.fifastatistics.R;

public class RoundedFrameLayout extends FrameLayout {

    protected static final int TOP_LEFT_RADIUS_INDEX = 0;
    protected static final int TOP_RIGHT_RADIUS_INDEX = 2;
    protected static final int BOTTOM_RIGHT_RADIUS_INDEX = 4;
    protected static final int BOTTOM_LEFT_RADIUS_INDEX = 6;
    private static final float DEFAULT_RADIUS = 0f;

    private Bitmap mMaskBitmap;
    private Bitmap mOffscreenBitmap;
    private RectF mRect;
    private final float[] mRadii = new float[8];

    public RoundedFrameLayout(Context context) {
        this(context, null, 0);
    }

    public RoundedFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundedFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        mRect = new RectF();
        initRadii(context, attrs, defStyle);
        setWillNotDraw(false);
    }

    protected void initRadii(Context context, AttributeSet attrs, int defStyle) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundedFrameLayout, defStyle, 0);
        try {
            setRadiiValues(ta.getDimension(R.styleable.RoundedFrameLayout_radiusTopLeft,
                    DEFAULT_RADIUS), TOP_LEFT_RADIUS_INDEX);
            setRadiiValues(ta.getDimension(R.styleable.RoundedFrameLayout_radiusTopRight,
                    DEFAULT_RADIUS), TOP_RIGHT_RADIUS_INDEX);
            setRadiiValues(ta.getDimension(R.styleable.RoundedFrameLayout_radiusBottomRight,
                    DEFAULT_RADIUS), BOTTOM_RIGHT_RADIUS_INDEX);
            setRadiiValues(ta.getDimension(R.styleable.RoundedFrameLayout_radiusBottomLeft,
                    DEFAULT_RADIUS), BOTTOM_LEFT_RADIUS_INDEX);
        } finally {
            ta.recycle();
        }
    }

    protected final void setRadiiValues(float value, int index) {
        mRadii[index] = value;
        mRadii[index + 1] = value;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mOffscreenBitmap == null) {
            mOffscreenBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        Canvas offscreenCanvas = new Canvas(mOffscreenBitmap);
        super.draw(offscreenCanvas);
        createBitmapMask(canvas.getWidth(), canvas.getHeight());
        maskOffscreenBitmap(offscreenCanvas);
        drawOffscreenBitmapToCanvas(canvas);
    }

    private void createBitmapMask(int width, int height) {
        if (mMaskBitmap == null) {
            mMaskBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ALPHA_8);
        }
        Canvas canvas = new Canvas(mMaskBitmap);
        Path borderPath = createBorderPath(width, height);
        drawBitmapMask(canvas, borderPath);
    }

    private Path createBorderPath(int width, int height) {
        Path borderPath = new Path();
        mRect.set(0, 0, width, height);
        borderPath.addRoundRect(mRect, mRadii, Path.Direction.CCW);
        return borderPath;
    }

    private void drawBitmapMask(Canvas canvas, Path borderPath) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        canvas.drawRect(mRect, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawPath(borderPath, paint);
    }

    private void maskOffscreenBitmap(Canvas offscreenCanvas) {
        Paint maskPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        offscreenCanvas.drawBitmap(mMaskBitmap, 0f, 0f, maskPaint);
    }

    private void drawOffscreenBitmapToCanvas(Canvas realCanvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        realCanvas.drawBitmap(mOffscreenBitmap, 0f, 0f, paint);
    }
}
