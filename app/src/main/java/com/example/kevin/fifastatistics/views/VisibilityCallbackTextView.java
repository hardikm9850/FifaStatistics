package com.example.kevin.fifastatistics.views;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;

public class VisibilityCallbackTextView extends AppCompatTextView {

    private OnVisibilityChangedListener mListener;

    public VisibilityCallbackTextView(Context context) {
        super(context);
    }

    public VisibilityCallbackTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VisibilityCallbackTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (mListener != null && changedView.equals(this)) {
            mListener.onVisibilityChanged(changedView, visibility);
        }
    }

    public void setOnVisibilityChangedListener(OnVisibilityChangedListener listener) {
        mListener = listener;
    }

    @BindingAdapter("onVisibilityChanged")
    public static void setVisibilityChangedListener(VisibilityCallbackTextView view,
                                                    OnVisibilityChangedListener listener) {
        view.setOnVisibilityChangedListener(listener);
    }

    public interface OnVisibilityChangedListener {
        void onVisibilityChanged(View view, int visibility);
    }
}

