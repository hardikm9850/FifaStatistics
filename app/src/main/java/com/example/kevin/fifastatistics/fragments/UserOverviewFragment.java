package com.example.kevin.fifastatistics.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.databinding.FragmentUserOverviewBinding;
import com.example.kevin.fifastatistics.event.EventBus;
import com.example.kevin.fifastatistics.event.SeriesRemovedEvent;
import com.example.kevin.fifastatistics.event.UpdateRemovedEvent;
import com.example.kevin.fifastatistics.interfaces.OnBackPressedHandler;
import com.example.kevin.fifastatistics.listeners.SimpleAnimationListener;
import com.example.kevin.fifastatistics.managers.sync.CurrentSeriesSynchronizer;
import com.example.kevin.fifastatistics.managers.sync.MatchUpdateSynchronizer;
import com.example.kevin.fifastatistics.managers.preferences.PrefsManager;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchUpdate;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.viewmodels.UserOverviewViewModel;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;

/**
 * The main overview fragment for the current user.
 */
public class UserOverviewFragment extends FifaBaseFragment implements OnBackPressedHandler, UserOverviewViewModel.UserOverviewViewModelInteraction {

    private static boolean sIsUpdated;

    private User mUser;
    private FragmentUserOverviewBinding mBinding;
    private UserOverviewViewModel mViewModel;
    private View.OnScrollChangeListener mScrollListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof View.OnScrollChangeListener) {
            mScrollListener = (View.OnScrollChangeListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mScrollListener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = PrefsManager.getUser();
        observeUpdateRemovedEvents();
        observerSeriesRemovedEvents();
    }

    private void observeUpdateRemovedEvents() {
        EventBus.getInstance().observeEvents(UpdateRemovedEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                    MatchUpdate update = event.getUpdate();
                    if (mViewModel != null) {
                        mViewModel.removePendingUpdate(update);
                    }
                });
    }

    private void observerSeriesRemovedEvents() {
        EventBus.getInstance().observeEvents(SeriesRemovedEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                    if (mViewModel != null) {
                        mViewModel.removeSeriesWithOpponentId(event.getOpponentId());
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_overview, container, false);
        List<MatchUpdate> updates = PrefsManager.getMatchUpdates();
        mViewModel = new UserOverviewViewModel(mUser, this, updates, this);
        mBinding.setViewModel(mViewModel);
        mBinding.swiperefresh.setOnRefreshListener(() -> mViewModel.update());
        mBinding.scrollview.setOnScrollChangeListener(mScrollListener);
        refreshUserAfterLayoutAnimationComplete();
        return mBinding.getRoot();
    }

    private void refreshUserAfterLayoutAnimationComplete() {
        ((ViewGroup) mBinding.overviewLayout.getRoot()).setLayoutAnimationListener(
                new SimpleAnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mBinding.swiperefresh.setEnabled(true);
                        if (!sIsUpdated) {
                            refresh();
                        }
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mViewModel != null) {
            mViewModel.destroy();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        stopRefreshing();
        mBinding.swiperefresh.setEnabled(false);
    }

    private void stopRefreshing() {
        mBinding.swiperefresh.setRefreshing(false);
    }

    private void refresh() {
        mBinding.swiperefresh.setRefreshing(true);
        mViewModel.update();
    }

    @Override
    public boolean handleBackPress() {
        return false;
    }

    @Override
    public void onUserUpdateSuccess(User user) {
        sIsUpdated = true;
        PrefsManager.storeUser(user);
        checkMatchUpdates(user);
        checkCurrentSeries(user);
    }

    private void checkMatchUpdates(User user) {
        MatchUpdateSynchronizer s = MatchUpdateSynchronizer.builder()
                .user(user)
                .onSyncErrorHandler(e -> onUserUpdateFailure())
                .onSyncSuccessHandler(updates -> mViewModel.setPendingUpdates(updates))
                .onSyncCompleteHandler(this::stopRefreshing)
                .build();
        addSubscription(s.sync());
    }

    private void checkCurrentSeries(User user) {
        CurrentSeriesSynchronizer s = CurrentSeriesSynchronizer.builder()
                .user(user)
                .onSyncSuccessHandler(series -> mViewModel.setCurrentSeries(series))
                .onSyncCompleteHandler(this::stopRefreshing)
                .onSyncErrorHandler(e -> onUserUpdateFailure())
                .build();
        addSubscription(s.get());
    }

    @Override
    public void onUserUpdateFailure() {
        stopRefreshing();
        // TODO show sync failed message
        sIsUpdated = false;
    }
}
