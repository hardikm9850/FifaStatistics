package com.example.kevin.fifastatistics.viewmodels;

import android.databinding.Bindable;
import android.view.View;

import com.example.kevin.fifastatistics.FifaApplication;
import com.example.kevin.fifastatistics.viewmodels.fragment.ProgressFragmentViewModel;

public abstract class FooterButtonsViewModel extends ProgressFragmentViewModel {

    public abstract String getRightButtonText();

    public abstract void onRightButtonClick(View button);

    public int getButtonTextColor() {
        return FifaApplication.getAccentColor();
    }

    public String getLeftButtonText() {
        return null;
    }

    public int getLeftButtonVisibility() {
        return View.GONE;
    }

    public void onLeftButtonClick(View button) {}

    @Bindable
    public int getFooterVisibility() {
        return View.VISIBLE;
    }
}
