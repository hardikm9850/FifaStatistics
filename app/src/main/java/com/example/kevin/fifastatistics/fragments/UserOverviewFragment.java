package com.example.kevin.fifastatistics.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.databinding.FragmentUserOverviewBinding;
import com.example.kevin.fifastatistics.event.EventBus;
import com.example.kevin.fifastatistics.event.UpdateRemovedEvent;
import com.example.kevin.fifastatistics.interfaces.OnBackPressedHandler;
import com.example.kevin.fifastatistics.listeners.SimpleObserver;
import com.example.kevin.fifastatistics.managers.MatchUpdateSynchronizer;
import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.models.ApiListResponse;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchUpdate;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.network.FifaApi;
import com.example.kevin.fifastatistics.utils.ObservableUtils;
import com.example.kevin.fifastatistics.utils.TransitionUtils;
import com.example.kevin.fifastatistics.viewmodels.UserOverviewViewModel;

import java.util.List;

import rx.Observable;

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
        mUser = SharedPreferencesManager.getUser();
        observeUpdateRemovedEvents();
    }

    private void observeUpdateRemovedEvents() {
        EventBus.getInstance().observeEvents(UpdateRemovedEvent.class).subscribe(event -> {
            Log.d("Overview", "Observing event removed");
            MatchUpdate update = event.getUpdate();
            if (mViewModel != null) {
                mViewModel.removePendingUpdate(update);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_overview, container, false);
        List<MatchUpdate> updates = SharedPreferencesManager.getMatchUpdates();
        mViewModel = new UserOverviewViewModel(mUser, this, updates, this);
        mBinding.setViewModel(mViewModel);
        mBinding.swiperefresh.setOnRefreshListener(() -> mViewModel.update());
        mBinding.scrollview.setOnScrollChangeListener(mScrollListener);
        TransitionUtils.addTransitionCallbackToBinding(mBinding);
        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mViewModel != null) {
            mViewModel.destroy();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mBinding.swiperefresh.setEnabled(true);
        if (!sIsUpdated) {
            refresh();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        stopRefreshing();
        mBinding.swiperefresh.setEnabled(false);
        if (mViewModel != null) {
            mViewModel.unsubscribeAll();
        }
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
        SharedPreferencesManager.storeUser(user);
        checkMatchUpdates(user);
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

    @Override
    public void onUserUpdateFailure() {
        stopRefreshing();
        // TODO show sync failed message
        sIsUpdated = false;
    }
}
