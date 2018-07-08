package com.example.kevin.fifastatistics.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.databinding.ItemSeriesMatchBinding;
import com.example.kevin.fifastatistics.databinding.ItemSeriesMatchesHeaderBinding;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.managers.StatsPresenter;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.match.Series;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.viewmodels.item.SeriesMatchItemViewModel;

import java.util.List;

import static com.example.kevin.fifastatistics.adapters.StickyEndlessProgressAdapter.BindingViewHolder;

public class SeriesMatchAdapter extends
        AbstractCardAdapter<Match, ItemSeriesMatchBinding, SeriesMatchItemViewModel> {

    private User mCurrentUser;
    private Series mSeries;

    public SeriesMatchAdapter(List<Match> matches, ActivityLauncher launcher) {
        super(matches, launcher, R.layout.item_series_match);
        setIncludeHeader(true);
    }

    public void setUser(User user) {
        mCurrentUser = user;
    }

    public void setSeries(Series series) {
        mSeries = series;
    }

    @Override
    protected SeriesMatchItemViewModel getBindingViewModel(ItemSeriesMatchBinding binding) {
        return binding.getViewModel();
    }

    @Override
    protected SeriesMatchItemViewModel createViewModel(Match item, ActivityLauncher launcher,
                                                       boolean isLastItem, int color, int position) {
        return new SeriesMatchItemViewModel(launcher, item, mCurrentUser, mSeries.getWinner(), position + 1, isLastItem);
    }

    @Override
    protected RecyclerView.ViewHolder getHeaderViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemSeriesMatchesHeaderBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_series_matches_header, parent, false);
        return new HeaderViewHolder(binding);
    }

    @Override
    protected <S extends RecyclerView.ViewHolder> void bindHeader(S header) {
        ((HeaderViewHolder) header).bindItem(mSeries);
    }

    private class HeaderViewHolder extends BindingViewHolder<ItemSeriesMatchesHeaderBinding, Series> {
        HeaderViewHolder(ItemSeriesMatchesHeaderBinding binding) {
            super(binding);
        }

        @Override
        void bindItem(Series item) {
            StatsPresenter presenter = new StatsPresenter(item.getTotalStats(), item, mCurrentUser.getName());
            mBinding.setHeaderLeft(presenter.getLeftHeader());
            mBinding.setHeaderRight(presenter.getRightHeader());
            mBinding.executePendingBindings();
        }
    }
}
