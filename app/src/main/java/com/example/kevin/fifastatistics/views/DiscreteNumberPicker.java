package com.example.kevin.fifastatistics.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.BindingAdapter;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.kevin.fifastatistics.FifaApplication;
import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.animation.AnimationHelper;
import com.example.kevin.fifastatistics.listeners.SimpleOnSeekBarChangeListener;
import com.example.kevin.fifastatistics.utils.ColorUtils;

import java.util.NavigableSet;
import java.util.TreeSet;

public class DiscreteNumberPicker extends LinearLayout {

    private static final int DEFAULT_MIN = 0;
    private static final int DEFAULT_MAX = 99;
    private static final int DEFAULT_STEP = 1;
    private static final int STEP_MULTIPLER = 10;

    private OnNumberChangedListener listener;
    private TextView numberLabel;
    private SeekBar seekBar;
    private NavigableSet<Integer> values;
    private int min;
    private int max;
    private int step;
    private int currentValue;
    private int lowerThreshold;

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
        initAttributes(context, attrs, defStyleAttr, defStyleRes);
        initSeekbar(root);
    }

    private View initView(Context context) {
        setOrientation(HORIZONTAL);
        View root = inflate(context, R.layout.widget_number_picker, this);
        numberLabel = root.findViewById(R.id.number_textview);
        return root;
    }

    private void initAttributes(Context context, AttributeSet attrs, int defStyle, int defStyleRes) {
        TypedArray ta = context. obtainStyledAttributes(attrs, R.styleable.DiscreteNumberPicker, defStyle, defStyleRes);
        try {
            max = ta.getInt(R.styleable.DiscreteNumberPicker_max, DEFAULT_MAX);
            min = ta.getInt(R.styleable.DiscreteNumberPicker_min, DEFAULT_MIN);
            step = ta.getInt(R.styleable.DiscreteNumberPicker_step, DEFAULT_STEP);
            lowerThreshold = min;
            int start = ta.getInt(R.styleable.DiscreteNumberPicker_start, min - 1);
            processStart(start);
            verifyAttributes();
            initValues();
        } finally {
            ta.recycle();
        }
    }

    private void processStart(int start) {
        if (start >= min && start <= max) {
            setLabel();
            setSeekbarValue();
        }
    }

    private void verifyAttributes() {
        if (min > max) {
            throw new IllegalArgumentException("min attribute cannot be greater than max");
        }
    }

    private void initValues() {
        int size = ((max - min)/step) + 1;
        values = new TreeSet<>();
        values.add(0);
        for (int i = 0; i < size; i++) {
            values.add(i*step*STEP_MULTIPLER);
        }
    }

    private void initSeekbar(View root) {
        seekBar = root.findViewById(R.id.seekbar);
        seekBar.setMax(max*STEP_MULTIPLER - min*STEP_MULTIPLER);
        seekBar.setMin(0);
        seekBar.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int thresholdMin = (lowerThreshold - min)*STEP_MULTIPLER;
                if (progress < thresholdMin) {
                    seekBar.setProgress(thresholdMin);
                } else {
                    currentValue = getClosestValueTo(progress);
                    setLabel();
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                focusNumberLabel();
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setSeekbarValue();
                unfocusNumberLabel();
                notifyChange();
            }
        });
    }

    private void notifyChange() {
        if (listener != null) {
            listener.onNumberPickerNumberChanged(currentValue);
        }
    }

    private int getClosestValueTo(int value) {
        int floor = values.floor(value);
        int ceiling = values.ceiling(value);
        int lowerDistance = Math.abs(value - floor);
        int higherDistance = Math.abs(value - ceiling);
        return ((lowerDistance < higherDistance ? floor : ceiling)/STEP_MULTIPLER) + min;
    }

    private void focusNumberLabel() {
        AnimationHelper.scaleIn(numberLabel);
        numberLabel.setTypeface(null, Typeface.BOLD);
        numberLabel.setTextColor(FifaApplication.getAccentColor());
    }

    private void unfocusNumberLabel() {
        AnimationHelper.scaleOut(numberLabel);
        numberLabel.setTypeface(null, Typeface.NORMAL);
        numberLabel.setTextColor(ColorUtils.getPrimaryTextColor(getContext()));
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
        String text = String.valueOf(currentValue);
        numberLabel.setText(text);
    }

    private void setSeekbarValue() {
        int seekbarValue = (currentValue - min)*STEP_MULTIPLER;
        seekBar.setProgress(seekbarValue);
    }

    public void setLowerThreshold(int lowerThreshold) {
        if (lowerThreshold >= min) {
            this.lowerThreshold = lowerThreshold;
        }
    }

    public interface OnNumberChangedListener {
        void onNumberPickerNumberChanged(int newNumber);
    }

    @BindingAdapter("numberChangedListener")
    public static void setChangeListener(DiscreteNumberPicker picker, OnNumberChangedListener listener) {
        picker.setOnNumberChangedListener(listener);
    }

    @BindingAdapter("start")
    public static void setStart(DiscreteNumberPicker picker, int start) {
        picker.setStartValue(start);
    }

    @BindingAdapter("lowerThreshold")
    public static void setLowerThreshold(DiscreteNumberPicker picker, int threshold) {
        picker.setLowerThreshold(threshold);
    }
}
