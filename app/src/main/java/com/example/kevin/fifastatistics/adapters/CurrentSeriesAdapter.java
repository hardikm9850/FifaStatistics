package com.example.kevin.fifastatistics.adapters;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.databinding.ItemCurrentSeriesBinding;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.models.databasemodels.match.CurrentSeries;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.viewmodels.item.CurrentSeriesItemViewModel;

import java.util.List;

public class CurrentSeriesAdapter extends AbstractCardAdapter<
        CurrentSeries, ItemCurrentSeriesBinding, CurrentSeriesItemViewModel> {

    private Player mCurrentUser;

    public CurrentSeriesAdapter(List<CurrentSeries> items, ActivityLauncher launcher, Player currentUser) {
        super(items, launcher, R.layout.item_current_series);
        mCurrentUser = currentUser;
    }

    @Override
    protected CurrentSeriesItemViewModel getBindingViewModel(ItemCurrentSeriesBinding binding) {
        return binding.getViewModel();
    }

    @Override
    protected CurrentSeriesItemViewModel createViewModel(CurrentSeries item, ActivityLauncher launcher, boolean isLastItem, int color) {
        return new CurrentSeriesItemViewModel(item, launcher, color, isLastItem, mCurrentUser);
    }

    public void setCurrentUser(Player user) {
        mCurrentUser = user;
    }
}
