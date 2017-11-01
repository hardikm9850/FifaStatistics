package com.example.kevin.fifastatistics.fragments;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.example.kevin.fifastatistics.activities.CameraActivity;
import com.example.kevin.fifastatistics.activities.PickTeamActivity;
import com.example.kevin.fifastatistics.databinding.FragmentCreateMatchBinding;
import com.example.kevin.fifastatistics.interfaces.ErrorHandler;
import com.example.kevin.fifastatistics.interfaces.OnMatchCreatedListener;
import com.example.kevin.fifastatistics.managers.StatsImageHandler;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.utils.BuildUtils;
import com.example.kevin.fifastatistics.utils.ByteHolder;
import com.example.kevin.fifastatistics.utils.MatchUtils;
import com.example.kevin.fifastatistics.utils.ToastUtils;
import com.example.kevin.fifastatistics.utils.TransitionUtils;
import com.example.kevin.fifastatistics.viewmodels.fragment.CreateMatchFragmentViewModel;

public class CreateMatchFragment extends FifaBaseFragment implements StatsImageHandler.StatsImageHandlerInteraction,
        CreateMatchFragmentViewModel.CreateMatchViewModelInteraction, OnMatchCreatedListener, ErrorHandler {

    public static final int RESULT_MATCH_CREATED = 701;
    public static final int CREATE_MATCH_REQUEST_CODE = 34733;

    private OnMatchUpdatedListener mMatchUpdateListener;
    private CreateMatchFragmentViewModel mViewModel;
    private ProgressDialog mMatchUploadingDialog;
    private Match mMatch;
    private User mUser;
    private Player mOpponent;
    private boolean mIsPartOfSeries;

    // Only used when part of series
    private Team team1;
    private Team team2;

    public static CreateMatchFragment newInstance(User user, Player opponent, Match match,
                                                  boolean isPartOfSeries, Team team1, Team team2) {
        CreateMatchFragment fragment = new CreateMatchFragment();
        Bundle args = new Bundle();
        args.putSerializable(USER, user);
        args.putSerializable(OPPONENT, opponent);
        args.putSerializable(MATCH, match);
        args.putSerializable(USER_TEAM, team1);
        args.putSerializable(OPPONENT_TEAM, team2);
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
        team1 = (Team) savedInstanceState.getSerializable(USER_TEAM);
        team2 = (Team) savedInstanceState.getSerializable(OPPONENT_TEAM);
        mIsPartOfSeries = savedInstanceState.getBoolean(SERIES);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initMatchUploadingDialog();
        FragmentCreateMatchBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_match, container, false);
        mViewModel = new CreateMatchFragmentViewModel(mUser, mOpponent, mMatch, binding.cardUpdateStatsLayout,
                this, this, savedInstanceState, mIsPartOfSeries, team1, team2);
        TransitionUtils.addTransitionCallbackToBinding(binding.cardUpdateStatsLayout);
        binding.setViewModel(mViewModel);
        return binding.getRoot();
    }

    private void initMatchUploadingDialog() {
        mMatchUploadingDialog = new ProgressDialog(getContext());
        mMatchUploadingDialog.setTitle(getString(R.string.uploading_match));
        mMatchUploadingDialog.setMessage(getString(R.string.please_wait));
        mMatchUploadingDialog.setCancelable(false);
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
        outState = mViewModel.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
        outState.putBoolean(SERIES, mIsPartOfSeries);
        outState.putSerializable(USER, mUser);
        outState.putSerializable(MATCH, mMatch);
        outState.putSerializable(OPPONENT, mOpponent);
        outState.putSerializable(USER_TEAM, team1);
        outState.putSerializable(OPPONENT_TEAM, team2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_MATCH_REQUEST_CODE && resultCode == CameraActivity.PICTURE_TAKEN_RESULT_CODE) {
            byte[] picture = ByteHolder.getData();
            String preprocessor = data.getStringExtra(CameraActivity.EXTRA_PREPROCESSOR);
            StatsImageHandler handler = new StatsImageHandler(getContext(), this);
            handler.processImage(picture, preprocessor);
        } else if (requestCode == CREATE_MATCH_REQUEST_CODE && resultCode == PickTeamActivity.RESULT_TEAM_PICKED) {
            Team team = (Team) data.getExtras().getSerializable(PickTeamActivity.EXTRA_TEAM);
            mViewModel.updateTeam(team);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        int menuRes = BuildUtils.isDebug() ? R.menu.menu_create_match_debug : R.menu.menu_create_match;
        inflater.inflate(menuRes, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_complete_match:
                createMatchIfValid();
                return true;
            case R.id.autofill:
                mViewModel.autofill();
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
            mViewModel.finalizeMatch();
            if (mIsPartOfSeries) {
                addMatchToSeries();
            } else {
                uploadMatch();
            }
        }
    }

    private void addMatchToSeries() {
        Bundle b = new Bundle();
        b.putSerializable(MATCH, mMatch);
        Intent intent = new Intent();
        intent.putExtras(b);
        getActivity().setResult(RESULT_MATCH_CREATED, intent);
        getActivity().finish();
    }

    private void uploadMatch() {
        mMatchUploadingDialog.show();
        MatchUtils.createMatch(mMatch, this, this);
    }

    @Override
    public void handleError(String message, Throwable throwable) {
        mMatchUploadingDialog.cancel();
        ToastUtils.showShortToast(getContext(), message);
    }

    @Override
    public void onMatchCreated(Match match) {
        mMatchUploadingDialog.cancel();
        ToastUtils.showShortToast(getContext(), R.string.match_upload_success);
        getActivity().finish();
    }

    @Override
    public void onStatsRetrieved(User.StatsPair statsPair) {
        mViewModel.setStats(statsPair);
    }

    @Override
    public void onStatsRetrievalError() {
        ToastUtils.showShortToast(getContext(), R.string.failed_to_parse);
    }

    @Override
    public void onMatchUpdated(Match match) {
        mMatch = match;
        if (mMatchUpdateListener != null) {
            mMatchUpdateListener.onMatchUpdate(match);
        }
    }

    public interface OnMatchUpdatedListener {
        void onMatchUpdate(Match match);
    }
}
