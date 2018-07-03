package com.example.kevin.fifastatistics.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ThemePicker extends RecyclerView {

    private final ThemePickerAdapter mAdapter;
    private List<Integer> mThemes;
    private List<String> mNames;

    public ThemePicker(Context context) {
        this(context, null);
    }

    public ThemePicker(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThemePicker(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new ThemePickerAdapter();
        setAdapter(mAdapter);
    }

    public int getTheme() {
        int theme =
        return 0;
        //TODO
    }

    private static class ThemePickerAdapter extends RecyclerView.Adapter<ThemePickerAdapter.ThemeViewHolder> {

        @Override
        public ThemeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(ThemeViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        class ThemeViewHolder extends RecyclerView.ViewHolder {

            public ThemeViewHolder(View itemView) {
                super(itemView);
            }
        }
    }


}
