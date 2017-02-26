package com.example.kevin.fifastatistics.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.view.View;
import android.widget.AdapterView;

import com.android.databinding.library.baseAdapters.BR;
import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.activities.FifaBaseActivity;
import com.example.kevin.fifastatistics.interfaces.OnMatchUpdatedListener;
import com.example.kevin.fifastatistics.interfaces.OnSeriesCompletedListener;
import com.example.kevin.fifastatistics.interfaces.OnSeriesScoreUpdateListener;
import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.match.Series;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.network.ApiAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;
import rx.Observable;
import rx.Subscriber;

public class CreateSeriesMatchListViewModel extends BaseObservable implements OnMatchUpdatedListener {

    private final ObservableList<CreateSeriesListItemViewModel> mItems;
    private final ItemView mItemView;
    private FifaBaseActivity mActivity;
    private User mUser;
    private Friend mOpponent;
    private CreateSeriesScoreViewModel mSeriesScoreViewModel;
    private List<OnMatchUpdatedListener> mOnMatchUpdateListeners;
    private OnSeriesCompletedListener mOnSeriesCompletedListener;

    private int mUserWins;
    private int mOpponentWins;
    private int mMaxSeriesLength;

    public CreateSeriesMatchListViewModel(FifaBaseActivity activity, User user, Friend opponent, OnSeriesScoreUpdateListener scoreUpdateListener,
                                          OnSeriesCompletedListener seriesCompletedListener) {
        mItems = new ObservableArrayList<>();
        mItemView = ItemView.of(BR.listItemViewModel, R.layout.item_create_series_match_list);
        mActivity = activity;
        mUser = user;
        mOpponent = opponent;
        mSeriesScoreViewModel = new CreateSeriesScoreViewModel(user, opponent, scoreUpdateListener);
        mOnSeriesCompletedListener = seriesCompletedListener;
        mMaxSeriesLength = Series.DEFAULT_MAX_SERIES_LENGTH;
        mOnMatchUpdateListeners = Arrays.asList(mSeriesScoreViewModel, this);
    }

    public void add(Match match) {
        mItems.add(new CreateSeriesListItemViewModel(mActivity, match, mUser, mOpponent, mItems.size() + 1, mOnMatchUpdateListeners));
        updateSeriesScoreViewModel(match);
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
            mOnSeriesCompletedListener.onSeriesCompleted(series);
        }
    }

    private boolean didWinSeries(int playerWins) {
        return playerWins > mMaxSeriesLength / 2;
    }

    private Series getSeries() {
        List<Match> matches = getMatches();
        Series series = new Series(Friend.fromUser(mUser), mOpponent);
        series.addAll(matches);
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
        Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                SharedPreferencesManager.storeCurrentSeries(getMatches());
                subscriber.onCompleted();
            }
        }).subscribe(ApiAdapter.EMPTY_OBSERVER);
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
            maybeEndSeries(newMatch);
            maybeResumeSeries();
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

    public ObservableList<CreateSeriesListItemViewModel> getItems() {
        return mItems;
    }

    public ItemView getItemView() {
        return mItemView;
    }
}