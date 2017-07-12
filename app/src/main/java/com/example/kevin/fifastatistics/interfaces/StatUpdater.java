package com.example.kevin.fifastatistics.interfaces;

import android.text.Editable;

public interface StatUpdater {
    void onStatForChanged(Editable s);
    void onStatAgainstChanged(Editable s);
}
