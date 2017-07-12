package com.example.kevin.fifastatistics.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.databinding.ActivityCameraBinding;
import com.example.kevin.fifastatistics.interfaces.MatchFactsPreprocessor;
import com.example.kevin.fifastatistics.managers.MatchFactsPreprocessor15;
import com.example.kevin.fifastatistics.utils.ByteHolder;
import com.example.kevin.fifastatistics.utils.UiUtils;
import com.flurgle.camerakit.CameraListener;
import com.flurgle.camerakit.CameraView;

public class CameraActivity extends AppCompatActivity {

    public static final int PICTURE_TAKEN_RESULT_CODE = 6173;
    public static final String EXTRA_PICTURE = "extraPicture";
    public static final String EXTRA_PREPROCESSOR = "extraPreprocessor";
    private static final float CAMERA_VIEW_RATIO = 20.000f/11.000f;

    private CameraView mCamera;
    private Preprocessor mPreprocessor;

    public enum Preprocessor {
        PREPROCESSOR_15 {
            @Override
            public MatchFactsPreprocessor getPreprocessor() {
                return new MatchFactsPreprocessor15();
            }
        },
        PREPROCESSOR_17 {
            @Override
            public MatchFactsPreprocessor getPreprocessor() {
                return new MatchFactsPreprocessor15();
            }
        };

        public abstract MatchFactsPreprocessor getPreprocessor();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCameraBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_camera);
        mPreprocessor = Preprocessor.PREPROCESSOR_17;
        initShutter(binding);
        initCamera(binding);
    }

    private void initShutter(final ActivityCameraBinding binding) {
        binding.shutter.setOnClickListener(view -> {
            binding.shutter.setClickable(false);
            mCamera.captureImage();
        });
    }

    private void initCamera(ActivityCameraBinding binding) {
        mCamera = binding.camera;
        mCamera.setCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(byte[] picture) {
                setResult();
                ByteHolder.setImage(picture);
                finish();
            }
        });
        adjustCameraViewWidth(binding.shutterContainer);
    }

    private void adjustCameraViewWidth(View shutterContainer) {
        mCamera.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mCamera.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                ViewGroup.LayoutParams params = mCamera.getLayoutParams();
                params.width = getWidthThatFitsAspectRatio();
                mCamera.setLayoutParams(params);
                mCamera.requestLayout();
            }

            private int getWidthThatFitsAspectRatio() {
                float maxWidth = UiUtils.getScreenWidth(CameraActivity.this) - shutterContainer.getWidth();
                float desiredWidth = CAMERA_VIEW_RATIO * mCamera.getHeight();
                Log.d("CAMERA", "maxW: " + maxWidth + ", desired: " + desiredWidth);
                return Math.round(desiredWidth > maxWidth ? maxWidth : desiredWidth);
            }
        });
    }

    private void setResult() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PREPROCESSOR, mPreprocessor.toString());
        setResult(PICTURE_TAKEN_RESULT_CODE, intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCamera.start();
    }

    @Override
    protected void onPause() {
        mCamera.stop();
        super.onPause();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // ignore orientation change
        super.onConfigurationChanged(newConfig);
    }
}
