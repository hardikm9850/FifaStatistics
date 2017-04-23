package com.example.kevin.fifastatistics.viewmodels;

import android.content.Context;
import android.content.Intent;
import android.databinding.Bindable;
import android.view.View;

import com.example.kevin.fifastatistics.BR;
import com.example.kevin.fifastatistics.activities.PickTeamActivity;
import com.example.kevin.fifastatistics.fragments.AddMatchDialogFragment;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.interfaces.OnTeamSelectedListener;
import com.example.kevin.fifastatistics.managers.CrestUrlResizer;
import com.example.kevin.fifastatistics.managers.RetrievalManager;
import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.utils.ObservableUtils;

import rx.Observable;
import rx.Subscription;

public class AddMatchFragmentViewModel extends FifaBaseViewModel implements OnTeamSelectedListener {

    private Player mUser;
    private Player mOpponent;
    private Context mContext;
    private ActivityLauncher mLauncher;
    private Team mUserTeam;
    private Team mOpponentTeam;
    private AddMatchFragmentViewModelInteraction mInteraction;
    private boolean mIsPartOfSeries;
    private boolean mIsSelectedTeamUser;

    public AddMatchFragmentViewModel(Player user, Player opponent, Context context, ActivityLauncher launcher,
                                     boolean isPartOfSeries, AddMatchFragmentViewModelInteraction interaction) {
        mUser = user;
        mOpponent = opponent;
        mContext = context;
        mIsPartOfSeries = isPartOfSeries;
        mLauncher = launcher;
        mInteraction = interaction;
        getFavoriteTeam();
        getOpponentFavoriteTeam();
    }

    private void getFavoriteTeam() {
        Observable.<Team>create(s -> s.onNext(SharedPreferencesManager.getFavoriteTeam()))
                .compose(ObservableUtils.applySchedulers()).subscribe(team -> {
            if (team != null) {
                changeUserTeam(team);
            }
        });
    }

    private void changeUserTeam(Team team) {
        mUserTeam = team;
        notifyPropertyChanged(BR.userTeamImageUrl);
        if (mInteraction != null) {
            mInteraction.onUserTeamChange(team);
        }
    }

    private void getOpponentFavoriteTeam() {
        Subscription s = RetrievalManager.getTeam(mOpponent.getFavoriteTeamId()).subscribe(new ObservableUtils.OnNextObserver<Team>() {
            @Override
            public void onNext(Team team) {
                if (team != null) {
                   changeOpponentTeam(team);
                }
            }
        });
        addSubscription(s);
    }

    private void changeOpponentTeam(Team team) {
        mOpponentTeam = team;
        notifyPropertyChanged(BR.opponentTeamImageUrl);
        if (mInteraction != null) {
            mInteraction.onOpponentTeamChange(team);
        }
    }

    public String getUserImageUrl() {
        return mUser.getImageUrl();
    }

    public String getOpponentImageUrl() {
        return mOpponent.getImageUrl();
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
        mLauncher.launchActivity(intent, AddMatchDialogFragment.ADD_MATCH_REQUEST_CODE, null);
    }

    @Override
    public void onTeamSelected(Team team) {
        if (mIsSelectedTeamUser) {
            changeUserTeam(team);
        } else {
            changeOpponentTeam(team);
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

    public int getTeamVisibility() {
        return mIsPartOfSeries ? View.GONE : View.VISIBLE;
    }

    public void swap() {
        Team temp = mUserTeam;
        mUserTeam = mOpponentTeam;
        mOpponentTeam = temp;
        if (mInteraction != null) {
            mInteraction.onOpponentTeamChange(mOpponentTeam);
            mInteraction.onUserTeamChange(mUserTeam);
        }
        notifyChange();
    }

    public interface AddMatchFragmentViewModelInteraction {
        void onUserTeamChange(Team team);
        void onOpponentTeamChange(Team team);
    }
}
