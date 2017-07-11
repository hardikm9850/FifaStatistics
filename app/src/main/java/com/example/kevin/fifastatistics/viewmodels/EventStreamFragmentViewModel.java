package com.example.kevin.fifastatistics.viewmodels;

import android.util.Log;

import com.example.kevin.fifastatistics.interfaces.AdapterInteraction;
import com.example.kevin.fifastatistics.models.ApiListResponse;
import com.example.kevin.fifastatistics.models.databasemodels.match.FifaEvent;
import com.example.kevin.fifastatistics.utils.ObservableUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

public class EventStreamFragmentViewModel<T extends FifaEvent> extends ProgressFragmentViewModel {

    private static final String TAG = "MatchesFragmentVM";

    private final OnEventsLoadedListener<T> mOnEventsLoadedListener;
    private final AdapterInteraction mAdapterInteraction;
    private final OnLoadMoreHandler<T> mLoadMoreHandler;
    private Observable<ApiListResponse<T>> mLoadEventsObservable;
    private List<T> mEvents;
    private String mNextUri;
    private boolean mIsLoadInProgress;

    public EventStreamFragmentViewModel(OnEventsLoadedListener<T> listener, AdapterInteraction interaction,
                                        OnLoadMoreHandler<T> loadMoreHandler) {
        mOnEventsLoadedListener = listener;
        mAdapterInteraction = interaction;
        mLoadMoreHandler = loadMoreHandler;
        mEvents = new ArrayList<>();
    }

    public void loadEvents(Observable<ApiListResponse<T>> eventsObservable) {
        mIsLoadInProgress = true;
        mLoadEventsObservable = eventsObservable;
        showProgressBar();
        Observer<ApiListResponse<T>> eventsObserver = new ObservableUtils.EmptyOnCompleteObserver<ApiListResponse<T>>() {
            @Override
            public void onError(Throwable e) {
                hideProgressBar();
                showRetryButton();
                Log.e(TAG, e.getMessage());
                mNextUri = null;
                mOnEventsLoadedListener.onEventsLoadFailure();
                mIsLoadInProgress = false;
            }

            @Override
            public void onNext(ApiListResponse<T> response) {
                hideProgressBar();
                mEvents = response.getItems();
                mNextUri = response.getNext();
                mOnEventsLoadedListener.onEventsLoadSuccess(mEvents);
                mIsLoadInProgress = false;
            }
        };
        Subscription s = eventsObservable.compose(ObservableUtils.applySchedulers()).subscribe(eventsObserver);
        addSubscription(s);
    }

    public void loadMore(int page) {
        if (!mIsLoadInProgress && mNextUri != null) {
            mIsLoadInProgress = true;
            mAdapterInteraction.notifyLoadingItems();
            Subscription s = mLoadMoreHandler.getLoadMoreObservable(mNextUri)
                    .compose(ObservableUtils.applySchedulers()).subscribe(mLoadMoreObserver);
            addSubscription(s);
        } else {
            mAdapterInteraction.notifyNoMoreItemsToLoad();
        }
    }

    private final Observer<ApiListResponse<T>> mLoadMoreObserver = new ObservableUtils.EmptyOnCompleteObserver<ApiListResponse<T>>() {
        @Override
        public void onNext(ApiListResponse<T> response) {
            mIsLoadInProgress =  false;
            mNextUri = response.getNext();
            List<T> events = response.getItems();
            if (events != null && events.size() > 0) {
                int startPos = mEvents.size();
                mEvents.addAll(events);
                mAdapterInteraction.notifyItemsInserted(startPos, events.size());
            } else {
                mAdapterInteraction.notifyNoMoreItemsToLoad();
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.d(TAG, e.getMessage());
            mIsLoadInProgress = false;
            mNextUri = null;
            mOnEventsLoadedListener.onEventsLoadFailure();
            mAdapterInteraction.notifyNoMoreItemsToLoad();
        }
    };

    @Override
    public void onRetryButtonClick() {
        loadEvents(mLoadEventsObservable);
    }

    public interface OnEventsLoadedListener<T extends FifaEvent> {
        void onEventsLoadSuccess(List<T> events);
        void onEventsLoadFailure();
    }

    public interface OnLoadMoreHandler<T extends FifaEvent> {
        Observable<ApiListResponse<T>> getLoadMoreObservable(String nextUri);
    }
}
