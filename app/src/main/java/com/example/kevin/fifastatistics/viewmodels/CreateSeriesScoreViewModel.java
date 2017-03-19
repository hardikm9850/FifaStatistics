package com.example.kevin.fifastatistics.viewmodels;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.example.kevin.fifastatistics.BR;
import com.example.kevin.fifastatistics.activities.PickTeamActivity;
import com.example.kevin.fifastatistics.fragments.CreateSeriesMatchListFragment;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.interfaces.OnMatchUpdatedListener;
import com.example.kevin.fifastatistics.interfaces.OnSeriesScoreUpdateListener;
import com.example.kevin.fifastatistics.interfaces.OnTeamSelectedListener;
import com.example.kevin.fifastatistics.managers.CrestUrlResizer;
import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.utils.ObservableUtils;

import rx.Observable;
import rx.Subscriber;

public class CreateSeriesScoreViewModel extends BaseObservable implements OnMatchUpdatedListener, OnTeamSelectedListener {

    private Player mUser;
    private Player mOpponent;
    private OnSeriesScoreUpdateListener mListener;
    private Context mContext;
    private ActivityLauncher mLauncher;
    private Team mUserTeam;
    private Team mOpponentTeam;
    private int mUserWins;
    private int mOpponentWins;
    private boolean mIsSelectedTeamUser;

    public CreateSeriesScoreViewModel(Player user, Player opponent, OnSeriesScoreUpdateListener listener,
                                      Context context, ActivityLauncher launcher) {
        mUser = user;
        mOpponent = opponent;
        mListener = listener;
        mContext = context;
        mLauncher = launcher;
        getFavoriteTeam();
    }

    private void getFavoriteTeam() {
        Observable.<Team>create(s -> s.onNext(SharedPreferencesManager.getFavoriteTeam()))
                .compose(ObservableUtils.applySchedulers()).subscribe(team -> {
            mUserTeam = team;
            notifyPropertyChanged(BR.userTeamImageUrl);
        });
    }

    public String getUserImageUrl() {
        return mUser.getImageUrl();
    }

    public String getOpponentImageUrl() {
        return mOpponent.getImageUrl();
    }

    public void incrementUserWins() {
        mListener.onUserScoreUpdate(mUserWins, mUserWins + 1);
        mUserWins++;
    }

    public void decrementUserWins() {
        mListener.onUserScoreUpdate(mUserWins, mUserWins - 1);
        mUserWins--;
    }

    public void incrementOpponentWins() {
        mListener.onOpponentScoreUpdate(mOpponentWins, mOpponentWins + 1);
        mOpponentWins++;
    }

    public void decrementOpponentWins() {
        mListener.onOpponentScoreUpdate(mOpponentWins, mOpponentWins - 1);
        mOpponentWins--;
    }

    public void setSeriesScoreUpdateListener(OnSeriesScoreUpdateListener listener) {
        mListener = listener;
    }

    public void onUserTeamClick() {
        mIsSelectedTeamUser = true;
        launchPickTeamActivity();
    }

    public void onOpponentTeamClick() {
        mIsSelectedTeamUser = false;
        launchPickTeamActivity();
    }

    private void launchPickTeamActivity() {
        Intent intent = new Intent(mContext, PickTeamActivity.class);
        mLauncher.launchActivity(intent, CreateSeriesMatchListFragment.CREATE_SERIES_REQUEST_CODE, null);
    }

    @Override
    public void onTeamSelected(Team team) {
        if (mIsSelectedTeamUser) {
            mUserTeam = team;
            notifyPropertyChanged(BR.userTeamImageUrl);
        } else {
            mOpponentTeam = team;
            notifyPropertyChanged(BR.opponentTeamImageUrl);
        }
    }

    @Bindable
    public String getUserTeamImageUrl() {
        return mUserTeam != null ? CrestUrlResizer.resizeSmall(mUserTeam.getCrestUrl()) : null;
    }

    @Bindable
    public String getOpponentTeamImageUrl() {
        return mOpponentTeam != null ? CrestUrlResizer.resizeSmall(mOpponentTeam.getCrestUrl()) : null;
    }

    public Team getUserTeam() {
        return mUserTeam;
    }

    public Team getOpponentTeam() {
        return mOpponentTeam;
    }

    @Override
    public void onMatchUpdated(Match oldMatch, Match newMatch) {
        Friend oldWinner = oldMatch.getWinner();
        Friend newWinner = newMatch.getWinner();
        if (!oldWinner.equals(newWinner)) {
            if (mUser.equals(newWinner)) {
                incrementUserWins();
                decrementOpponentWins();
            } else {
                decrementUserWins();
                incrementOpponentWins();
            }
        }
    }
}
