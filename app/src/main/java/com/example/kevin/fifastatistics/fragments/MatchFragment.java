package com.example.kevin.fifastatistics.fragments;

import android.content.Intent;
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
import com.example.kevin.fifastatistics.activities.MatchUpdateActivity;
import com.example.kevin.fifastatistics.databinding.FragmentMatchBinding;
import com.example.kevin.fifastatistics.managers.RetrofitErrorManager;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchProjection;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.viewmodels.fragment.MatchFragmentViewModel;

public class MatchFragment extends FifaBaseFragment implements MatchFragmentViewModel.OnMatchLoadedListener {

    private static final int CREATE_UPDATE_REQUEST_CODE = 4733;

    private Match mMatch;
    private MatchProjection mMatchProjection;
    private String mMatchId;
    private User mUser;
    private MatchFragmentViewModel mViewModel;

    public static MatchFragment newInstance(MatchProjection matchProjection, User user) {
        MatchFragment f = new MatchFragment();
        Bundle args = new Bundle();
        args.putSerializable(USER, user);
        args.putSerializable(MATCH_PROJECTION, matchProjection);
        f.setArguments(args);
        return f;
    }

    public static MatchFragment newInstance(String matchId, User user) {
        MatchFragment f = new MatchFragment();
        Bundle args = new Bundle();
        args.putSerializable(USER, user);
        args.putString(EVENT_ID, matchId);
        f.setArguments(args);
        return f;
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
        mMatchProjection = (MatchProjection) b.getSerializable(MATCH_PROJECTION);
        mMatchId = b.getString(EVENT_ID);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(USER, mUser);
        outState.putSerializable(MATCH, mMatch);
        outState.putSerializable(MATCH_PROJECTION, mMatchProjection);
        outState.putString(EVENT_ID, mMatchId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentMatchBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_match, container, false);
        mViewModel = new MatchFragmentViewModel(this, mMatchProjection, mUser, mMatchId, this);
        binding.setViewModel(mViewModel);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mViewModel.setMatch(mMatch);
        mViewModel.loadMatch();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mViewModel != null) {
            mViewModel.destroy();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_match, menu);
        MenuItem requestUpdate = menu.findItem(R.id.update);
        MenuItem viewUpdate = menu.findItem(R.id.view_pending_update);
        boolean isUpdatePending = mMatch != null && mMatch.getUpdateId() != null;
        viewUpdate.setVisible(isUpdatePending);
        requestUpdate.setVisible(mMatch != null && mUser.participatedIn(mMatchProjection) && !isUpdatePending);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update:
                launchMatchUpdater(MatchUpdateActivity.MatchEditType.UPDATE);
                return true;
            case R.id.view_pending_update:
                launchMatchUpdater(MatchUpdateActivity.MatchEditType.REVIEW);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void launchMatchUpdater(MatchUpdateActivity.MatchEditType type) {
        Intent intent = MatchUpdateActivity.getLaunchIntent(getContext(), type, null, mMatch);
        launchActivity(intent, CREATE_UPDATE_REQUEST_CODE, null);
    }

    @Override
    public void onMatchLoaded(Match match) {
        mMatch = match;
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onMatchLoadFailed(Throwable t) {
        RetrofitErrorManager.showToastForError(t, getActivity());
        getActivity().finish();
    }
}
