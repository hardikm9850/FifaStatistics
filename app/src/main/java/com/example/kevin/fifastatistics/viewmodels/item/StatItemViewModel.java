package com.example.kevin.fifastatistics.viewmodels.item;

import android.content.res.ColorStateList;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.support.percent.PercentLayoutHelper;
import android.view.View;

import com.example.kevin.fifastatistics.models.databasemodels.user.Stats;

import java.util.HashSet;
import java.util.Set;

public class StatItemViewModel extends BaseObservable {

    private static final float MIDDLE_PADDING = 0.006125f;
    private static final Set<String> INVERTS;

    static {
        INVERTS = new HashSet<>(5);
        INVERTS.add(Stats.FOULS);
        INVERTS.add(Stats.YELLOW_CARDS);
        INVERTS.add(Stats.RED_CARDS);
        INVERTS.add(Stats.INJURIES);
        INVERTS.add(Stats.OFFSIDES);
    }

    private String mTitle;
    private String mFloatFormat;
    private ColorStateList mLeftColor;
    private ColorStateList mRightColor;
    private float mLeftValue;
    private float mRightValue;

    public StatItemViewModel(float leftValue, float rightValue, int leftColor, int rightColor, String title, boolean doShowDecimal) {
        init(leftValue, rightValue, leftColor, rightColor, title);
        mFloatFormat = doShowDecimal ? "%.1f" : "%.0f";
    }

    public void init(float leftValue, float rightValue, int leftColor, int rightColor, String title) {
        mLeftValue = leftValue;
        mRightValue = rightValue;
        mLeftColor = ColorStateList.valueOf(leftColor);
        mRightColor = ColorStateList.valueOf(rightColor);
        mTitle = title;
        notifyChange();
    }

    @Bindable
    public String getTitle() {
        return mTitle;
    }

    @Bindable
    public String getLeftValue() {
        return String.format(mFloatFormat, mLeftValue);
    }

    @Bindable
    public String getRightValue() {
        return String.format(mFloatFormat, mRightValue);
    }

    @Bindable
    public ColorStateList getLeftColor() {
        return mLeftColor;
    }

    @Bindable
    public ColorStateList getRightColor() {
        return mRightColor;
    }

    @Bindable
    public float getLeftPercentage() {
        float left = mLeftValue;
        float right = mRightValue;
        if (shouldInvertBar()) {
            left = mRightValue;
            right = mLeftValue;
        }
        if (left + right == 0) {
            return 0.5f - MIDDLE_PADDING;
        } else if (right == 0) {
            return 1f;
        } else if (left == 0) {
            return 0f;
        } else {
            return left/(left + right) - MIDDLE_PADDING;
        }
    }

    private boolean shouldInvertBar() {
        return INVERTS.contains(mTitle);
    }

    @Bindable
    public float getRightPercentage() {
        float left = getLeftPercentage();
        float padding = left == 0f || left == 1f ? 0f : 2*MIDDLE_PADDING;
        return 1 - left - padding;
    }

    @BindingAdapter("app:layout_widthPercent")
    public static void setWidthPercent(View view, float width) {
        PercentLayoutHelper.PercentLayoutParams params = (PercentLayoutHelper.PercentLayoutParams) view.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo info = params.getPercentLayoutInfo();
        info.widthPercent = width;
    }
}
