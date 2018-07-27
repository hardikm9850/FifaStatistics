package com.example.kevin.fifastatistics.viewmodels;

import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.util.Log;
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
import com.example.kevin.fifastatistics.managers.preferences.PrefsManager;
import com.example.kevin.fifastatistics.managers.sync.CurrentSeriesSynchronizer;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.match.Series;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.utils.CollectionUtils;
import com.example.kevin.fifastatistics.viewmodels.item.CreateSeriesListItemViewModel;
import com.example.kevin.fifastatistics.views.DiscreteNumberPicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;

public class CreateSeriesMatchListViewModel extends FifaBaseViewModel implements OnMatchUpdatedListener,
        OnTeamSelectedListener, OnMatchCreatedListener, DiscreteNumberPicker.OnNumberChangedListener {

    private final ObservableList<CreateSeriesListItemViewModel> mItems;
    private final ItemView mItemView;
    private ActivityLauncher mLauncher;
    private User mUser;
    private Friend mOpponent;
    private CreateSeriesScoreViewModel mSeriesScoreViewModel;
    private List<OnMatchUpdatedListener> mOnMatchUpdateListeners;
    private OnSeriesCompletedListener mOnSeriesCompletedListener;
    private OnSeriesUpdatedListener mOnSeriesUpdateListener;
    private CurrentSeriesSynchronizer mSeriesSynchronizer;

    private int mUserWins;
    private int mOpponentWins;
    private int mMaxSeriesLength;
    private int mUpdatedMatchIndex;
    private boolean mIsSeriesDone;

    public CreateSeriesMatchListViewModel(FifaBaseActivity activity, User user, Friend opponent, OnSeriesScoreUpdateListener scoreUpdateListener,
                                          OnSeriesCompletedListener seriesCompletedListener, ActivityLauncher launcher, OnSeriesUpdatedListener listener,
                                          Series series, Team userTeam, Team opponentTeam) {
        mItems = new ObservableArrayList<>();
        mItemView = ItemView.of(BR.listItemViewModel, R.layout.item_create_series_match_list);
        mLauncher = launcher;
        mUser = user;
        mOpponent = opponent;
        mOnSeriesUpdateListener = listener;
        mSeriesSynchronizer = CurrentSeriesSynchronizer.with(user, activity);
        mSeriesScoreViewModel = new CreateSeriesScoreViewModel(user, opponent,
                scoreUpdateListener, activity, launcher, series, userTeam, opponentTeam);
        mOnSeriesCompletedListener = seriesCompletedListener;
        mUpdatedMatchIndex = -1;
        mOnMatchUpdateListeners = Arrays.asList(mSeriesScoreViewModel, this);
        restoreSeries(series);
    }

    private void restoreSeries(Series series) {
        mMaxSeriesLength = PrefsManager.getDefaultSeriesLength();
        if (series != null && series.getMatches() != null) {
            int numberOfMatches = series.getMatches().size();
            if (numberOfMatches > mMaxSeriesLength) {
                mMaxSeriesLength = numberOfMatches + 1 - (numberOfMatches % 2);
            }
            for (Match match : series.getMatches()) {
                mItems.add(new CreateSeriesListItemViewModel(mLauncher, match, mUser, mOpponent, mItems.size() + 1, mOnMatchUpdateListeners));
                maybeEndSeries(match);
            }
        }
    }

    public void add(Match match) {
        mItems.add(new CreateSeriesListItemViewModel(mLauncher, match, mUser, mOpponent, mItems.size() + 1, mOnMatchUpdateListeners));
        updateSeriesScoreViewModel(match);
        notifySeriesUpdated();
        maybeEndSeries(match);
        storeSeries();
    }

    private void updateSeriesScoreViewModel(Match match) {
        if (match.wonBy(mUser)) {
            mSeriesScoreViewModel.incrementUserWins();
        } else {
            mSeriesScoreViewModel.incrementOpponentWins();
        }
    }

    private void maybeEndSeries(Match match) {
        if (match.wonBy(mUser)) {
            mUserWins++;
        } else {
            mOpponentWins++;
        }
        completeSeriesIfWon();
    }

    private void completeSeriesIfWon() {
        if (didWinSeries(mUserWins) || didWinSeries(mOpponentWins)) {
            Series series = getSeries();
            series.setBestOf(getMinimumSeriesLength());
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
        series.setBestOf(mMaxSeriesLength);
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
        Team userTeam = mSeriesScoreViewModel.getUserTeam();
        Team opponentTeam = mSeriesScoreViewModel.getOpponentTeam();
        mSeriesSynchronizer.save(new ArrayList<>(getMatches()), mOpponent, userTeam, opponentTeam);
    }

    private void notifySeriesUpdated() {
        notifyPropertyChanged(BR.minimumSeriesLength);
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

    @Override
    public void destroy() {
        super.destroy();
        mOnSeriesUpdateListener = null;
        mOnMatchUpdateListeners = null;
        mOnSeriesCompletedListener = null;
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

    public int getDefaultSeriesLength() {
        return mMaxSeriesLength;
    }

    @Bindable
    public int getMinimumSeriesLength() {
        int mostWins = Math.max(mUserWins, mOpponentWins);
        return Math.max((mostWins*2) - 1, Series.MIN_SERIES_LENGTH);
    }

    @Override
    public void onNumberPickerNumberChanged(int newNumber) {
        mMaxSeriesLength = newNumber;
        if (isSeriesIncomplete()) {
            mOnSeriesCompletedListener.onSeriesContinued();
        } else {
            completeSeriesIfWon();
        }
    }

    private boolean isSeriesIncomplete() {
        return !didWinSeries(mUserWins) && !didWinSeries(mOpponentWins);
    }
}
