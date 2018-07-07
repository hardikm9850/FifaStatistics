package com.example.kevin.fifastatistics.adapters;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.databinding.ItemMatchEventBinding;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.viewmodels.item.MatchEventItemViewModel;

import java.util.List;

import static com.example.kevin.fifastatistics.models.databasemodels.footballers.MatchEvents.MatchEventItem;

public class MatchEventCardAdapter<T extends MatchEventItem>
        extends AbstractCardAdapter<T, ItemMatchEventBinding, MatchEventItemViewModel<T>> {

    private Team mTeamWinner;
    private Team mTeamLoser;

    public MatchEventCardAdapter(List<T> items, ActivityLauncher launcher, Match match) {
        super(items, launcher, R.layout.item_match_event);
        initTeams(match);
    }

    private void initTeams(Match match) {
        mTeamWinner = match != null ? match.getTeamWinner() : null;
        mTeamLoser = match != null ? match.getTeamLoser() : null;
    }

    public void setMatch(Match match) {
        initTeams(match);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected MatchEventItemViewModel<T> getBindingViewModel(ItemMatchEventBinding binding) {
        return binding.getViewModel();
    }

    @Override
    protected MatchEventItemViewModel<T> createViewModel(T item, ActivityLauncher launcher, boolean
            isLastItem, int color, int position) {
        return new MatchEventItemViewModel<>(item, launcher, isLastItem, mTeamWinner, mTeamLoser);
    }
}
