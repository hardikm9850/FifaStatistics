package com.example.kevin.fifastatistics.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.activities.FifaBaseActivity;
import com.example.kevin.fifastatistics.databinding.FragmentCreateSeriesMatchListBinding;
import com.example.kevin.fifastatistics.interfaces.OnMatchCreatedListener;
import com.example.kevin.fifastatistics.interfaces.OnSeriesCompletedListener;
import com.example.kevin.fifastatistics.interfaces.OnSeriesScoreUpdateListener;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.viewmodels.CreateSeriesMatchListViewModel;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

public class CreateSeriesMatchListFragment extends FifaBaseFragment implements OnMatchCreatedListener {

    private User mUser;
    private Friend mOpponent;
    private CreateSeriesMatchListViewModel mListViewModel;
    private OnSeriesScoreUpdateListener mSeriesUpdateListener;
    private OnSeriesCompletedListener mSeriesCompletedListener;
    private TickerView mUserTicker;
    private TickerView mOpponentTicker;

    public CreateSeriesMatchListFragment() {}

    public static CreateSeriesMatchListFragment newInstance(User user, Friend opponent, OnSeriesCompletedListener seriesCompletedListener) {
        CreateSeriesMatchListFragment f = new CreateSeriesMatchListFragment();
        f.mUser = user;
        f.mOpponent = opponent;
        f.mSeriesCompletedListener = seriesCompletedListener;
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListViewModel = new CreateSeriesMatchListViewModel((FifaBaseActivity) getActivity(), mUser, mOpponent, mSeriesUpdateListener, mSeriesCompletedListener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentCreateSeriesMatchListBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_series_match_list, container, false);
        initializeSeriesUpdateListener(binding.getRoot());
        mListViewModel.setOnSeriesScoreUpdateListener(mSeriesUpdateListener);
        binding.setListViewModel(mListViewModel);
        return binding.getRoot();
    }

    private void initializeSeriesUpdateListener(View root) {
        mUserTicker = (TickerView) root.findViewById(R.id.user_score_ticker);
        mUserTicker.setCharacterList(TickerUtils.getDefaultNumberList());
        mUserTicker.setText("0");
        mOpponentTicker = (TickerView) root.findViewById(R.id.opponent_score_ticker);
        mOpponentTicker.setCharacterList(TickerUtils.getDefaultNumberList());
        mOpponentTicker.setText("0");
        mSeriesUpdateListener = new OnSeriesScoreUpdateListener() {
            @Override
            public void onUserScoreUpdate(int oldScore, int newScore) {
                mUserTicker.setText(String.valueOf(newScore));
            }

            @Override
            public void onOpponentScoreUpdate(int oldScore, int newScore) {
                mOpponentTicker.setText(String.valueOf(newScore));
            }
        };
    }

    @Override
    public void onMatchCreated(Match match) {
        mListViewModel.add(match);
    }

    public boolean isSeriesStarted() {
        return !mListViewModel.getItems().isEmpty();
    }
}
