package com.example.kevin.fifastatistics.managers;

import com.example.kevin.fifastatistics.models.databasemodels.match.TeamEvent;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.viewmodels.card.StatsCardViewModel;

public class StatsPresenter {

    private final User.StatsPair mStats;
    private final TeamEvent mEvent;
    private final String mUsername;
    private String mLeftHeader;
    private String mRightHeader;
    private boolean mDidSwap;

    public StatsPresenter(User.StatsPair stats, TeamEvent event, String username) {
        mStats = stats;
        mEvent = event;
        mUsername = username;
        init();
    }

    private void init() {
        if (mEvent != null) {
            final String winnerName = mEvent.getWinner().getName();
            final String loserName = mEvent.getLoser().getName();
            if (winnerName.equals(mUsername)) {
                mLeftHeader = StatsCardViewModel.MY_STATS_LEFT_HEADER;
                mRightHeader = mEvent.getLoser().getFirstName();
            } else if (loserName.equals(mUsername)) {
                mStats.swap();
                mDidSwap = true;
                mLeftHeader = StatsCardViewModel.MY_STATS_LEFT_HEADER;
                mRightHeader = mEvent.getWinner().getFirstName();
            } else {
                mLeftHeader = mEvent.getWinner().getFirstName();
                mRightHeader = mEvent.getLoser().getFirstName();
            }
        }
    }

    public TeamEvent getEvent() {
        return mEvent;
    }

    public String getLeftHeader() {
        return mLeftHeader;
    }

    public String getRightHeader() {
        return mRightHeader;
    }

    public String getLeftColor() {
        return mDidSwap ? mEvent.getTeamLoser().getColor() : mEvent.getTeamWinner().getColor();
    }

    public String getRightColor() {
        return mDidSwap ? mEvent.getTeamWinner().getColor() : mEvent.getTeamLoser().getColor();
    }

    public User.StatsPair getStats() {
        return mStats;
    }
}
