package com.example.kevin.fifastatistics.viewmodels;

import android.content.res.ColorStateList;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.support.percent.PercentLayoutHelper;
import android.view.View;

public class StatItemViewModel extends BaseObservable {

    private static final float MIDDLE_PADDING = 0.0125f;

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
        return ((mLeftValue + mRightValue == 0) ? 0.5f : mLeftValue/(mLeftValue + mRightValue)) - MIDDLE_PADDING;
    }

    @Bindable
    public float getRightPercentage() {
        return 1 - getLeftPercentage() - 2*MIDDLE_PADDING;
    }

    @BindingAdapter("app:layout_widthPercent")
    public static void setWidthPercent(View view, float width) {
        PercentLayoutHelper.PercentLayoutParams params =(PercentLayoutHelper.PercentLayoutParams) view.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo info = params.getPercentLayoutInfo();
        info.widthPercent = width;
    }
}
