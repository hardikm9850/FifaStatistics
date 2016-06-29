package com.example.kevin.fifastatistics.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.models.databasemodels.user.Stats;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;

/**
 * Created by kevin on 2016-06-07.
 */
public class StatsRecyclerViewAdapter extends RecyclerView.Adapter<StatsRecyclerViewAdapter.ViewHolder> {

    private int[] valuesFor;
    private int[] valuesAgainst;

    public StatsRecyclerViewAdapter(User.StatsPair statsPair) {
        this.valuesFor = statsPair.getStatsFor().buildValueSet();
        this.valuesAgainst = statsPair.getStatsAgainst().buildValueSet();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate( R.layout.stats_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        int vf = valuesFor[position];
        int va = valuesAgainst[position];
        int percent = (vf + va == 0) ? 50 : (int)((vf * 100.0f) / (vf + va));
        holder.mProgressBar.setProgress(percent);
        holder.mStatFor.setText(String.valueOf(vf));
        holder.mStatAgainst.setText(String.valueOf(va));
        holder.mTitle.setText(Stats.getNameSet()[position]);
    }

    @Override
    public int getItemCount() {
        return valuesFor.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ProgressBar mProgressBar;
        public final TextView mTitle;
        public final TextView mStatFor;
        public final TextView mStatAgainst;

        public ViewHolder(View view) {
            super(view);
            mProgressBar = (ProgressBar) view.findViewById(R.id.stats_progress_bar);
            mTitle = (TextView) view.findViewById(R.id.stat_title_text_view);
            mStatFor = (TextView) view.findViewById(R.id.stat_for_text_view);
            mStatAgainst = (TextView) view.findViewById(R.id.stat_against_text_view);
        }
    }
}
