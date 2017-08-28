package com.example.kevin.fifastatistics.viewmodels.fragment;

import com.example.kevin.fifastatistics.databinding.CardUpdateStatsBinding;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.viewmodels.FifaBaseViewModel;
import com.example.kevin.fifastatistics.viewmodels.card.CreateStatsCardViewModel;
import com.example.kevin.fifastatistics.viewmodels.card.MatchStatsViewModel;
import com.example.kevin.fifastatistics.viewmodels.card.UpdateStatsCardViewModel;

public class CreateMatchFragmentViewModel extends FifaBaseViewModel
        implements CreateStatsCardViewModel.CreateStatsInteraction {

    private CreateStatsCardViewModel mStatsViewModel;
    private CreateMatchViewModelInteraction mInteraction;

    public CreateMatchFragmentViewModel(User user, Match match, CardUpdateStatsBinding binding,
                                        CreateMatchViewModelInteraction interaction) {
        mStatsViewModel = new CreateStatsCardViewModel(match, user, binding, this);
        mInteraction = interaction;
    }

    @Override
    public void destroy() {
        super.destroy();
        mStatsViewModel.destroy();
        mInteraction = null;
    }

    @Override
    public void onGoalCountChanged(int count) {

    }

    @Override
    public void onCardCountChanged(int count) {

    }

    @Override
    public void onInjuryCountChanged(int count) {

    }

    @Override
    public void onMatchUpdated(Match match) {
        if (mInteraction != null) {
            mInteraction.onMatchUpdated(match);
        }
    }

    public MatchStatsViewModel getStatsCardViewModel() {
        return mStatsViewModel;
    }

    public interface CreateMatchViewModelInteraction {
        void onMatchUpdated(Match match);
    }
}
