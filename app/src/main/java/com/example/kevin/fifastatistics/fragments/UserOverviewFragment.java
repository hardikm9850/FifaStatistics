package com.example.kevin.fifastatistics.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.databinding.FragmentUserOverviewBinding;
import com.example.kevin.fifastatistics.interfaces.OnBackPressedHandler;
import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.viewmodels.UserOverviewViewModel;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_overview, container, false);
        mViewModel = new UserOverviewViewModel(mUser, this);
        mBinding.setViewModel(mViewModel);
        mBinding.swiperefresh.setOnRefreshListener(() -> mViewModel.update());
        mBinding.scrollview.setOnScrollChangeListener(mScrollListener);
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
        mBinding.swiperefresh.setRefreshing(false);
        mBinding.swiperefresh.setEnabled(false);
        if (mViewModel != null) {
            mViewModel.unsubscribeAll();
        }
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
        mBinding.swiperefresh.setRefreshing(false);
        sIsUpdated = true;
        SharedPreferencesManager.storeUser(user);
    }

    @Override
    public void onUserUpdateFailure() {
        mBinding.swiperefresh.setRefreshing(false);
        sIsUpdated = false;
    }
}
