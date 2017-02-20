package com.example.kevin.fifastatistics.viewmodels;

import android.util.Log;

import com.example.kevin.fifastatistics.interfaces.AdapterInteraction;
import com.example.kevin.fifastatistics.models.apiresponses.ApiListResponse;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchProjection;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.network.ApiAdapter;
import com.example.kevin.fifastatistics.utils.ObservableUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;

public class MatchesFragmentViewModel extends FifaBaseViewModel {

    private static final String TAG = "MatchesFragmentVM";

    private final OnMatchesLoadedListener mOnMatchesLoadedListener;
    private final AdapterInteraction<MatchProjection> mAdapterInteraction;
    private final Player mUser;
    private List<MatchProjection> mMatches;
    private boolean mIsLoadInProgress;

    public MatchesFragmentViewModel(OnMatchesLoadedListener listener, Player user, AdapterInteraction<MatchProjection> interaction) {
        mOnMatchesLoadedListener = listener;
        mAdapterInteraction = interaction;
        mUser = user;
        mMatches = new ArrayList<>();
    }

    public void loadMatches() {
        mIsLoadInProgress = true;
        Observer<ApiListResponse<MatchProjection>> matchObserver = new ObservableUtils.EmptyOnCompleteObserver<ApiListResponse<MatchProjection>>() {
            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.getMessage());
                mOnMatchesLoadedListener.onMatchesLoadFailure();
                mIsLoadInProgress = false;
            }

            @Override
            public void onNext(ApiListResponse<MatchProjection> response) {
                mMatches = response.getItems();
                mOnMatchesLoadedListener.onMatchesLoadSuccess(mMatches);
                mIsLoadInProgress = false;
            }
        };
        Subscription s = ApiAdapter.getFifaApi().getMatches().compose(ObservableUtils.applySchedulers()).subscribe(matchObserver);
        addSubscription(s);
    }

    public void loadMore(int page) {
        if (!mIsLoadInProgress) {
            mIsLoadInProgress = true;
            mAdapterInteraction.notifyLoadingItems();
            Subscription s = ApiAdapter.getFifaApi().getMatches(page).compose(ObservableUtils.applySchedulers()).subscribe(mLoadMoreObserver);
            addSubscription(s);
        }
    }

    private final Observer<ApiListResponse<MatchProjection>> mLoadMoreObserver = new ObservableUtils.EmptyOnCompleteObserver<ApiListResponse<MatchProjection>>() {
        @Override
        public void onNext(ApiListResponse<MatchProjection> response) {
            mIsLoadInProgress =  false;
            List<MatchProjection> matches = response.getItems();
            if (matches != null && matches.size() > 0) {
                int startPos = mMatches.size();
                mMatches.addAll(matches);
                mAdapterInteraction.notifyItemsInserted(startPos, matches.size());
            } else {
                mAdapterInteraction.notifyNoMoreItemsToLoad();
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.d(TAG, e.getMessage());
            mIsLoadInProgress = false;
            mOnMatchesLoadedListener.onMatchesLoadFailure();
            mAdapterInteraction.notifyNoMoreItemsToLoad();
        }
    };

    public interface OnMatchesLoadedListener {
        void onMatchesLoadSuccess(List<MatchProjection> matches);
        void onMatchesLoadFailure();
    }
}
