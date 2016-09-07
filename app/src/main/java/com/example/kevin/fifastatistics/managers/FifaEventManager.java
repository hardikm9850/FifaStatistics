package com.example.kevin.fifastatistics.managers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.example.kevin.fifastatistics.activities.FifaActivity;
import com.example.kevin.fifastatistics.fragments.AddMatchDialogFragment;
import com.example.kevin.fifastatistics.fragments.dialogs.SelectOpponentDialog;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;

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
    private abstract class Flow implements AddMatchDialogFragment.AddMatchDialogSaveListener {

        public void startNewFlow() {
            SelectOpponentDialog.newInstance(mUser, FifaEventManager.this).show(mActivity.getSupportFragmentManager());
        }

        public void showAddMatchFragment(FifaActivity parentActivity, Friend opponent) {
            FragmentTransaction t = parentActivity.getSupportFragmentManager().beginTransaction();
            t.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            Fragment fragment = AddMatchDialogFragment.newInstance(mUser, opponent, this, mActivity);
            t.add(android.R.id.content, fragment).addToBackStack(null).commit();
        }

        public abstract void startNewFlow(Friend opponent);
    }

    private class SeriesFlow extends Flow {

        @Override
        public void startNewFlow(Friend opponent) {
            Log.i("SERIES", "new series versus " + opponent.getName());
        }

        @Override
        public void onSave() {
            // TODO
        }
    }

    private class MatchFlow extends Flow {

        @Override
        public void startNewFlow(Friend opponent) {
            showAddMatchFragment(mActivity, opponent);
        }

        @Override
        public void onSave() {
            // TODO
        }
    }
}
