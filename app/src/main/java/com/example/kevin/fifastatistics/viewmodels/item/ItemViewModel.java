package com.example.kevin.fifastatistics.viewmodels.item;

import android.databinding.Bindable;
import android.support.annotation.ColorRes;
import android.view.View;

import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchUpdate;
import com.example.kevin.fifastatistics.viewmodels.FifaBaseViewModel;

public abstract class ItemViewModel<T> extends FifaBaseViewModel {

    protected T mItem;
    protected ActivityLauncher mLauncher;
    private int mColor;
    private boolean mIsLastItem;

    public ItemViewModel(T item, ActivityLauncher launcher,
                         @ColorRes int color, boolean isLastItem) {
        mItem = item;
        mLauncher = launcher;
        mColor = color;
        mIsLastItem = isLastItem;
    }

    public void setItem(T t) {
        mItem = t;
        notifyChange();
    }

    @Bindable
    public int getColor() {
        return mColor;
    }

    @Bindable
    public int getDividerVisibility() {
        return mIsLastItem ? View.GONE : View.VISIBLE;
    }

    @Override
    public void destroy() {
        super.destroy();
        mLauncher = null;
    }
}
