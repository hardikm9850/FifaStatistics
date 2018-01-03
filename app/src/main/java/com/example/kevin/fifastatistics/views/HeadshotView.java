package com.example.kevin.fifastatistics.views;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kevin.fifastatistics.FifaApplication;
import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.animation.CircularAnimationHelper;
import com.example.kevin.fifastatistics.interfaces.AnimationListener;
import com.example.kevin.fifastatistics.listeners.SimpleImageCallback;
import com.example.kevin.fifastatistics.models.databasemodels.footballers.Footballer;
import com.example.kevin.fifastatistics.models.databasemodels.footballers.MatchEvents;
import com.example.kevin.fifastatistics.views.databinding.ImageBindingAdapter;
import com.nostra13.universalimageloader.core.assist.FailReason;

public class HeadshotView extends RoundedFrameLayout {

    private static final float CIRCLE_RADIUS;

    static {
        Resources resources = FifaApplication.getContext().getResources();
        CIRCLE_RADIUS = resources.getDimension(R.dimen.headshot_radius);
    }

    private ImageView mPrimaryBackground;
    private ImageView mSecondaryBackground;
    private ImageView mPhoto;
    private TextView mText;
    private OnColorAnimationCompleteListener mListener;
    private Context mContext;
    private boolean mIsAnimating;

    public HeadshotView(Context context) {
        this(context, null);
    }

    public HeadshotView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeadshotView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View root = inflate(context, R.layout.headshot_view, this);
        mPrimaryBackground = (ImageView) root.findViewById(R.id.headshot_primary_background);
        mSecondaryBackground = (ImageView) root.findViewById(R.id.headshot_secondary_background);
        mSecondaryBackground.setColorFilter(FifaApplication.getAccentColor());
        mPhoto = (ImageView) root.findViewById(R.id.headshot_photo);
        mText = (TextView) root.findViewById(R.id.headshot_textview);
        mContext = context;
    }

    @Override
    protected void initRadii(Context context, AttributeSet attrs, int defStyle) {
        setRadiiValues(CIRCLE_RADIUS, BOTTOM_RIGHT_RADIUS_INDEX);
        setRadiiValues(CIRCLE_RADIUS, BOTTOM_LEFT_RADIUS_INDEX);
    }

    public void setColor(@ColorInt int color) {
        if (!mIsAnimating) {
            mSecondaryBackground.setColorFilter(color);
            CircularAnimationHelper.revealOpenCenter(mSecondaryBackground, mContext, new AnimationListener() {
                @Override
                public void onAnimationStarted() {
                }

                @Override
                public void onAnimationCompleted() {
                    mPrimaryBackground.setColorFilter(color);
                    mSecondaryBackground.setVisibility(View.GONE);
                    mIsAnimating = false;
                    if (mListener != null) {
                        mListener.onAnimationCompleted();
                    }
                }

                @Override
                public void onAnimationCanceled() {
                }
            });
        }
    }

    public void setFootballer(MatchEvents.DummyPlayer footballer) {
        ImageBindingAdapter.loadImage(mPhoto, footballer.getHeadshotImgUrl(), new
                SimpleImageCallback() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        mText.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        mText.setVisibility(View.VISIBLE);
                    }
                }, 0);
        mText.setText(footballer.getInitials());
    }

    public void setAnimationListener(OnColorAnimationCompleteListener listener) {
        mListener = listener;
    }

    public interface OnColorAnimationCompleteListener {
        void onAnimationCompleted();
    }

    @BindingAdapter(value = "footballer")
    public static void bindFootballer(HeadshotView view, MatchEvents.DummyPlayer footballer) {
        if (footballer != null) {
            view.setFootballer(footballer);
        }
    }

    @BindingAdapter(value = "color")
    public static void bindColor(HeadshotView view, @ColorInt int color) {
        view.setColor(color);
    }

    @BindingAdapter(value = "onColorAnimationComplete")
    public static void bindListener(HeadshotView view, OnColorAnimationCompleteListener listener) {
        view.setAnimationListener(listener);
    }
}
