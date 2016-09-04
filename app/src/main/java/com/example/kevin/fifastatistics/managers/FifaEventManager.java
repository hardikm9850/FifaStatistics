package com.example.kevin.fifastatistics.managers;

import com.example.kevin.fifastatistics.activities.FifaActivity;
import com.example.kevin.fifastatistics.fragments.dialogs.SelectOpponentDialog;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * Manager for adding new matches and series.
 */
@RequiredArgsConstructor
public class FifaEventManager implements SelectOpponentDialog.SelectOpponentListener {

    private final FifaActivity mActivity;
    private final User mUser;
    private Flow mFlow;

    public static FifaEventManager newInstance(FifaActivity activity, User user) {
        return new FifaEventManager(activity, user);
    }

    /** Set the type of flow to a new match type */
    public void setMatchFlow() {
        mFlow = new MatchFlow();
    }

    /** Set the type of flow to a new series type */
    public void setSeriesFlow() {
        mFlow = new SeriesFlow();
    }

    /**
     * Start a new flow for when we do not yet know the opponent i.e., when starting from
     * MainActivity. be sure to either setMatchFlow() or setSeriesFlow() before calling.
     */
    public void startNewFlow() {
        mFlow.startNewFlow();
    }

    /**
     * Start a new flow for when we already know the opponent i.e., when starting from
     * PlayerActivity. be sure to either setMatchFlow() or setSeriesFlow() before calling.
     */
    public void startNewFlow(Friend opponent) {
        mFlow.startNewFlow(opponent);
    }

    @Override
    public void onOpponentClick(Friend friend) {
        mFlow.startNewFlow(friend);
    }

    /** Represents the type of flow (Match or Series) */
    private abstract class Flow {

        public void startNewFlow() {
            SelectOpponentDialog.newInstance(mUser, FifaEventManager.this).show(mActivity.getSupportFragmentManager());
        }

        public abstract void startNewFlow(Friend opponent);
    }

    private class SeriesFlow extends Flow {

        @Override
        public void startNewFlow(Friend opponent) {

        }
    }

    private class MatchFlow extends Flow {

        @Override
        public void startNewFlow(Friend opponent) {

        }
    }
}
