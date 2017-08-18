package com.example.kevin.fifastatistics.viewmodels.fragment;

import android.util.Log;

import com.example.kevin.fifastatistics.managers.RetrievalManager;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.utils.ObservableUtils;

import java.util.List;

import rx.Observer;
import rx.Subscription;

public class PlayersFragmentViewModel extends ProgressFragmentViewModel {

    private OnPlayersLoadedListener mListener;

    public PlayersFragmentViewModel(OnPlayersLoadedListener listener) {
        mListener = listener;
    }

    public void loadPlayers() {
        showProgressBar();
        Subscription s = RetrievalManager.getUsersWithoutCurrentUser().subscribe(getUserSubscription());
        addSubscription(s);
    }

    private Observer<List<User>> getUserSubscription() {
        return new ObservableUtils.EmptyOnCompleteObserver<List<User>>() {
            @Override
            public void onError(Throwable e) {
                Log.d("ERROR", Log.getStackTraceString(e));
                hideProgressBar();
                showRetryButton();
                if (mListener != null) {
                    mListener.onPlayersLoadFailed();
                }
            }

            @Override
            public void onNext(List<User> users) {
                hideProgressBar();
                if (mListener != null) {
                    mListener.onPlayersLoaded(users);
                }
            }
        };
    }

    @Override
    public void onRetryButtonClick() {
        loadPlayers();
    }

    public interface OnPlayersLoadedListener {
        void onPlayersLoaded(List<User> players);
        void onPlayersLoadFailed();
    }
}
