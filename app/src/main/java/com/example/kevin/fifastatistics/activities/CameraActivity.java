package com.example.kevin.fifastatistics.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.databinding.ActivityCameraBinding;
import com.example.kevin.fifastatistics.interfaces.MatchFactsPreprocessor;
import com.example.kevin.fifastatistics.managers.MatchFactsPreprocessor15;
import com.example.kevin.fifastatistics.utils.ByteHolder;
import com.flurgle.camerakit.CameraListener;
import com.flurgle.camerakit.CameraView;

public class CameraActivity extends AppCompatActivity {

    public static final int PICTURE_TAKEN_RESULT_CODE = 6173;
    public static final String EXTRA_PICTURE = "extraPicture";
    public static final String EXTRA_PREPROCESSOR = "extraPreprocessor";

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

    private void initShutter(ActivityCameraBinding binding) {
        binding.shutter.setOnClickListener(view -> mCamera.captureImage());
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
