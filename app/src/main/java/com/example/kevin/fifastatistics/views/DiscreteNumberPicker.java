package com.example.kevin.fifastatistics.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.BindingAdapter;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.listeners.SimpleOnSeekBarChangeListener;

public class DiscreteNumberPicker extends LinearLayout {

    private static final int MESSAGE_ID = R.string.best_of;
    private static final int DEFAULT_MIN = 0;
    private static final int DEFAULT_MAX = 99;
    private static final int DEFAULT_STEP = 1;

    private OnNumberChangedListener listener;
    private TextView label;
    private SeekBar seekBar;
    private String numberText;
    private int min;
    private int max;
    private int step;
    private int currentValue;

    public DiscreteNumberPicker(Context context) {
        this(context, null);
    }

    public DiscreteNumberPicker(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DiscreteNumberPicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public DiscreteNumberPicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        View root = initView(context);
        initAttributes(context, attrs, defStyleAttr, defStyleRes, root);
        initSeekbar(root);
    }

    private View initView(Context context) {
        setOrientation(HORIZONTAL);
        View root = inflate(context, R.layout.widget_number_picker, this);
        label = root.findViewById(R.id.text);
        return root;
    }

    private void initAttributes(Context context, AttributeSet attrs, int defStyle, int defStyleRes, View root) {
        TypedArray ta = context. obtainStyledAttributes(attrs, R.styleable.DiscreteNumberPicker, defStyle, defStyleRes);
        try {
            min = ta.getInt(R.styleable.DiscreteNumberPicker_min, DEFAULT_MIN);
            max = ta.getInt(R.styleable.DiscreteNumberPicker_max, DEFAULT_MAX);
            step = ta.getInt(R.styleable.DiscreteNumberPicker_step, DEFAULT_STEP);
            int start = ta.getInt(R.styleable.DiscreteNumberPicker_start, min - 1);
            processStart(start);
            verifyAttributes();
            numberText = ta.getString(R.styleable.DiscreteNumberPicker_textLabel);
        } finally {
            ta.recycle();
        }
    }

    private void processStart(int start) {
        if (start >= min && start <= max) {
            setSeekbarValue();
        }
    }

    private void verifyAttributes() {
        if (min > max) {
            throw new IllegalArgumentException("min attribute cannot be greater than max");
        }
    }

    private void initSeekbar(View root) {
        seekBar = root.findViewById(R.id.seekbar);
        seekBar.setMin(min);
        seekBar.setMax((int)Math.ceil(((double) (max + min)) / step));
        seekBar.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int offset = (min*step) - min;
                currentValue = progress*step - offset;
                setLabel();
                notifyChange();
            }
        });
    }

    private void notifyChange() {
        if (listener != null) {
            listener.onNumberChanged(currentValue);
        }
    }

    public void setOnNumberChangedListener(OnNumberChangedListener listener) {
        this.listener = listener;
    }

    public void setStartValue(int start) {
        currentValue = Math.min(Math.max(start, min), max);
        setSeekbarValue();
        setLabel();
    }

    private void setLabel() {
        String text = getContext().getString(MESSAGE_ID, currentValue);
        label.setText(text);
    }

    private void setSeekbarValue() {
        int seekbarValue = (currentValue + (min*step - min)) / step;
        seekBar.setProgress(seekbarValue);
    }

    public interface OnNumberChangedListener {
        void onNumberChanged(int newNumber);
    }

    @BindingAdapter("numberChangedListener")
    public static void setChangeListener(DiscreteNumberPicker picker, OnNumberChangedListener listener) {
        picker.setOnNumberChangedListener(listener);
    }

    @BindingAdapter("start")
    public static void setStart(DiscreteNumberPicker picker, int start) {
        picker.setStartValue(start);
    }
}
