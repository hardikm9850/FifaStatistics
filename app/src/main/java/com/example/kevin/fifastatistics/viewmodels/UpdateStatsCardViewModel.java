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
    private static final String ERROR_SHOTS;
    private static final String ERROR_SHOTS_ON_TARGET;

    static {
        Resources r = FifaApplication.getContext().getResources();
        ERROR_GOALS = r.getString(R.string.error_goals);
        ERROR_SHOTS = r.getString(R.string.error_shots);
        ERROR_SHOTS_ON_TARGET = r.getString(R.string.error_shots_on_target);
    }

    private Match mMatch;
    private MatchUpdate.Builder mUpdateBuilder;
    private User mUser;
    private CardUpdateStatsBinding mBinding;

    private UpdateStatsItemViewModel mGoalsViewModel;
    private UpdateStatsItemViewModel mShotsViewModel;
    private UpdateStatsItemViewModel mShotsOnTargetViewModel;

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
            mGoalsViewModel = UpdateStatsItemViewModel.builder()
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
        }
        return mGoalsViewModel;
    }

    @Bindable
    public UpdateStatsItemViewModel getShotsViewModel() {
        if (mMatch != null) {
            int shotsFor = Math.round(mMatch.getStatsFor().getShots());
            int shotsAgainst = Math.round(mMatch.getStatsAgainst().getShots());
            mShotsViewModel = UpdateStatsItemViewModel.builder()
                    .binding(mBinding.shotsStatUpdate)
                    .statFor(shotsFor)
                    .statAgainst(shotsAgainst)
                    .forConsumer(s -> mUpdateBuilder.statsFor().shots(s))
                    .againstConsumer(s -> mUpdateBuilder.statsAgainst().shots(s))
                    .forPredicate(s -> s == null || (s >= getGoalsFor() && s >= getShotsOnTargetFor()))
                    .againstPredicate(s -> s == null || (s >= getGoalsAgainst() && s >= getShotsOnTargetAgainst()))
                    .label(Stats.SHOTS)
                    .errorMessage(ERROR_SHOTS)
                    .build();
        }
        return mShotsViewModel;
    }

    @Bindable
    public UpdateStatsItemViewModel getShotsOnTargetViewModel() {
        if (mMatch != null) {
            mShotsOnTargetViewModel = UpdateStatsItemViewModel.builder()
                    .binding(mBinding.shotsOnTargetStatUpdate)
                    .statFor(Math.round(mMatch.getStatsFor().getShotsOnTarget()))
                    .statAgainst(Math.round(mMatch.getStatsAgainst().getShotsOnTarget()))
                    .forConsumer(s -> mUpdateBuilder.statsFor().shotsOnTarget(s))
                    .againstConsumer(s -> mUpdateBuilder.statsAgainst().shotsOnTarget(s))
                    .forPredicate(s -> s == null || s <= getShotsFor())
                    .againstPredicate(s -> s == null || s <= getShotsAgainst())
                    .label(Stats.SHOTS_ON_TARGET)
                    .errorMessage(ERROR_SHOTS_ON_TARGET)
                    .build();
        }
        return mShotsOnTargetViewModel;
    }

    private boolean getGoalsForPredicate(Integer newGoalsFor) {
        int goalsAgainst = getGoalsAgainst();
        int goalsFor = newGoalsFor == null ? mMatch.getScoreWinner() : newGoalsFor;
        return (goalsAgainst < goalsFor || (goalsAgainst == goalsFor && mMatch.hasPenalties()));
    }

    private boolean getGoalsAgainstPredicate(Integer newGoalsAgainst) {
        int goalsFor = getGoalsFor();
        int goalsAgainst = newGoalsAgainst == null ? mMatch.getScoreLoser() : newGoalsAgainst;
        return (goalsAgainst < goalsFor || (goalsAgainst == goalsFor && mMatch.hasPenalties()));
    }

    private int getGoalsFor() {
        Integer updateGoals = mUpdateBuilder.build().getStatFor("goals");
        return updateGoals == null ? mMatch.getScoreWinner() : updateGoals;
    }

    private int getGoalsAgainst() {
        Integer updateGoals = mUpdateBuilder.build().getStatAgainst("goals");
        return updateGoals == null ? mMatch.getScoreLoser() : updateGoals;
    }

    private int getShotsFor() {
        Integer updateGoals = mUpdateBuilder.build().getStatFor("shots");
        return updateGoals == null ? Math.round(mMatch.getStatsFor().getShots()) : updateGoals;
    }

    private int getShotsAgainst() {
        Integer updateGoals = mUpdateBuilder.build().getStatAgainst("shots");
        return updateGoals == null ? Math.round(mMatch.getStatsAgainst().getShots()) : updateGoals;
    }

    private int getShotsOnTargetFor() {
        Integer updateGoals = mUpdateBuilder.build().getStatFor("shotsOnTarget");
        return updateGoals == null ? Math.round(mMatch.getStatsFor().getShotsOnTarget()) : updateGoals;
    }

    private int getShotsOnTargetAgainst() {
        Integer updateGoals = mUpdateBuilder.build().getStatAgainst("shotsOnTarget");
        return updateGoals == null ? Math.round(mMatch.getStatsAgainst().getShotsOnTarget()) : updateGoals;
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

    public boolean isError() {
        return mGoalsViewModel.isError();
    }
}
