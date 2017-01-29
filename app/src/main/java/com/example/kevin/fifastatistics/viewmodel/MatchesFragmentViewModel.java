package com.example.kevin.fifastatistics.viewmodel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.util.Log;

import com.example.kevin.fifastatistics.BR;
import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.models.apiresponses.ApiListResponse;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchProjection;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.network.ApiAdapter;
import com.example.kevin.fifastatistics.utils.ObservableUtils;

import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;
import rx.Observer;
import rx.Subscription;

public class MatchesFragmentViewModel extends FifaBaseViewModel {

    private final OnMatchesLoadedListener mOnMatchesLoadedListener;
    private final Player mUser;

    public MatchesFragmentViewModel(OnMatchesLoadedListener listener, Player user) {
        mOnMatchesLoadedListener = listener;
        mUser = user;
    }

    public void loadMatches() {
        Observer<ApiListResponse<MatchProjection>> matchObserver = new ObservableUtils.EmptyOnCompleteObserver<ApiListResponse<MatchProjection>>() {
            @Override
            public void onError(Throwable e) {
                Log.e("error", e.getMessage());
                mOnMatchesLoadedListener.onMatchesLoadFailure();
            }

            @Override
            public void onNext(ApiListResponse<MatchProjection> response) {
                mOnMatchesLoadedListener.onMatchesLoadSuccess(response.getItems());
            }
        };
        Subscription s = ApiAdapter.getFifaApi().getMatches().compose(ObservableUtils.applySchedulers()).subscribe(matchObserver);
        addSubscription(s);
    }

    public interface OnMatchesLoadedListener {
        void onMatchesLoadSuccess(List<MatchProjection> matches);
        void onMatchesLoadFailure();
    }
}
