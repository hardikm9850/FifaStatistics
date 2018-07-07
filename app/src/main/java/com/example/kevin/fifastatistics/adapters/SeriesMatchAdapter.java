package com.example.kevin.fifastatistics.adapters;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.databinding.ItemSeriesMatchBinding;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.viewmodels.item.SeriesMatchItemViewModel;

import java.util.List;

public class SeriesMatchAdapter extends
        AbstractCardAdapter<Match, ItemSeriesMatchBinding, SeriesMatchItemViewModel> {

    private User mCurrentUser;
    private Player mSeriesWinner;

    public SeriesMatchAdapter(List<Match> matches, ActivityLauncher launcher) {
        super(matches, launcher, R.layout.item_series_match);
    }

    public void setUser(User user) {
        mCurrentUser = user;
    }

    public void setSeriesWinner(Player player) {
        mSeriesWinner = player;
    }

    @Override
    protected SeriesMatchItemViewModel getBindingViewModel(ItemSeriesMatchBinding binding) {
        return binding.getViewModel();
    }

    @Override
    protected SeriesMatchItemViewModel createViewModel(Match item, ActivityLauncher launcher,
                                                       boolean isLastItem, int color, int position) {
        return new SeriesMatchItemViewModel(launcher, item, mCurrentUser, mSeriesWinner, position + 1, isLastItem);
    }
}
