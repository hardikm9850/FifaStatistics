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

public class MatchesFragmentViewModel extends ProgressFragmentViewModel {

    private static final String TAG = "MatchesFragmentVM";

    private final OnMatchesLoadedListener mOnMatchesLoadedListener;
    private final AdapterInteraction<MatchProjection> mAdapterInteraction;
    private final Player mUser;
    private List<MatchProjection> mMatches;
    private String mNextUri;
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
                notifyHideProgressBar();
                Log.e(TAG, e.getMessage());
                mNextUri = null;
                mOnMatchesLoadedListener.onMatchesLoadFailure();
                mIsLoadInProgress = false;
            }

            @Override
            public void onNext(ApiListResponse<MatchProjection> response) {
                notifyHideProgressBar();
                mMatches = response.getItems();
                mNextUri = response.getNext();
                mOnMatchesLoadedListener.onMatchesLoadSuccess(mMatches);
                mIsLoadInProgress = false;
            }
        };
        Subscription s = ApiAdapter.getFifaApi().getMatches().compose(ObservableUtils.applySchedulers()).subscribe(matchObserver);
        addSubscription(s);
    }

    public void loadMore(int page) {
        if (!mIsLoadInProgress && mNextUri != null) {
            mIsLoadInProgress = true;
            mAdapterInteraction.notifyLoadingItems();
            Subscription s = ApiAdapter.getFifaApi().getNextMatches(mNextUri)
                    .compose(ObservableUtils.applySchedulers()).subscribe(mLoadMoreObserver);
            addSubscription(s);
        } else {
            mAdapterInteraction.notifyNoMoreItemsToLoad();
        }
    }

    private final Observer<ApiListResponse<MatchProjection>> mLoadMoreObserver = new ObservableUtils.EmptyOnCompleteObserver<ApiListResponse<MatchProjection>>() {
        @Override
        public void onNext(ApiListResponse<MatchProjection> response) {
            mIsLoadInProgress =  false;
            mNextUri = response.getNext();
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
            mNextUri = null;
            mOnMatchesLoadedListener.onMatchesLoadFailure();
            mAdapterInteraction.notifyNoMoreItemsToLoad();
        }
    };

    public interface OnMatchesLoadedListener {
        void onMatchesLoadSuccess(List<MatchProjection> matches);
        void onMatchesLoadFailure();
    }
}
