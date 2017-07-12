package com.example.kevin.fifastatistics.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.view.View;
import android.widget.AdapterView;

import com.android.databinding.library.baseAdapters.BR;
import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.activities.FifaBaseActivity;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.interfaces.OnMatchCreatedListener;
import com.example.kevin.fifastatistics.interfaces.OnMatchUpdatedListener;
import com.example.kevin.fifastatistics.interfaces.OnSeriesCompletedListener;
import com.example.kevin.fifastatistics.interfaces.OnSeriesScoreUpdateListener;
import com.example.kevin.fifastatistics.interfaces.OnSeriesUpdatedListener;
import com.example.kevin.fifastatistics.interfaces.OnTeamSelectedListener;
import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.match.Series;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.utils.CollectionUtils;
import com.example.kevin.fifastatistics.utils.ObservableUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;
import rx.Observable;

public class CreateSeriesMatchListViewModel extends BaseObservable implements OnMatchUpdatedListener, OnTeamSelectedListener, OnMatchCreatedListener {

    private final ObservableList<CreateSeriesListItemViewModel> mItems;
    private final ItemView mItemView;
    private FifaBaseActivity mActivity;
    private User mUser;
    private Friend mOpponent;
    private CreateSeriesScoreViewModel mSeriesScoreViewModel;
    private List<OnMatchUpdatedListener> mOnMatchUpdateListeners;
    private OnSeriesCompletedListener mOnSeriesCompletedListener;
    private OnSeriesUpdatedListener mOnSeriesUpdateListener;

    private int mUserWins;
    private int mOpponentWins;
    private int mMaxSeriesLength;
    private int mUpdatedMatchIndex;
    private boolean mIsSeriesDone;

    public CreateSeriesMatchListViewModel(FifaBaseActivity activity, User user, Friend opponent, OnSeriesScoreUpdateListener scoreUpdateListener,
                                          OnSeriesCompletedListener seriesCompletedListener, ActivityLauncher launcher, OnSeriesUpdatedListener listener, Series series) {
        mItems = new ObservableArrayList<>();
        mItemView = ItemView.of(BR.listItemViewModel, R.layout.item_create_series_match_list);
        mActivity = activity;
        mUser = user;
        mOpponent = opponent;
        mOnSeriesUpdateListener = listener;
        mSeriesScoreViewModel = new CreateSeriesScoreViewModel(user, opponent, scoreUpdateListener, activity, launcher, series);
        mOnSeriesCompletedListener = seriesCompletedListener;
        mMaxSeriesLength = Series.DEFAULT_MAX_SERIES_LENGTH;
        mUpdatedMatchIndex = -1;
        mOnMatchUpdateListeners = Arrays.asList(mSeriesScoreViewModel, this);
        restoreSeries(series);
    }

    private void restoreSeries(Series series) {
        if (series != null && series.getMatches() != null) {
            for (Match match : series.getMatches()) {
                mItems.add(new CreateSeriesListItemViewModel(mActivity, match, mUser, mOpponent, mItems.size() + 1, mOnMatchUpdateListeners));
                maybeEndSeries(match);
            }
        }
    }

    public void add(Match match) {
        mItems.add(new CreateSeriesListItemViewModel(mActivity, match, mUser, mOpponent, mItems.size() + 1, mOnMatchUpdateListeners));
        updateSeriesScoreViewModel(match);
        notifySeriesUpdated();
        maybeEndSeries(match);
        storeSeries();
    }

    private void updateSeriesScoreViewModel(Match match) {
        if (match.didWin(mUser)) {
            mSeriesScoreViewModel.incrementUserWins();
        } else {
            mSeriesScoreViewModel.incrementOpponentWins();
        }
    }

    private void maybeEndSeries(Match match) {
        if (match.didWin(mUser)) {
            mUserWins++;
        } else {
            mOpponentWins++;
        }
        if (didWinSeries(mUserWins) || didWinSeries(mOpponentWins)) {
            Series series = getSeries();
            mIsSeriesDone = true;
            mOnSeriesCompletedListener.onSeriesCompleted(series);
        }
    }

    private boolean didWinSeries(int playerWins) {
        return playerWins > mMaxSeriesLength / 2;
    }

    private Series getSeries() {
        List<Match> matches = getMatches();
        Series series = new Series(Friend.fromPlayer(mUser), mOpponent);
        series.addAll(matches);
        if (mUserWins > mOpponentWins) {
            series.setTeamWinner(mSeriesScoreViewModel.getUserTeam());
            series.setTeamLoser(mSeriesScoreViewModel.getOpponentTeam());
        } else {
            series.setTeamWinner(mSeriesScoreViewModel.getOpponentTeam());
            series.setTeamLoser(mSeriesScoreViewModel.getUserTeam());
        }
        return series;
    }

    private List<Match> getMatches() {
        List<Match> matches = new ArrayList<>(mMaxSeriesLength);
        for (CreateSeriesListItemViewModel item : mItems) {
            matches.add(item.getMatch());
        }
        return matches;
    }

    private void storeSeries() {
        Observable.create(s -> SharedPreferencesManager.storeCurrentSeries(getMatches(), mOpponent.getId()))
                .compose(ObservableUtils.applyBackground()).subscribe();
    }

    private void notifySeriesUpdated() {
        if (mOnSeriesUpdateListener != null) {
            mOnSeriesUpdateListener.onSeriesUpdated(getSeries());
        }
    }

    @SuppressWarnings("unused")
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CreateSeriesListItemViewModel item = (CreateSeriesListItemViewModel) parent.getAdapter().getItem(position);
        item.onItemClicked();
    }

    @Override
    public void onMatchUpdated(Match oldMatch, Match newMatch) {
        Friend oldWinner = oldMatch.getWinner();
        Friend newWinner = newMatch.getWinner();
        if (!oldWinner.equals(newWinner)) {
            if (mUser.equals(newWinner)) {
                mOpponentWins--;
            } else {
                mUserWins--;
            }
            notifySeriesUpdated();
            maybeEndSeries(newMatch);
            maybeResumeSeries();
        } else {
            notifySeriesUpdated();
        }
        storeSeries();
    }

    private void maybeResumeSeries() {
        if (!didWinSeries(mUserWins) && !didWinSeries(mOpponentWins)) {
            mOnSeriesCompletedListener.onSeriesContinued();
        }
    }

    public CreateSeriesScoreViewModel getSeriesScoreViewModel() {
        return mSeriesScoreViewModel;
    }

    public void setOnSeriesScoreUpdateListener(OnSeriesScoreUpdateListener listener) {
        mSeriesScoreViewModel.setSeriesScoreUpdateListener(listener);
    }

    @Override
    public void onTeamSelected(Team team) {
        mSeriesScoreViewModel.onTeamSelected(team);
        if (mIsSeriesDone) {
            mOnSeriesCompletedListener.onSeriesCompleted(getSeries());
        }
    }

    @Override
    public void onMatchCreated(Match match) {
        if (mUpdatedMatchIndex >= 0) {
            if (mUpdatedMatchIndex < CollectionUtils.getSize(mItems)) {
                mItems.get(mUpdatedMatchIndex).onMatchUpdated(match);
            }
        } else {
            add(match);
        }
    }

    @Override
    public void setMatchIndex(int index) {
        mUpdatedMatchIndex = index;
    }

    public ObservableList<CreateSeriesListItemViewModel> getItems() {
        return mItems;
    }

    public ItemView getItemView() {
        return mItemView;
    }

    public Team getUserTeam() {
        return mSeriesScoreViewModel.getUserTeam();
    }

    public Team getOpponentTeam() {
        return mSeriesScoreViewModel.getOpponentTeam();
    }

    public void setTeams(Team userTeam, Team opponentTeam) {
        if (userTeam != null || opponentTeam != null) {
            mSeriesScoreViewModel.setUserTeam(userTeam);
            mSeriesScoreViewModel.setOpponentTeam(opponentTeam);
        }
    }
}
