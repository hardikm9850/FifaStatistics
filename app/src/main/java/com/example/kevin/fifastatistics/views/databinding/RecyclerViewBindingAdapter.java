package com.example.kevin.fifastatistics.views.databinding;

import android.databinding.BindingAdapter;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import static android.support.v7.widget.RecyclerView.ViewHolder;

public class RecyclerViewBindingAdapter {

    @BindingAdapter("adapter")
    public static void setAdapter(RecyclerView view, RecyclerView.Adapter<? extends ViewHolder> adapter) {
        if (view != null) {
            view.setAdapter(adapter);
        }
    }

    @BindingAdapter("addDividers")
    public static void addDividers(RecyclerView view, boolean showDividers) {
        if (view != null && showDividers) {
            view.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        }
    }
}
