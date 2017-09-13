package com.example.kevin.fifastatistics.viewmodels.fragment;

import android.os.Bundle;

import com.example.kevin.fifastatistics.databinding.CardUpdateStatsBinding;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.match.Penalties;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.viewmodels.CreateEventHeaderViewModel;
import com.example.kevin.fifastatistics.viewmodels.FifaBaseViewModel;
import com.example.kevin.fifastatistics.viewmodels.card.CreateStatsCardViewModel;
import com.example.kevin.fifastatistics.viewmodels.card.MatchStatsViewModel;

public class CreateMatchFragmentViewModel extends FifaBaseViewModel
        implements CreateStatsCardViewModel.CreateStatsInteraction, CreateEventHeaderViewModel.CreateEventHeaderInteraction {

    private CreateEventHeaderViewModel mHeaderViewModel;
    private CreateStatsCardViewModel mStatsViewModel;
    private CreateMatchViewModelInteraction mInteraction;
    private Match mMatch;
    private User mUser;

    public CreateMatchFragmentViewModel(User user, Player opponent, Match match, CardUpdateStatsBinding binding,
                                        CreateMatchViewModelInteraction interaction, ActivityLauncher launcher,
                                        Bundle savedInstanceState, boolean isPartOfSeries) {
        initMatch(match, user, opponent);
        mHeaderViewModel = new CreateEventHeaderViewModel(launcher, user, opponent, isPartOfSeries, savedInstanceState, this);
        mStatsViewModel = new CreateStatsCardViewModel(match, user, binding, this, launcher);
        mInteraction = interaction;
        mUser = user;
    }

    private void initMatch(Match match, Player user, Player opponent) {
        if (match == null) {
            mMatch = Match.builder()
                    .winner(Friend.fromPlayer(user))
                    .loser(Friend.fromPlayer(opponent))
                    .build();
            onMatchUpdated(mMatch);
        } else {
            mMatch = match;
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        mStatsViewModel.destroy();
        mHeaderViewModel.destroy();
        mInteraction = null;
    }

    @Override
    public Bundle saveInstanceState(Bundle bundle) {
        return mHeaderViewModel.saveInstanceState(bundle);
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
        mMatch.setStats(match.getStats());
        mMatch.setPenalties(match.getPenalties());
        if (mInteraction != null) {
            mInteraction.onMatchUpdated(mMatch);
        }
    }

    @Override
    public void onSidesSwapped() {
        mMatch.swapWinnerAndLoser();
        onMatchUpdated(mMatch);
    }

    @Override
    public void onLeftTeamChanged(Team team) {
        mMatch.setTeamWinner(team);
        onMatchUpdated(mMatch);
    }

    @Override
    public void onRightTeamChanged(Team team) {
        mMatch.setTeamLoser(team);
        onMatchUpdated(mMatch);
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

    public void finalizeMatch() {
        if (mMatch.getScoreWinner() != mMatch.getScoreLoser()) {
            mMatch.setPenalties(null);
        }
        float goalsLeft = mMatch.getStatsFor().getGoals();
        float goalsRight = mMatch.getStatsAgainst().getGoals();
        swapStatsIfLeftLost(goalsLeft, goalsRight, mMatch.getPenalties());
        swapWinnerIfChanged(goalsLeft, goalsRight, mMatch.getPenalties());
        onMatchUpdated(mMatch);
    }

    private void swapStatsIfLeftLost(float goalsLeft, float goalsRight, Penalties penalties) {
        if (goalsLeft < goalsRight) {
            mMatch.swapStats();
        } else if (penalties != null) {
            if (penalties.getWinner() < penalties.getLoser()) {
                mMatch.swapStats();
            }
        }
    }

    private void swapWinnerIfChanged(float goalsLeft, float goalsRight, Penalties penalties) {
        boolean userDidWin;
        boolean swapped = mHeaderViewModel.isSwapped();
        if (penalties == null) {
            userDidWin = swapped != goalsLeft > goalsRight;
        } else {
            userDidWin = swapped != penalties.getWinner() > penalties.getLoser();
        }
        if ((mUser.didWin(mMatch) && !userDidWin) || (mUser.didLose(mMatch) && userDidWin)) {
            mMatch.swapWinnerAndLoser();
        }
    }

    public void updateTeam(Team team) {
        mHeaderViewModel.updateTeam(team);
    }

    public CreateEventHeaderViewModel getHeaderViewModel() {
        return mHeaderViewModel;
    }

    public MatchStatsViewModel getStatsCardViewModel() {
        return mStatsViewModel;
    }

    public interface CreateMatchViewModelInteraction {
        void onMatchUpdated(Match match);
    }
}
