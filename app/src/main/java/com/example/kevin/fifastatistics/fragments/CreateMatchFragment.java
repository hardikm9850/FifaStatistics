package com.example.kevin.fifastatistics.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.databinding.FragmentCreateMatchBinding;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.utils.TransitionUtils;
import com.example.kevin.fifastatistics.viewmodels.fragment.CreateMatchFragmentViewModel;

public class CreateMatchFragment extends FifaBaseFragment implements CreateMatchFragmentViewModel.CreateMatchViewModelInteraction {

    private OnMatchUpdatedListener mMatchUpdateListener;
    private FragmentCreateMatchBinding mBinding;
    private CreateMatchFragmentViewModel mViewModel;
    private Match mMatch;
    private User mUser;
    private Player mOpponent;
    private boolean mIsPartOfSeries;

    public static CreateMatchFragment newInstance(User user, Player opponent, Match match, boolean isPartOfSeries) {
        CreateMatchFragment fragment = new CreateMatchFragment();
        Bundle args = new Bundle();
        args.putSerializable(USER, user);
        args.putSerializable(OPPONENT, opponent);
        args.putSerializable(MATCH, match);
        args.putBoolean(SERIES, isPartOfSeries);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMatchUpdatedListener) {
            mMatchUpdateListener = (OnMatchUpdatedListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mMatchUpdateListener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restore(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void restore(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            savedInstanceState = getArguments();
        }
        mMatch = (Match) savedInstanceState.getSerializable(MATCH);
        mUser = (User) savedInstanceState.getSerializable(USER);
        mOpponent = (Player) savedInstanceState.getSerializable(OPPONENT);
        mIsPartOfSeries = savedInstanceState.getBoolean(SERIES);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_match, container, false);
        mViewModel = new CreateMatchFragmentViewModel(mUser, mMatch, mBinding.cardUpdateStatsLayout, this);
        TransitionUtils.addTransitionCallbackToBinding(mBinding.cardUpdateStatsLayout);
        mBinding.setViewModel(mViewModel);
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SERIES, mIsPartOfSeries);
        outState.putSerializable(USER, mUser);
        outState.putSerializable(MATCH, mMatch);
        outState.putSerializable(OPPONENT, mOpponent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_create_match, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_complete_match:
                createMatchIfValid();
                return true;
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createMatchIfValid() {
        if (mViewModel.isValid()) {
            // TODO
        }
    }

    @Override
    public void onMatchUpdated(Match match) {
        if (mMatchUpdateListener != null) {
            mMatchUpdateListener.onMatchUpdate(match);
        }
    }

    public interface OnMatchUpdatedListener {
        void onMatchUpdate(Match match);
    }
}
