package com.example.kevin.fifastatistics.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.interfaces.RadioCheckable;

import java.util.ArrayList;
import java.util.List;

public class ThemeRadioButton extends LinearLayout implements RadioCheckable {

    private String mTitle;
    private TextView mIsSelectedText;
    private OnClickListener mOnClickListener;
    private OnTouchListener mOnTouchListener;
    private List<OnCheckedChangeListener> mOnCheckedChangeListeners = new ArrayList<>();
    private int mThemeRes;
    private boolean mChecked;

    public ThemeRadioButton(Context context) {
        this(context, null);
    }

    public ThemeRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThemeRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ThemeRadioButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttributes(context, attrs, defStyleAttr, defStyleRes);
        initLayout(context);
        setCustomTouchListener();
    }

    private void initAttributes(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ThemeRadioButton, defStyleAttr, defStyleRes);
        try {
            mTitle = ta.getString(R.styleable.ThemeRadioButton_text);
            mThemeRes = ta.getResourceId(R.styleable.ThemeRadioButton_buttonTheme, 0);
        } finally {
            ta.recycle();
        }
        if (mThemeRes == 0) {
            throw new IllegalStateException("No theme is specified for ThemeRadioButton");
        }
    }

    private void initLayout(Context context) {
        setOrientation(VERTICAL);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.radio_button_theme, this, true);
        mIsSelectedText = findViewById(R.id.checked_text);
        TextView title = findViewById(R.id.theme_text);
        title.setText(mTitle);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        mOnClickListener = l;
    }

    private void setCustomTouchListener() {
        super.setOnTouchListener(new TouchListener());
    }

    @Override
    public void setOnTouchListener(OnTouchListener onTouchListener) {
        mOnTouchListener = onTouchListener;
    }

    @Override
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            if (!mOnCheckedChangeListeners.isEmpty()) {
                for (int i = 0; i < mOnCheckedChangeListeners.size(); i++) {
                    mOnCheckedChangeListeners.get(i).onCheckedChanged(this, mChecked);
                }
            }
        }
        mIsSelectedText.setVisibility(mChecked ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    @Override
    public void addOnCheckChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        mOnCheckedChangeListeners.add(onCheckedChangeListener);
    }

    @Override
    public void removeOnCheckChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        mOnCheckedChangeListeners.remove(onCheckedChangeListener);
    }

    public int getTheme() {
        return mThemeRes;
    }

    public String getText() {
        return mTitle;
    }

    private final class TouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    setChecked(true);
                    break;
                case MotionEvent.ACTION_UP:
                    if (mOnClickListener != null) {
                        mOnClickListener.onClick(ThemeRadioButton.this);
                    }
                    break;
            }
            if (mOnTouchListener != null) {
                mOnTouchListener.onTouch(v, event);
            }
            return true;
        }
    }
}
