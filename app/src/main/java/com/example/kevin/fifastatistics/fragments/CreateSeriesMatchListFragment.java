package com.example.kevin.fifastatistics.fragments;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.activities.CreateMatchActivity;
import com.example.kevin.fifastatistics.activities.FifaBaseActivity;
import com.example.kevin.fifastatistics.activities.PickTeamActivity;
import com.example.kevin.fifastatistics.databinding.FragmentCreateSeriesMatchListBinding;
import com.example.kevin.fifastatistics.interfaces.OnMatchCreatedListener;
import com.example.kevin.fifastatistics.interfaces.OnSeriesCompletedListener;
import com.example.kevin.fifastatistics.interfaces.OnSeriesScoreUpdateListener;
import com.example.kevin.fifastatistics.interfaces.OnSeriesUpdatedListener;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.match.Series;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.viewmodels.CreateSeriesMatchListViewModel;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

public class CreateSeriesMatchListFragment extends FifaBaseFragment implements
        OnMatchCreatedListener {

    public static final int CREATE_SERIES_REQUEST_CODE = 5313;
    private static final String USER_WINS = "userWins";
    private static final String OPPONENT_WINS = "opponentWins";

    private User mUser;
    private Friend mOpponent;
    private FragmentCreateSeriesMatchListBinding mBinding;
    private CreateSeriesMatchListViewModel mListViewModel;
    private OnSeriesScoreUpdateListener mSeriesScoreUpdateListener;
    private OnSeriesUpdatedListener mSeriesUpdatedListener;
    private OnSeriesCompletedListener mSeriesCompletedListener;
    private Team mUserTeam;
    private Team mOpponentTeam;
    private TickerView mUserTicker;
    private TickerView mOpponentTicker;
    private int mUserScore;
    private int mOpponentScore;

    public static CreateSeriesMatchListFragment newInstance(User user, Friend opponent, Series series,
                                                            Team userTeam, Team opponentTeam) {
        CreateSeriesMatchListFragment f = new CreateSeriesMatchListFragment();
        Bundle args = new Bundle();
        args.putSerializable(USER, user);
        args.putSerializable(OPPONENT, opponent);
        args.putSerializable(SERIES, series);
        args.putSerializable(USER_TEAM, userTeam);
        args.putSerializable(OPPONENT_TEAM, opponentTeam);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSeriesUpdatedListener) {
            mSeriesUpdatedListener = (OnSeriesUpdatedListener) context;
        }
        if (context instanceof OnSeriesCompletedListener) {
            mSeriesCompletedListener = (OnSeriesCompletedListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mSeriesUpdatedListener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restoreInstance(savedInstanceState);
    }

    private void restoreInstance(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mUser = (User) savedInstanceState.getSerializable(USER);
            mOpponent = (Friend) savedInstanceState.getSerializable(OPPONENT);
            mUserTeam = (Team) savedInstanceState.getSerializable(USER_TEAM);
            mOpponentTeam = (Team) savedInstanceState.getSerializable(OPPONENT_TEAM);
            mUserScore = savedInstanceState.getInt(USER_WINS);
            mOpponentScore = savedInstanceState.getInt(OPPONENT_WINS);
        } else {
            Bundle args = getArguments();
            mUser = (User) args.getSerializable(USER);
            mOpponent = (Friend) args.getSerializable(OPPONENT);
            mUserTeam = (Team) args.getSerializable(USER_TEAM);
            mOpponentTeam = (Team) args.getSerializable(OPPONENT_TEAM);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(USER, mUser);
        outState.putSerializable(OPPONENT, mOpponent);
        outState.putSerializable(USER_TEAM, mListViewModel.getUserTeam());
        outState.putSerializable(OPPONENT_TEAM, mListViewModel.getOpponentTeam());
        outState.putInt(USER_WINS, mUserScore);
        outState.putInt(OPPONENT_WINS, mOpponentScore);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mListViewModel != null) {
            mListViewModel.destroy();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_series_match_list, container, false);
        initializeSeriesUpdateListener();
        Series series = (Series) getArguments().getSerializable(SERIES);
        mListViewModel = new CreateSeriesMatchListViewModel((FifaBaseActivity) getActivity(), mUser, mOpponent,
                mSeriesScoreUpdateListener, mSeriesCompletedListener, this,
                mSeriesUpdatedListener, series, mUserTeam, mOpponentTeam);
        mBinding.setListViewModel(mListViewModel);
        return mBinding.getRoot();
    }

    private void initializeSeriesUpdateListener() {
        mUserTicker = mBinding.createSeriesScoreSummary.userScoreTicker;
        mUserTicker.setCharacterList(TickerUtils.getDefaultNumberList());
        mUserTicker.setText(String.valueOf(mUserScore));
        mOpponentTicker = mBinding.createSeriesScoreSummary.opponentScoreTicker;
        mOpponentTicker.setCharacterList(TickerUtils.getDefaultNumberList());
        mOpponentTicker.setText(String.valueOf(mOpponentScore));
        mSeriesScoreUpdateListener = new OnSeriesScoreUpdateListener() {
            @Override
            public void onUserScoreUpdate(int oldScore, int newScore) {
                mUserScore = newScore;
                mUserTicker.setText(String.valueOf(newScore));
            }
            @Override
            public void onOpponentScoreUpdate(int oldScore, int newScore) {
                mOpponentScore = newScore;
                mOpponentTicker.setText(String.valueOf(newScore));
            }
            @Override
            public void onUserTeamUpdated(Team team) {
                if (mSeriesUpdatedListener != null) {
                    mSeriesUpdatedListener.onUserTeamUpdated(team);
                }
                mUserTeam = team;
            }

            @Override
            public void onOpponentTeamUpdated(Team team) {
                if (mSeriesUpdatedListener != null) {
                    mSeriesUpdatedListener.onOpponentTeamUpdated(team);
                }
                mOpponentTeam = team;
            }
        };
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListViewModel.setTeams(mUserTeam, mOpponentTeam);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_SERIES_REQUEST_CODE && resultCode == PickTeamActivity.RESULT_TEAM_PICKED) {
            Team team = (Team) data.getExtras().getSerializable(PickTeamActivity.EXTRA_TEAM);
            mListViewModel.onTeamSelected(team);
        } else if (requestCode == CREATE_SERIES_REQUEST_CODE && resultCode == CreateMatchFragment.RESULT_MATCH_CREATED) {
            Match match = (Match) data.getExtras().getSerializable(MATCH);
            onMatchCreated(match);
        }
    }

    @Override
    public void onMatchCreated(Match match) {
        mListViewModel.onMatchCreated(match);
    }

    public void createNewMatch() {
        mListViewModel.setMatchIndex(-1);
        Intent intent = CreateMatchActivity.getPartOfSeriesIntent(getContext(), mOpponent, null, mUserTeam, mOpponentTeam);
        startActivityForResult(intent, CreateSeriesMatchListFragment.CREATE_SERIES_REQUEST_CODE);
    }

    public boolean isSeriesStarted() {
        return !mListViewModel.getItems().isEmpty();
    }
}
