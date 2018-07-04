package com.example.kevin.fifastatistics.interfaces;

import android.view.View;
import android.widget.Checkable;

public interface RadioCheckable extends Checkable {
    void addOnCheckChangeListener(OnCheckedChangeListener onCheckedChangeListener);
    void removeOnCheckChangeListener(OnCheckedChangeListener onCheckedChangeListener);

    interface OnCheckedChangeListener {
        void onCheckedChanged(View radioGroup, boolean isChecked);
    }
}
