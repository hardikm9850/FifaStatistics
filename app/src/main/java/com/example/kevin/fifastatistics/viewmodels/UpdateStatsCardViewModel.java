package com.example.kevin.fifastatistics.viewmodels;

import android.content.res.Resources;
import android.databinding.Bindable;

import com.example.kevin.fifastatistics.FifaApplication;
import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.databinding.CardUpdateStatsBinding;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchUpdate;
import com.example.kevin.fifastatistics.models.databasemodels.user.Stats;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;

public class UpdateStatsCardViewModel extends FifaBaseViewModel {

    private static final String ERROR_GOALS;

    static {
        Resources r = FifaApplication.getContext().getResources();
        ERROR_GOALS = r.getString(R.string.error_goals);
    }

    private Match mMatch;
    private MatchUpdate.Builder mUpdateBuilder;
    private User mUser;
    private CardUpdateStatsBinding mBinding;

    public UpdateStatsCardViewModel(Match match, MatchUpdate update, User user, CardUpdateStatsBinding binding) {
        mMatch = match;
        mUpdateBuilder = new MatchUpdate.Builder(update);
        mUser = user;
        mBinding = binding;
    }

    public void init(Match match, MatchUpdate update) {
        mMatch = match;
        mUpdateBuilder = new MatchUpdate.Builder(update);
        notifyChange();
    }

    @Bindable
    public UpdateStatsItemViewModel getGoalsViewModel() {
        if (mMatch != null) {
            return UpdateStatsItemViewModel.builder()
                    .binding(mBinding.goalsStatUpdate)
                    .statFor(mMatch.getScoreWinner())
                    .statAgainst(mMatch.getScoreLoser())
                    .forConsumer(i -> mUpdateBuilder = mUpdateBuilder.statsFor().goals(i).and())
                    .againstConsumer(i -> mUpdateBuilder = mUpdateBuilder.statsAgainst().goals(i).and())
                    .forPredicate(this::getGoalsForPredicate)
                    .againstPredicate(this::getGoalsAgainstPredicate)
                    .label(Stats.GOALS)
                    .errorMessage(ERROR_GOALS)
                    .build();
        } else {
            return null;
        }
    }

    private boolean getGoalsForPredicate(Integer newGoalsFor) {
        Integer updateGoals = mUpdateBuilder.build().getStatAgainst("goals");
        int goalsAgainst = updateGoals == null ? mMatch.getScoreLoser() : updateGoals;
        int goalsFor = newGoalsFor == null ? mMatch.getScoreWinner() : newGoalsFor;
        return (goalsAgainst < goalsFor || (goalsAgainst == goalsFor && !mMatch.hasPenalties()));
    }

    private boolean getGoalsAgainstPredicate(Integer newGoalsAgainst) {
        Integer updateGoals = mUpdateBuilder.build().getStatFor("goals");
        int goalsFor = updateGoals == null ? mMatch.getScoreWinner() : updateGoals;
        int goalsAgainst = newGoalsAgainst == null ? mMatch.getScoreLoser() : newGoalsAgainst;
        return (goalsAgainst < goalsFor || (goalsAgainst == goalsFor && !mMatch.hasPenalties()));
    }

    public String getLeftHeader() {
        return mMatch.didWin(mUser) ? "You" : mMatch.getWinnerFirstName();
    }

    public String getRightHeader() {
        return !mMatch.didWin(mUser) ? "You" : mMatch.getLoserFirstName();
    }

    public MatchUpdate build() {
        return mUpdateBuilder
                .creatorId(mUser.getId())
                .matchId(mMatch.getId())
                .build();
    }
}
