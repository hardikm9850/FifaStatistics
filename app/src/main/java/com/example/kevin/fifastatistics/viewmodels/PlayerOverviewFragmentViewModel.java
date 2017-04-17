package com.example.kevin.fifastatistics.viewmodels;

import com.example.kevin.fifastatistics.managers.RetrievalManager;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.utils.ObservableUtils;

import rx.Subscription;

public class PlayerOverviewFragmentViewModel extends ProgressFragmentViewModel {

    private OnPlayerLoadedListener mListener;

    public PlayerOverviewFragmentViewModel(OnPlayerLoadedListener listener) {
        mListener = listener;
    }

    public void loadPlayer(String playerId) {
        showProgressBar();
        Subscription userSub = RetrievalManager.getUser(playerId)
            .subscribe(new ObservableUtils.EmptyOnCompleteObserver<User>() {
                @Override
                public void onError(Throwable e) {
                    hideProgressBar();
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

    public interface OnPlayerLoadedListener {
        void onPlayerLoaded(User player);
        void onPlayerLoadFailed(Throwable t);
    }
}
