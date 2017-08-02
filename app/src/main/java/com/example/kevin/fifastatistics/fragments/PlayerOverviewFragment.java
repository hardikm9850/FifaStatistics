package com.example.kevin.fifastatistics.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.databinding.FragmentPlayerOverviewBinding;
import com.example.kevin.fifastatistics.interfaces.OnBackPressedHandler;
import com.example.kevin.fifastatistics.managers.RetrofitErrorManager;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.utils.TransitionUtils;
import com.example.kevin.fifastatistics.viewmodels.PlayerOverviewFragmentViewModel;
import com.example.kevin.fifastatistics.viewmodels.UserOverviewViewModel;

public class PlayerOverviewFragment extends FifaBaseFragment implements OnBackPressedHandler,
        PlayerOverviewFragmentViewModel.OnPlayerLoadedListener {

    private static final String ARG_USER_ID = "id";

    private String mUserId;
    private FragmentPlayerOverviewBinding mBinding;
    private PlayerOverviewFragmentViewModel mFragmentViewModel;
    private View.OnScrollChangeListener mScrollListener;

    public static PlayerOverviewFragment newInstance(String userId) {
        PlayerOverviewFragment fragment = new PlayerOverviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

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
        mUserId = getArguments().getString(ARG_USER_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_player_overview, container, false);
        mFragmentViewModel = new PlayerOverviewFragmentViewModel(this, mUserId);
        mBinding.setProgressViewModel(mFragmentViewModel);
        mBinding.nestedScrollView.setOnScrollChangeListener(mScrollListener);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mFragmentViewModel.loadPlayer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mFragmentViewModel != null) {
            mFragmentViewModel.destroy();
        }
    }

    @Override
    public void onPlayerLoaded(User player) {
        UserOverviewViewModel viewModel = new UserOverviewViewModel(player);
        mBinding.setViewModel(viewModel);
    }

    @Override
    public void onPlayerLoadFailed(Throwable t) {
        RetrofitErrorManager.showToastForError(t, getActivity());
        getActivity().finish();
    }

    @Override
    public boolean handleBackPress() {
        return false;
    }
}
