package com.example.kevin.fifastatistics.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.databinding.StatsListViewLayoutBinding;
import com.example.kevin.fifastatistics.models.databasemodels.user.Stats;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.viewmodels.StatsCardViewModel;

import java.util.List;

public class StatsPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<User.StatsPair> mStats;
    private String mUsername;
    private boolean mIsMyStats;

    public StatsPagerAdapter(Context context, List<User.StatsPair> stats, String username) {
        mContext = context;
        mStats = stats;
        mUsername = username;
        mIsMyStats = username != null;
    }

    @Override
    public int getCount() {
        return mStats.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        StatsListViewLayoutBinding binding = DataBindingUtil.inflate(inflater, R.layout.stats_list_view_layout, collection, false);
        initBinding(binding, position);
        collection.addView(binding.getRoot());
        return binding.getRoot();
    }

    private void initBinding(StatsListViewLayoutBinding binding, int position) {
        StatsCardViewModel viewModel = getViewModelForPosition(position);
        binding.setViewModel(viewModel);
        binding.statsRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        binding.statsRecyclerView.setHasFixedSize(true);
        binding.statsRecyclerView.setAdapter(new StatsRecyclerViewAdapter(mStats.get(position), isAverages(position)));
    }

    private boolean isAverages(int position) {
        return position == 0;
    }

    private StatsCardViewModel getViewModelForPosition(int position) {
        Stats.Type type = isAverages(position) ? Stats.Type.AVERAGES: Stats.Type.RECORDS;
        if (mIsMyStats) {
            return StatsCardViewModel.myStats(mContext, type);
        } else {
            return StatsCardViewModel.playerStats(mContext, type, mUsername);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}

