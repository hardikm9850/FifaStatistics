package com.example.kevin.fifastatistics.viewmodels.card;

import android.content.Intent;
import android.content.res.Resources;
import android.databinding.Bindable;
import android.view.View;

import com.example.kevin.fifastatistics.FifaApplication;
import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.activities.CameraActivity;
import com.example.kevin.fifastatistics.activities.MatchUpdateActivity;
import com.example.kevin.fifastatistics.databinding.CardUpdateStatsBinding;
import com.example.kevin.fifastatistics.fragments.CreateMatchFragment;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.interfaces.Consumer;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchUpdate;
import com.example.kevin.fifastatistics.models.databasemodels.user.Stats;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.viewmodels.FifaBaseViewModel;
import com.example.kevin.fifastatistics.viewmodels.item.UpdateStatsItemViewModel;

import lombok.AllArgsConstructor;
import lombok.Getter;

public abstract class MatchStatsViewModel extends FifaBaseViewModel {

    private static final String ERROR_GOALS;
    private static final String ERROR_SHOTS;
    private static final String ERROR_SHOTS_ON_TARGET;
    private static final String ERROR_POSSESSION;
    private static final String ERROR_SHOT_ACCURACY;
    private static final String ERROR_PENALTIES;

    static {
        Resources r = FifaApplication.getContext().getResources();
        ERROR_GOALS = r.getString(R.string.error_goals);
        ERROR_SHOTS = r.getString(R.string.error_shots);
        ERROR_SHOTS_ON_TARGET = r.getString(R.string.error_shots_on_target);
        ERROR_POSSESSION = r.getString(R.string.error_possession);
        ERROR_SHOT_ACCURACY = r.getString(R.string.error_shot_accuracy);
        ERROR_PENALTIES = r.getString(R.string.error_penalties_same);
    }

    protected Match mMatch;
    protected User mUser;
    protected MatchUpdate mMatchUpdate;
    private CardUpdateStatsBinding mBinding;
    private MatchUpdateActivity.MatchEditType mType;
    private ActivityLauncher mLauncher;

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
    private UpdateStatsItemViewModel mPenaltiesViewModel;

    public MatchStatsViewModel(Match match, MatchUpdate update, User user, CardUpdateStatsBinding binding,
                               MatchUpdateActivity.MatchEditType type, ActivityLauncher launcher) {
        mMatch = match == null ? Match.empty() : match;
        mUser = user;
        mBinding = binding;
        mMatchUpdate = update;
        mType = type;
        mLauncher = launcher;
        restore(match);
    }

    private void restore(Match match) {
        if (mType == MatchUpdateActivity.MatchEditType.CREATE && match != null && match.getStats() != null) {
            initViewModels();
            setStats(match.getStats());
        }
    }

    private void initViewModels() {
        getGoalsViewModel();
        getShotsViewModel();
        getShotsOnTargetViewModel();
        getPossessionViewModel();
        getTacklesViewModel();
        getFoulsViewModel();
        getYellowCardsViewModel();
        getRedCardsViewModel();
        getOffsidesViewModel();
        getInjuriesViewModel();
        getCornersViewModel();
        getShotAccuracyViewModel();
        getPassAccuracyViewModel();
        getPenaltiesViewModel();
    }

    @Bindable
    public UpdateStatsItemViewModel getGoalsViewModel() {
        if (mMatch != null && mGoalsViewModel == null) {
            mGoalsViewModel = getBuilder("goals", getGoalsConsumers())
                    .binding(mBinding.goalsStatUpdate)
                    .statFor(mMatch.getScoreWinner())
                    .statAgainst(mMatch.getScoreLoser())
                    .forPredicate(this::getGoalsForPredicate)
                    .againstPredicate(this::getGoalsAgainstPredicate)
                    .label(Stats.GOALS)
                    .errorMessage(ERROR_GOALS)
                    .arePredicatesLinked(true)
                    .build();
        }
        return mGoalsViewModel;
    }

    protected abstract StatConsumers getGoalsConsumers();

    @Bindable
    public UpdateStatsItemViewModel getShotsViewModel() {
        if (mMatch != null && mShotsViewModel == null) {
            int shotsFor = Math.round(mMatch.getStatsFor().getShots());
            int shotsAgainst = Math.round(mMatch.getStatsAgainst().getShots());
            mShotsViewModel = getBuilder("shots", getShotsConsumers())
                    .binding(mBinding.shotsStatUpdate)
                    .statFor(shotsFor)
                    .statAgainst(shotsAgainst)
                    .forPredicate(s -> s == null || (s >= getGoalsFor() && s >= getShotsOnTargetFor()))
                    .againstPredicate(s -> s == null || (s >= getGoalsAgainst() && s >= getShotsOnTargetAgainst()))
                    .label(Stats.SHOTS)
                    .errorMessage(ERROR_SHOTS)
                    .build();
        }
        return mShotsViewModel;
    }

    protected abstract StatConsumers getShotsConsumers();

    @Bindable
    public UpdateStatsItemViewModel getShotsOnTargetViewModel() {
        if (mMatch != null && mShotsOnTargetViewModel == null) {
            mShotsOnTargetViewModel = getBuilder("shotsOnTarget", getShotsOnTargetConsumers())
                    .binding(mBinding.shotsOnTargetStatUpdate)
                    .statFor(Math.round(mMatch.getStatsFor().getShotsOnTarget()))
                    .statAgainst(Math.round(mMatch.getStatsAgainst().getShotsOnTarget()))
                    .forPredicate(s -> s == null || (s <= getShotsFor() && s >= getGoalsFor()))
                    .againstPredicate(s -> s == null || (s <= getShotsAgainst() && s >= getGoalsAgainst()))
                    .label(Stats.SHOTS_ON_TARGET)
                    .errorMessage(ERROR_SHOTS_ON_TARGET)
                    .build();
        }
        return mShotsOnTargetViewModel;
    }

    protected abstract StatConsumers getShotsOnTargetConsumers();

    @Bindable
    public UpdateStatsItemViewModel getPossessionViewModel() {
        if (mMatch != null && mPossessionViewModel == null) {
            mPossessionViewModel = getBuilder("possession", getPossessionConsumers())
                    .binding(mBinding.possessionStatUpdate)
                    .statFor(Math.round(mMatch.getStatsFor().getPossession()))
                    .statAgainst(Math.round(mMatch.getStatsAgainst().getPossession()))
                    .forPredicate(p -> p == null || p + getPossessionAgainst() == 100)
                    .againstPredicate(p -> p == null || p + getPossessionFor() == 100)
                    .label(Stats.POSSESSION)
                    .errorMessage(ERROR_POSSESSION)
                    .arePredicatesLinked(true)
                    .build();
        }
        return mPossessionViewModel;
    }

    protected abstract StatConsumers getPossessionConsumers();

    @Bindable
    public UpdateStatsItemViewModel getTacklesViewModel() {
        if (mMatch != null && mTacklesViewModel == null) {
            mTacklesViewModel = getBuilder("tackles", getTacklesConsumers())
                    .binding(mBinding.tacklesStatUpdate)
                    .statFor(Math.round(mMatch.getStatsFor().getTackles()))
                    .statAgainst(Math.round(mMatch.getStatsAgainst().getTackles()))
                    .label(Stats.TACKLES)
                    .build();
        }
        return mTacklesViewModel;
    }

    protected abstract StatConsumers getTacklesConsumers();

    @Bindable
    public UpdateStatsItemViewModel getFoulsViewModel() {
        if (mMatch != null && mFoulsViewModel == null) {
            mFoulsViewModel = getBuilder("fouls", getFoulsConsumers())
                    .binding(mBinding.foulsStatUpdate)
                    .statFor(Math.round(mMatch.getStatsFor().getFouls()))
                    .statAgainst(Math.round(mMatch.getStatsAgainst().getFouls()))
                    .label(Stats.FOULS)
                    .build();
        }
        return mFoulsViewModel;
    }

    protected abstract StatConsumers getFoulsConsumers();

    @Bindable
    public UpdateStatsItemViewModel getYellowCardsViewModel() {
        if (mMatch != null && mYellowCardsViewModel == null) {
            mYellowCardsViewModel = getBuilder("yellowCards", getYellowCardsConsumers())
                    .binding(mBinding.yellowCardsStatUpdate)
                    .statFor(Math.round(mMatch.getStatsFor().getYellowCards()))
                    .statAgainst(Math.round(mMatch.getStatsAgainst().getYellowCards()))
                    .label(Stats.YELLOW_CARDS)
                    .build();
        }
        return mYellowCardsViewModel;
    }

    protected abstract StatConsumers getYellowCardsConsumers();

    @Bindable
    public UpdateStatsItemViewModel getRedCardsViewModel() {
        if (mMatch != null && mRedCardsViewModel == null) {
            mRedCardsViewModel = getBuilder("redCards", getRedCardsConsumers())
                    .binding(mBinding.redCardsStatUpdate)
                    .statFor(Math.round(mMatch.getStatsFor().getRedCards()))
                    .statAgainst(Math.round(mMatch.getStatsAgainst().getRedCards()))
                    .label(Stats.RED_CARDS)
                    .build();
        }
        return mRedCardsViewModel;
    }

    protected abstract StatConsumers getRedCardsConsumers();

    @Bindable
    public UpdateStatsItemViewModel getOffsidesViewModel() {
        if (mMatch != null && mOffsidesViewModel == null) {
            mOffsidesViewModel = getBuilder("offsides", getOffsidesConsumers())
                    .binding(mBinding.offsidesStatUpdate)
                    .statFor(Math.round(mMatch.getStatsFor().getOffsides()))
                    .statAgainst(Math.round(mMatch.getStatsAgainst().getOffsides()))
                    .label(Stats.OFFSIDES)
                    .build();
        }
        return mOffsidesViewModel;
    }

    protected abstract StatConsumers getOffsidesConsumers();

    @Bindable
    public UpdateStatsItemViewModel getInjuriesViewModel() {
        if (mMatch != null && mInjuriesViewModel == null) {
            mInjuriesViewModel = getBuilder("injuries", getInjuriesConsumers())
                    .binding(mBinding.injuriesStatUpdate)
                    .statFor(Math.round(mMatch.getStatsFor().getInjuries()))
                    .statAgainst(Math.round(mMatch.getStatsAgainst().getInjuries()))
                    .label(Stats.INJURIES)
                    .build();
        }
        return mInjuriesViewModel;
    }

    protected abstract StatConsumers getInjuriesConsumers();

    @Bindable
    public UpdateStatsItemViewModel getCornersViewModel() {
        if (mMatch != null && mCornersViewModel == null) {
            mCornersViewModel = getBuilder("corners", getCornersConsumers())
                    .binding(mBinding.cornersStatUpdate)
                    .statFor(Math.round(mMatch.getStatsFor().getCorners()))
                    .statAgainst(Math.round(mMatch.getStatsAgainst().getCorners()))
                    .label(Stats.CORNERS)
                    .build();
        }
        return mCornersViewModel;
    }

    protected abstract StatConsumers getCornersConsumers();

    @Bindable
    public UpdateStatsItemViewModel getShotAccuracyViewModel() {
        if (mMatch != null && mShotAccuracyViewModel == null) {
            mShotAccuracyViewModel = getBuilder("shotAccuracy", getShotAccuracyConsumers())
                    .binding(mBinding.shotAccuracyStatUpdate)
                    .statFor(Math.round(mMatch.getStatsFor().getShotAccuracy()))
                    .statAgainst(Math.round(mMatch.getStatsAgainst().getShotAccuracy()))
                    .forPredicate(s -> s == null || isShotAccuracyCorrect(s, getShotsFor(), getShotsOnTargetFor()))
                    .againstPredicate(s -> s == null || isShotAccuracyCorrect(s, getShotsAgainst(), getShotsOnTargetAgainst()))
                    .label(Stats.SHOT_ACCURACY)
                    .errorMessage(ERROR_SHOT_ACCURACY)
                    .build();
        }
        return mShotAccuracyViewModel;
    }

    protected abstract StatConsumers getShotAccuracyConsumers();

    @Bindable
    public UpdateStatsItemViewModel getPassAccuracyViewModel() {
        if (mMatch != null && mPassAccuracyViewModel == null) {
            mPassAccuracyViewModel = getBuilder("passAccuracy", getPassAccuracyConsumers())
                    .binding(mBinding.passAccuracyStatUpdate)
                    .statFor(Math.round(mMatch.getStatsFor().getPassAccuracy()))
                    .statAgainst(Math.round(mMatch.getStatsAgainst().getPassAccuracy()))
                    .label(Stats.PASS_ACCURACY)
                    .build();
        }
        return mPassAccuracyViewModel;
    }

    protected abstract StatConsumers getPassAccuracyConsumers();

    @Bindable
    public UpdateStatsItemViewModel getPenaltiesViewModel() {
        if (mMatch != null && mPenaltiesViewModel == null) {
            StatConsumers consumers = getPenaltiesConsumers();
            mPenaltiesViewModel = UpdateStatsItemViewModel.builder()
                    .type(mType)
                    .forConsumer(consumers.forConsumer)
                    .againstConsumer(consumers.againstConsumer)
                    .forPredicate(p -> !needPenalties() || (p != null && p != mMatch.getPenaltiesAgainst()))
                    .againstPredicate(p -> !needPenalties() || (p != null && p != mMatch.getPenaltiesFor()))
                    .binding(mBinding.penaltiesStatUpdate)
                    .statFor(mMatch.getPenaltiesFor())
                    .statAgainst(mMatch.getPenaltiesAgainst())
                    .label(Stats.PENALTIES)
                    .errorMessage(ERROR_PENALTIES)
                    .build();
        }
        return mPenaltiesViewModel;
    }

    protected abstract StatConsumers getPenaltiesConsumers();

    private boolean needPenalties() {
        return getGoalsFor() == getGoalsAgainst();
    }

    private UpdateStatsItemViewModel.UpdateStatsItemViewModelBuilder getBuilder(String key,
                                                                                StatConsumers consumers) {
        return UpdateStatsItemViewModel.builder()
                .type(mType)
                .updateFor(mMatchUpdate != null ? mMatchUpdate.getStatFor(key) : null)
                .updateAgainst(mMatchUpdate != null ? mMatchUpdate.getStatAgainst(key) : null)
                .forConsumer(consumers.forConsumer)
                .againstConsumer(consumers.againstConsumer);
    }

    private boolean isShotAccuracyCorrect(int check, float shots, float shotsOnTarget) {
        double floor = shots == 0f ? 0f : Math.floor(shotsOnTarget / shots * 100);
        return check >= floor && check <= floor + 1;
    }

    private boolean getGoalsForPredicate(Integer newGoalsFor) {
        int goalsAgainst = getGoalsAgainst();
        int goalsFor = newGoalsFor == null ? mMatch.getScoreWinner() : newGoalsFor;
        return isMatchOutcomeUnchanged(goalsFor, goalsAgainst);
    }

    private boolean getGoalsAgainstPredicate(Integer newGoalsAgainst) {
        int goalsFor = getGoalsFor();
        int goalsAgainst = newGoalsAgainst == null ? mMatch.getScoreLoser() : newGoalsAgainst;
        return isMatchOutcomeUnchanged(goalsFor, goalsAgainst);
    }

    private boolean isMatchOutcomeUnchanged(int goalsFor, int goalsAgainst) {
        boolean unchanged = (goalsAgainst < goalsFor || (goalsAgainst == goalsFor && mMatch.hasPenalties()));
        return unchanged || mType == MatchUpdateActivity.MatchEditType.CREATE;
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

    protected abstract int getFor(String key, float matchVal);

    protected abstract int getAgainst(String key, float matchVal);

    @Bindable
    public String getLeftHeader() {
        return mMatch.didWin(mUser) ? "You" : mMatch.getWinnerFirstName();
    }

    @Bindable
    public String getRightHeader() {
        return !mMatch.didWin(mUser) ? "You" : mMatch.getLoserFirstName();
    }

    public int getHeaderVisibility() {
        return View.VISIBLE;
    }

    @Bindable
    public int getPenaltiesItemVisibility() {
        return View.GONE;
    }

    @Bindable
    public int getCameraButtonVisibility() {
        return mType == MatchUpdateActivity.MatchEditType.CREATE ? View.VISIBLE : View.GONE;
    }

    public void onCameraButtonClick() {
        if (mLauncher != null) {
            System.gc();
            Intent intent = new Intent(mLauncher.getContext(), CameraActivity.class);
            mLauncher.launchActivity(intent, CreateMatchFragment.CREATE_MATCH_REQUEST_CODE, null);
        }
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
                mPassAccuracyViewModel.isValid() &&
                mPenaltiesViewModel.isValid();
    }

    public boolean areAllEditTextsFilled() {
        return 
                mGoalsViewModel.areEditTextsFilled() &&
                mShotsViewModel.areEditTextsFilled() &&
                mShotsOnTargetViewModel.areEditTextsFilled() &&
                mPossessionViewModel.areEditTextsFilled() &&
                mTacklesViewModel.areEditTextsFilled() &&
                mFoulsViewModel.areEditTextsFilled() &&
                mYellowCardsViewModel.areEditTextsFilled() &&
                mRedCardsViewModel.areEditTextsFilled() &&
                mOffsidesViewModel.areEditTextsFilled() &&
                mInjuriesViewModel.areEditTextsFilled() &&
                mCornersViewModel.areEditTextsFilled() &&
                mShotAccuracyViewModel.areEditTextsFilled() &&
                mPassAccuracyViewModel.areEditTextsFilled() &&
                (!needPenalties() || mPenaltiesViewModel.areEditTextsFilled());
    }
    
    public void autofill() {
        User.StatsPair stats = mUser.getAverageStats();
        setStats(stats);
    }

    public void setStats(User.StatsPair stats) {
        Stats statsFor = stats.getStatsFor();
        Stats statsAgainst = stats.getStatsAgainst();
        mGoalsViewModel.setEditTextValues(statsFor.getGoals(), statsAgainst.getGoals());
        mShotsViewModel.setEditTextValues(statsFor.getShots(), statsAgainst.getShots());
        mShotsOnTargetViewModel.setEditTextValues(statsFor.getShotsOnTarget(), statsAgainst.getShotsOnTarget());
        mPossessionViewModel.setEditTextValues(statsFor.getPossession(), statsAgainst.getPossession());
        mTacklesViewModel.setEditTextValues(statsFor.getTackles(), statsAgainst.getTackles());
        mFoulsViewModel.setEditTextValues(statsFor.getFouls(), statsAgainst.getFouls());
        mYellowCardsViewModel.setEditTextValues(statsFor.getYellowCards(), statsAgainst.getYellowCards());
        mRedCardsViewModel.setEditTextValues(statsFor.getRedCards(), statsAgainst.getRedCards());
        mOffsidesViewModel.setEditTextValues(statsFor.getOffsides(), statsAgainst.getOffsides());
        mInjuriesViewModel.setEditTextValues(statsFor.getGoals(), statsAgainst.getGoals());
        mCornersViewModel.setEditTextValues(statsFor.getGoals(), statsAgainst.getGoals());
        mShotAccuracyViewModel.setEditTextValues(statsFor.getShotAccuracy(), statsAgainst.getShotAccuracy());
        mPassAccuracyViewModel.setEditTextValues(statsFor.getPassAccuracy(), statsAgainst.getPassAccuracy());
    }

    @Override
    public void destroy() {
        super.destroy();
        mLauncher = null;
    }

    @AllArgsConstructor
    @Getter
    public static class StatConsumers {
        private final Consumer<Integer> forConsumer;
        private final Consumer<Integer> againstConsumer;
    }
}
