package com.example.kevin.fifastatistics.viewmodels.fragment;

import com.example.kevin.fifastatistics.databinding.CardUpdateStatsBinding;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.viewmodels.FifaBaseViewModel;
import com.example.kevin.fifastatistics.viewmodels.card.CreateStatsCardViewModel;
import com.example.kevin.fifastatistics.viewmodels.card.MatchStatsViewModel;

public class CreateMatchFragmentViewModel extends FifaBaseViewModel
        implements CreateStatsCardViewModel.CreateStatsInteraction {

    private CreateStatsCardViewModel mStatsViewModel;
    private CreateMatchViewModelInteraction mInteraction;

    public CreateMatchFragmentViewModel(User user, Match match, CardUpdateStatsBinding binding,
                                        CreateMatchViewModelInteraction interaction, ActivityLauncher launcher) {
        mStatsViewModel = new CreateStatsCardViewModel(match, user, binding, this, launcher);
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

    public boolean isValid() {
        return mStatsViewModel.areAllEditTextsFilled() && mStatsViewModel.validate();
    }

    public void autofill() {
        mStatsViewModel.autofill();
    }

    public void setStats(User.StatsPair stats) {
        mStatsViewModel.setStats(stats);
    }

    public MatchStatsViewModel getStatsCardViewModel() {
        return mStatsViewModel;
    }

    public interface CreateMatchViewModelInteraction {
        void onMatchUpdated(Match match);
    }
}
