package com.example.kevin.fifastatistics.viewmodels;

import android.view.View;

import com.example.kevin.fifastatistics.managers.RetrievalManager;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.utils.ObservableUtils;

import rx.Subscription;

public class PlayerOverviewFragmentViewModel extends ProgressFragmentViewModel {

    private OnPlayerLoadedListener mListener;
    private String mPlayerId;

    public PlayerOverviewFragmentViewModel(OnPlayerLoadedListener listener, String playerId) {
        mListener = listener;
        mPlayerId = playerId;
    }

    public void loadPlayer() {
        showProgressBar();
        Subscription userSub = RetrievalManager.getUser(mPlayerId)
            .subscribe(new ObservableUtils.EmptyOnCompleteObserver<User>() {
                @Override
                public void onError(Throwable e) {
                    hideProgressBar();
                    showRetryButton();
                    if (mListener != null) {
                        mListener.onPlayerLoadFailed(e);
                    }
                }

                @Override
                public void onNext(User user) {
                    hideProgressBar();
                    if (mListener != null) {
                        mListener.onPlayerLoaded(user);
                    }
                }
            });
        addSubscription(userSub);
    }

    @Override
    public void destroy() {
        mListener = null;
    }

    @Override
    public void onRetryButtonClick() {
        loadPlayer();
    }

    public interface OnPlayerLoadedListener {
        void onPlayerLoaded(User player);
        void onPlayerLoadFailed(Throwable t);
    }
}
