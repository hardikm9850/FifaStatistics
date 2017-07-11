package com.example.kevin.fifastatistics.fragments;

import android.databinding.DataBindingUtil;
import android.databinding.OnRebindCallback;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.databinding.FragmentMatchUpdateBinding;
import com.example.kevin.fifastatistics.interfaces.OnBackPressedHandler;
import com.example.kevin.fifastatistics.managers.RetrofitErrorManager;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchUpdate;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.viewmodels.MatchUpdateFragmentViewModel;

public class MatchUpdateFragment extends FifaBaseFragment implements OnBackPressedHandler,
        MatchUpdateFragmentViewModel.MatchUpdateInteraction {

    private static final String ARG_IS_RESPONSE = "isResponse";

    private Match mMatch;
    private MatchUpdate mUpdate;
    private User mUser;
    private MatchUpdateFragmentViewModel mViewModel;
    private boolean mIsResponse;

    public static MatchUpdateFragment newInstance(@Nullable MatchUpdate update, User user, @Nullable Match match) {
        Bundle args = new Bundle();
        MatchUpdateFragment fragment = new MatchUpdateFragment();
        args.putSerializable(MATCH_UPDATE, update);
        args.putSerializable(MATCH, match);
        args.putSerializable(USER, user);
        args.putBoolean(ARG_IS_RESPONSE, update != null || (match != null && match.getUpdateId() != null));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restoreInstance(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void restoreInstance(Bundle savedInstanceState) {
        Bundle b = savedInstanceState == null ? getArguments() : savedInstanceState;
        mUser = (User) b.getSerializable(USER);
        mMatch = (Match) b.getSerializable(MATCH);
        mUpdate = (MatchUpdate) b.getSerializable(MATCH_UPDATE);
        mIsResponse = b.getBoolean(ARG_IS_RESPONSE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(USER, mUser);
        outState.putSerializable(MATCH, mMatch);
        outState.putSerializable(MATCH_UPDATE, mUpdate);
        outState.putBoolean(ARG_IS_RESPONSE, mIsResponse);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentMatchUpdateBinding b = DataBindingUtil.inflate(inflater, R.layout.fragment_match_update, container, false);
        mViewModel = new MatchUpdateFragmentViewModel(mMatch, mUpdate, mUser, this, b);
        addTransitionCallbackToBinding(b);
        b.setViewModel(mViewModel);
        return b.getRoot();
    }

    private void addTransitionCallbackToBinding(FragmentMatchUpdateBinding binding) {
        binding.addOnRebindCallback(new OnRebindCallback<FragmentMatchUpdateBinding>() {
            @Override
            public boolean onPreBind(FragmentMatchUpdateBinding binding) {
                TransitionManager.beginDelayedTransition((ViewGroup)binding.getRoot());
                return super.onPreBind(binding);
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mViewModel.load();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mViewModel != null) {
            mViewModel.destroy();
        }
    }

    @Override
    public boolean handleBackPress() {
        // if update changed, show dialog
        return false;
    }

    @Override
    public void onUpdateLoaded() {
        // slide up footer button
        // set things visible
    }

    @Override
    public void onUpdateLoadFailed(Throwable e) {
        RetrofitErrorManager.showToastForError(e, getActivity());
    }
}
