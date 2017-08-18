package com.example.kevin.fifastatistics.viewmodels.card;

import android.content.res.Resources;
import android.databinding.Bindable;

import com.example.kevin.fifastatistics.FifaApplication;
import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.activities.MatchUpdateActivity;
import com.example.kevin.fifastatistics.databinding.CardUpdateStatsBinding;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchUpdate;
import com.example.kevin.fifastatistics.models.databasemodels.user.Stats;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.viewmodels.FifaBaseViewModel;
import com.example.kevin.fifastatistics.viewmodels.item.UpdateStatsItemViewModel;

public class UpdateStatsCardViewModel extends FifaBaseViewModel {

    private static final String ERROR_GOALS;
    private static final String ERROR_SHOTS;
    private static final String ERROR_SHOTS_ON_TARGET;
    private static final String ERROR_POSSESSION;
    private static final String ERROR_SHOT_ACCURACY;

    static {
        Resources r = FifaApplication.getContext().getResources();
        ERROR_GOALS = r.getString(R.string.error_goals);
        ERROR_SHOTS = r.getString(R.string.error_shots);
        ERROR_SHOTS_ON_TARGET = r.getString(R.string.error_shots_on_target);
        ERROR_POSSESSION = r.getString(R.string.error_possession);
        ERROR_SHOT_ACCURACY = r.getString(R.string.error_shot_accuracy);
    }

    private Match mMatch;
    private MatchUpdate.Builder mUpdateBuilder;
    private User mUser;
    private MatchUpdate mMatchUpdate;
    private CardUpdateStatsBinding mBinding;
    private MatchUpdateActivity.MatchEditType mType;

    private UpdateStatsItemViewModel mGoalsViewModel;
    private UpdateStatsItemViewModel mShotsViewModel;
    private UpdateStatsItemViewModel mShotsOnTargetViewModel;
    private UpdateStatsItemViewModel mPossessionViewModel;
    private UpdateStatsItemViewModel mTacklesViewModel;
    private UpdateStatsItemViewModel mFoulsViewModel;
    private UpdateStatsItemViewModel mYellowCardsViewModel;
    private UpdateStatsItemViewModel mRedCardsViewModel;
    private UpdateStatsItemViewModel mOffsidesViewModel;
    private UpdateStatsItemViewModel mInjuriesViewModel;
    private UpdateStatsItemViewModel mCornersViewModel;
    private UpdateStatsItemViewModel mShotAccuracyViewModel;
    private UpdateStatsItemViewModel mPassAccuracyViewModel;

    public UpdateStatsCardViewModel(Match match, MatchUpdate update, User user,
                                    CardUpdateStatsBinding binding, MatchUpdateActivity.MatchEditType type) {
        mMatch = match == null ? Match.empty() : match;
        mUpdateBuilder = new MatchUpdate.Builder(update);
        mUser = user;
        mBinding = binding;
        mMatchUpdate = update;
        mType = type;
    }

    public void init(Match match, MatchUpdate update) {
        mMatch = match == null ? Match.empty() : match;
        mUpdateBuilder = new MatchUpdate.Builder(update);
        mMatchUpdate = update;
        notifyChange();
    }

    @Bindable
    public UpdateStatsItemViewModel getGoalsViewModel() {
        if (mMatch != null) {
            mGoalsViewModel = getBuilder("goals")
                    .binding(mBinding.goalsStatUpdate)
                    .statFor(mMatch.getScoreWinner())
                    .statAgainst(mMatch.getScoreLoser())
                    .forConsumer(i -> mUpdateBuilder.statsFor().goals(i))
                    .againstConsumer(i -> mUpdateBuilder.statsAgainst().goals(i))
                    .forPredicate(this::getGoalsForPredicate)
                    .againstPredicate(this::getGoalsAgainstPredicate)
                    .label(Stats.GOALS)
                    .errorMessage(ERROR_GOALS)
                    .arePredicatesLinked(true)
                    .build();
        }
        return mGoalsViewModel;
    }

    @Bindable
    public UpdateStatsItemViewModel getShotsViewModel() {
        if (mMatch != null) {
            int shotsFor = Math.round(mMatch.getStatsFor().getShots());
            int shotsAgainst = Math.round(mMatch.getStatsAgainst().getShots());
            mShotsViewModel = getBuilder("shots")
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
            mShotsOnTargetViewModel = getBuilder("shotsOnTarget")
                    .binding(mBinding.shotsOnTargetStatUpdate)
                    .statFor(Math.round(mMatch.getStatsFor().getShotsOnTarget()))
                    .statAgainst(Math.round(mMatch.getStatsAgainst().getShotsOnTarget()))
                    .forConsumer(s -> mUpdateBuilder.statsFor().shotsOnTarget(s))
                    .againstConsumer(s -> mUpdateBuilder.statsAgainst().shotsOnTarget(s))
                    .forPredicate(s -> s == null || (s <= getShotsFor() && s >= getGoalsFor()))
                    .againstPredicate(s -> s == null || (s <= getShotsAgainst() && s >= getGoalsAgainst()))
                    .label(Stats.SHOTS_ON_TARGET)
                    .errorMessage(ERROR_SHOTS_ON_TARGET)
                    .build();
        }
        return mShotsOnTargetViewModel;
    }

    @Bindable
    public UpdateStatsItemViewModel getPossessionViewModel() {
        if (mMatch != null) {
            mPossessionViewModel = getBuilder("possession")
                    .binding(mBinding.possessionStatUpdate)
                    .statFor(Math.round(mMatch.getStatsFor().getPossession()))
                    .statAgainst(Math.round(mMatch.getStatsAgainst().getPossession()))
                    .forConsumer(p -> mUpdateBuilder.statsFor().possession(p))
                    .againstConsumer(p -> mUpdateBuilder.statsAgainst().possession(p))
                    .forPredicate(p -> p == null || p + getPossessionAgainst() == 100)
                    .againstPredicate(p -> p == null || p + getPossessionFor() == 100)
                    .label(Stats.POSSESSION)
                    .errorMessage(ERROR_POSSESSION)
                    .arePredicatesLinked(true)
                    .build();
        }
        return mPossessionViewModel;
    }

    @Bindable
    public UpdateStatsItemViewModel getTacklesViewModel() {
        if (mMatch != null) {
            mTacklesViewModel = getBuilder("tackles")
                    .binding(mBinding.tacklesStatUpdate)
                    .statFor(Math.round(mMatch.getStatsFor().getTackles()))
                    .statAgainst(Math.round(mMatch.getStatsAgainst().getTackles()))
                    .forConsumer(t -> mUpdateBuilder.statsFor().tackles(t))
                    .againstConsumer(t -> mUpdateBuilder.statsAgainst().tackles(t))
                    .label(Stats.TACKLES)
                    .build();
        }
        return mTacklesViewModel;
    }

    @Bindable
    public UpdateStatsItemViewModel getFoulsViewModel() {
        if (mMatch != null) {
            mFoulsViewModel = getBuilder("fouls")
                    .binding(mBinding.foulsStatUpdate)
                    .statFor(Math.round(mMatch.getStatsFor().getFouls()))
                    .statAgainst(Math.round(mMatch.getStatsAgainst().getFouls()))
                    .forConsumer(f -> mUpdateBuilder.statsFor().fouls(f))
                    .againstConsumer(f -> mUpdateBuilder.statsAgainst().fouls(f))
                    .label(Stats.FOULS)
                    .build();
        }
        return mFoulsViewModel;
    }

    @Bindable
    public UpdateStatsItemViewModel getYellowCardsViewModel() {
        if (mMatch != null) {
            mYellowCardsViewModel = getBuilder("yellowCards")
                    .binding(mBinding.yellowCardsStatUpdate)
                    .statFor(Math.round(mMatch.getStatsFor().getYellowCards()))
                    .statAgainst(Math.round(mMatch.getStatsAgainst().getYellowCards()))
                    .forConsumer(c -> mUpdateBuilder.statsFor().yellowCards(c))
                    .againstConsumer(c -> mUpdateBuilder.statsAgainst().yellowCards(c))
                    .label(Stats.YELLOW_CARDS)
                    .build();
        }
        return mYellowCardsViewModel;
    }
    
    @Bindable
    public UpdateStatsItemViewModel getRedCardsViewModel() {
        if (mMatch != null) {
            mRedCardsViewModel = getBuilder("redCards")
                    .binding(mBinding.redCardsStatUpdate)
                    .statFor(Math.round(mMatch.getStatsFor().getRedCards()))
                    .statAgainst(Math.round(mMatch.getStatsAgainst().getRedCards()))
                    .forConsumer(c -> mUpdateBuilder.statsFor().redCards(c))
                    .againstConsumer(c -> mUpdateBuilder.statsAgainst().redCards(c))
                    .label(Stats.RED_CARDS)
                    .build();
        }
        return mRedCardsViewModel;
    }

    @Bindable
    public UpdateStatsItemViewModel getOffsidesViewModel() {
        if (mMatch != null) {
            mOffsidesViewModel = getBuilder("offsides")
                    .binding(mBinding.offsidesStatUpdate)
                    .statFor(Math.round(mMatch.getStatsFor().getOffsides()))
                    .statAgainst(Math.round(mMatch.getStatsAgainst().getOffsides()))
                    .forConsumer(c -> mUpdateBuilder.statsFor().offsides(c))
                    .againstConsumer(c -> mUpdateBuilder.statsAgainst().offsides(c))
                    .label(Stats.OFFSIDES)
                    .build();
        }
        return mOffsidesViewModel;
    }

    @Bindable
    public UpdateStatsItemViewModel getInjuriesViewModel() {
        if (mMatch != null) {
            mInjuriesViewModel = getBuilder("injuries")
                    .binding(mBinding.injuriesStatUpdate)
                    .statFor(Math.round(mMatch.getStatsFor().getInjuries()))
                    .statAgainst(Math.round(mMatch.getStatsAgainst().getInjuries()))
                    .forConsumer(c -> mUpdateBuilder.statsFor().injuries(c))
                    .againstConsumer(c -> mUpdateBuilder.statsAgainst().injuries(c))
                    .label(Stats.INJURIES)
                    .build();
        }
        return mInjuriesViewModel;
    }

    @Bindable
    public UpdateStatsItemViewModel getCornersViewModel() {
        if (mMatch != null) {
            mCornersViewModel = getBuilder("corners")
                    .binding(mBinding.cornersStatUpdate)
                    .statFor(Math.round(mMatch.getStatsFor().getCorners()))
                    .statAgainst(Math.round(mMatch.getStatsAgainst().getCorners()))
                    .forConsumer(c -> mUpdateBuilder.statsFor().corners(c))
                    .againstConsumer(c -> mUpdateBuilder.statsAgainst().corners(c))
                    .label(Stats.CORNERS)
                    .build();
        }
        return mCornersViewModel;
    }

    @Bindable
    public UpdateStatsItemViewModel getShotAccuracyViewModel() {
        if (mMatch != null) {
            mShotAccuracyViewModel = getBuilder("shotAccuracy")
                    .binding(mBinding.shotAccuracyStatUpdate)
                    .statFor(Math.round(mMatch.getStatsFor().getShotAccuracy()))
                    .statAgainst(Math.round(mMatch.getStatsAgainst().getShotAccuracy()))
                    .forConsumer(s -> mUpdateBuilder.statsFor().shotAccuracy(s))
                    .againstConsumer(s -> mUpdateBuilder.statsAgainst().shotAccuracy(s))
                    .forPredicate(s -> s == null || isShotAccuracyCorrect(s, getShotsFor(), getShotsOnTargetFor()))
                    .againstPredicate(s -> s == null || isShotAccuracyCorrect(s, getShotsAgainst(), getShotsOnTargetAgainst()))
                    .label(Stats.SHOT_ACCURACY)
                    .errorMessage(ERROR_SHOT_ACCURACY)
                    .build();
        }
        return mShotAccuracyViewModel;
    }

    @Bindable
    public UpdateStatsItemViewModel getPassAccuracyViewModel() {
        if (mMatch != null) {
            mPassAccuracyViewModel = getBuilder("passAccuracy")
                    .binding(mBinding.passAccuracyStatUpdate)
                    .statFor(Math.round(mMatch.getStatsFor().getPassAccuracy()))
                    .statAgainst(Math.round(mMatch.getStatsAgainst().getPassAccuracy()))
                    .forConsumer(s -> mUpdateBuilder.statsFor().passAccuracy(s))
                    .againstConsumer(s -> mUpdateBuilder.statsAgainst().passAccuracy(s))
                    .label(Stats.PASS_ACCURACY)
                    .build();
        }
        return mPassAccuracyViewModel;
    }
    
    private UpdateStatsItemViewModel.UpdateStatsItemViewModelBuilder getBuilder(String key) {
        return UpdateStatsItemViewModel.builder()
                .type(mType)
                .updateFor(mMatchUpdate != null ? mMatchUpdate.getStatFor(key) : null)
                .updateAgainst(mMatchUpdate != null ? mMatchUpdate.getStatAgainst(key) : null);
    }

    private boolean isShotAccuracyCorrect(int check, float shots, float shotsOnTarget) {
        double floor = shots == 0f ? 0f : Math.floor(shotsOnTarget / shots * 100);
        return check >= floor && check <= floor + 1;
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
        return getFor("goals", mMatch.getStatsFor().getGoals());
    }

    private int getGoalsAgainst() {
        return getAgainst("goals", mMatch.getStatsAgainst().getGoals());
    }

    private int getShotsFor() {
        return getFor("shots", mMatch.getStatsFor().getShots());
    }

    private int getShotsAgainst() {
        return getAgainst("shots", mMatch.getStatsAgainst().getShots());
    }

    private int getShotsOnTargetFor() {
        return getFor("shotsOnTarget", mMatch.getStatsFor().getShotsOnTarget());
    }

    private int getShotsOnTargetAgainst() {
        return getAgainst("shotsOnTarget", mMatch.getStatsAgainst().getShotsOnTarget());
    }

    private int getPossessionFor() {
        return getFor("possession", mMatch.getStatsFor().getPossession());
    }

    private int getPossessionAgainst() {
        return getAgainst("possession", mMatch.getStatsAgainst().getPossession());
    }

    private int getFor(String key, float matchVal) {
        Integer updateVal = mUpdateBuilder.build().getStatFor(key);
        return updateVal == null ? Math.round(matchVal) : updateVal;
    }

    private int getAgainst(String key, float matchVal) {
        Integer updateVal = mUpdateBuilder.build().getStatAgainst(key);
        return updateVal == null ? Math.round(matchVal) : updateVal;
    }

    @Bindable
    public String getLeftHeader() {
        return mMatch.didWin(mUser) ? "You" : mMatch.getWinnerFirstName();
    }

    @Bindable
    public String getRightHeader() {
        return !mMatch.didWin(mUser) ? "You" : mMatch.getLoserFirstName();
    }
    
    public boolean validate() {
        return
                mGoalsViewModel.isValid() &&
                mShotsViewModel.isValid() &&
                mShotsOnTargetViewModel.isValid() &&
                mPossessionViewModel.isValid() &&
                mTacklesViewModel.isValid() &&
                mFoulsViewModel.isValid() &&
                mYellowCardsViewModel.isValid() &&
                mRedCardsViewModel.isValid() &&
                mOffsidesViewModel.isValid() &&
                mInjuriesViewModel.isValid() &&
                mCornersViewModel.isValid() &&
                mShotAccuracyViewModel.isValid() &&
                mPassAccuracyViewModel.isValid();
    }

    public MatchUpdate build() {
        return mUpdateBuilder
                .creatorId(mUser.getId())
                .receiverId(mMatchUpdate != null ? mMatchUpdate.getReceiverId() : null)
                .matchId(mMatch.getId())
                .id(mMatchUpdate != null ? mMatchUpdate.getId() : null)
                .build();
    }

    public boolean isError() {
        return mGoalsViewModel.isError();
    }
}
