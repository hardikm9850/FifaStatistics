package com.example.kevin.fifastatistics.adapters;

import android.content.res.ColorStateList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kevin.fifastatistics.FifaApplication;
import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.models.databasemodels.user.Stats;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.utils.EventBus;

import java.util.Locale;

public class StatsRecyclerViewAdapter extends RecyclerView.Adapter<StatsRecyclerViewAdapter.ViewHolder> {

    private float[] valuesFor;
    private float[] valuesAgainst;
    private String[] names;
    private String floatFormat;
    private int mColor;

    public StatsRecyclerViewAdapter(User.StatsPair statsPair, boolean doShowDecimal) {
        this.valuesFor = statsPair.getStatsFor().buildValueSet();
        this.valuesAgainst = statsPair.getStatsAgainst().buildValueSet();
        floatFormat = doShowDecimal ? "%.1f" : "%.0f";
        names = Stats.getNameSet();
        initColor();
    }

    private void initColor() {
        mColor = FifaApplication.getAccentColor();
        EventBus.getInstance().observeEvents(Integer.class).subscribe(color -> {
            mColor = color;
            notifyDataSetChanged();
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate( R.layout.stats_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        float vf = valuesFor[position];
        float va = valuesAgainst[position];
        int percent = (vf + va == 0) ? 50 : (int)((vf * 100.0f) / (vf + va));
        holder.mProgressBar.setProgressTintList(ColorStateList.valueOf(mColor));
        holder.mProgressBar.setProgress(percent);
        holder.mStatFor.setText(String.format(floatFormat, vf));
        holder.mStatAgainst.setText(String.format(floatFormat, va));
        holder.mTitle.setText(names[position]);
    }

    @Override
    public int getItemCount() {
        return valuesFor.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ProgressBar mProgressBar;
        final TextView mTitle;
        final TextView mStatFor;
        final TextView mStatAgainst;

        ViewHolder(View view) {
            super(view);
            mProgressBar = (ProgressBar) view.findViewById(R.id.stats_progress_bar);
            mTitle = (TextView) view.findViewById(R.id.stat_title_text_view);
            mStatFor = (TextView) view.findViewById(R.id.stat_for_text_view);
            mStatAgainst = (TextView) view.findViewById(R.id.stat_against_text_view);
        }
    }
}
