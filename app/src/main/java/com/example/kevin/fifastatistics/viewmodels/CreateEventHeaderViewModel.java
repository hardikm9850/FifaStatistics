package com.example.kevin.fifastatistics.viewmodels;

import android.content.Intent;
import android.databinding.Bindable;
import android.os.Bundle;
import android.view.View;

import com.example.kevin.fifastatistics.BR;
import com.example.kevin.fifastatistics.activities.PickTeamActivity;
import com.example.kevin.fifastatistics.fragments.CreateMatchFragment;
import com.example.kevin.fifastatistics.fragments.CreateSeriesMatchListFragment;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.managers.CrestUrlResizer;
import com.example.kevin.fifastatistics.managers.RetrievalManager;
import com.example.kevin.fifastatistics.managers.preferences.PrefsManager;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.utils.ObservableUtils;

import rx.Observable;
import rx.Subscription;

public class CreateEventHeaderViewModel extends FifaBaseViewModel {

    private static final String SWAPPED = "swapped";

    private final Player mUser;
    private final Player mOpponent;
    private final boolean mIsPartOfSeries;

    private CreateEventHeaderInteraction mInteraction;
    private ActivityLauncher mLauncher;
    private Team mUserTeam;
    private Team mOpponentTeam;
    private Team mLeftTeam;
    private Team mRightTeam;
    private Player mLeftPlayer;
    private Player mRightPlayer;
    private boolean mIsSwapped;
    private boolean mIsSelectedTeamLeft;

    public CreateEventHeaderViewModel(ActivityLauncher launcher, Player user, Player opponent,
                                      boolean isPartOfSeries, Bundle savedInstanceState,
                                      CreateEventHeaderInteraction interaction, Team userTeam,
                                      Team opponentTeam) {
        mLauncher = launcher;
        mUser = user;
        mOpponent = opponent;
        mIsPartOfSeries = isPartOfSeries;
        mInteraction = interaction;
        mUserTeam = userTeam;
        mOpponentTeam = opponentTeam;
        restore(savedInstanceState);
        initSides();
        initTeams();
    }

    private void restore(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mUserTeam = (Team) savedInstanceState.getSerializable(USER_TEAM);
            mOpponentTeam = (Team) savedInstanceState.getSerializable(OPPONENT_TEAM);
            mIsSwapped = savedInstanceState.getBoolean(SWAPPED, false);
        }
    }

    private void initSides() {
        if (mIsSwapped) {
            mLeftTeam = mOpponentTeam;
            mRightTeam = mUserTeam;
            mLeftPlayer = mOpponent;
            mRightPlayer = mUser;
        } else {
            mLeftTeam = mUserTeam;
            mRightTeam = mOpponentTeam;
            mLeftPlayer = mUser;
            mRightPlayer = mOpponent;
        }
    }

    private void initTeams() {
        if (mUserTeam == null) {
            getFavoriteTeam();
        }
        if (mOpponentTeam == null) {
            getOpponentFavoriteTeam();
        }
    }

    private void getFavoriteTeam() {
        Observable.<Team>create(s -> s.onNext(PrefsManager.getFavoriteTeam()))
                .compose(ObservableUtils.applySchedulers()).subscribe(team -> {
            if (team != null) {
                mUserTeam = team;
                notifyTeamChanged(true, team);
            }
        });
    }

    private void notifyTeamChanged(boolean isUserTeam, Team team) {
        boolean leftTeamChanged = (isUserTeam && !mIsSwapped) || (!isUserTeam && mIsSwapped);
        if (leftTeamChanged) {
            updateLeftTeam(team);
        } else {
            updateRightTeam(team);
        }
    }

    private void updateLeftTeam(Team team) {
        mLeftTeam = team;
        mInteraction.onLeftTeamChanged(team);
        notifyPropertyChanged(BR.leftTeamImageUrl);
    }

    private void updateRightTeam(Team team) {
        mRightTeam = team;
        mInteraction.onRightTeamChanged(team);
        notifyPropertyChanged(BR.rightTeamImageUrl);
    }

    private void getOpponentFavoriteTeam() {
        Subscription s = RetrievalManager.getTeam(mOpponent.getFavoriteTeamId()).subscribe(new ObservableUtils.OnNextObserver<Team>() {
            @Override
            public void onNext(Team team) {
                if (team != null) {
                    mOpponentTeam = team;
                    notifyTeamChanged(false, team);
                }
            }
        });
        addSubscription(s);
    }

    public void onLeftTeamClicked() {
        onTeamClicked(true);
    }

    public void onRightTeamClicked() {
        onTeamClicked(false);
    }

    private void onTeamClicked(boolean isLeftTeam) {
        mIsSelectedTeamLeft = isLeftTeam;
        Intent intent = new Intent(mLauncher.getContext(), PickTeamActivity.class);
        int requestCode = mIsPartOfSeries ?
                CreateSeriesMatchListFragment.CREATE_SERIES_REQUEST_CODE :
                CreateMatchFragment.CREATE_MATCH_REQUEST_CODE;
        mLauncher.launchActivity(intent, requestCode, null);
    }

    public void updateTeam(Team team) {
        if (mIsSelectedTeamLeft) {
            updateLeftTeam(team);
        } else {
            updateRightTeam(team);
        }
    }

    public void onSwapSidesButtonClicked() {
        mIsSwapped = !mIsSwapped;
        Team tempTeam = mLeftTeam;
        mLeftTeam = mRightTeam;
        mRightTeam = tempTeam;
        Player tempPlayer = mLeftPlayer;
        mLeftPlayer = mRightPlayer;
        mRightPlayer = tempPlayer;
        mInteraction.onSidesSwapped();
        notifyChange();
    }

    @Bindable
    public String getLeftImageUrl() {
        return mLeftPlayer.getImageUrl();
    }

    @Bindable
    public String getRightImageUrl() {
        return mRightPlayer.getImageUrl();
    }

    @Bindable
    public String getLeftTeamImageUrl() {
        return mLeftTeam != null ? CrestUrlResizer.resizeMedium(mLeftTeam.getCrestUrl()) : null;
    }

    @Bindable
    public String getRightTeamImageUrl() {
        return mRightTeam != null ? CrestUrlResizer.resizeMedium(mRightTeam.getCrestUrl()) : null;
    }

    public int getTeamVisibility() {
        return mIsPartOfSeries ? View.GONE : View.VISIBLE;
    }

    public boolean isSwapped() {
        return mIsSwapped;
    }

    @Override
    public void destroy() {
        super.destroy();
        mLauncher = null;
        mInteraction = null;
    }

    @Override
    public Bundle saveInstanceState(Bundle bundle) {
        bundle.putSerializable(USER_TEAM, mUserTeam);
        bundle.putSerializable(OPPONENT_TEAM, mOpponentTeam);
        bundle.putBoolean(SWAPPED, mIsSwapped);
        return bundle;
    }

    public interface CreateEventHeaderInteraction {
        void onSidesSwapped();
        void onLeftTeamChanged(Team team);
        void onRightTeamChanged(Team team);
    }
}
