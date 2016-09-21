package com.example.kevin.fifastatistics.managers;

import android.app.ProgressDialog;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.example.kevin.fifastatistics.activities.FifaActivity;
import com.example.kevin.fifastatistics.fragments.AddMatchDialogFragment;
import com.example.kevin.fifastatistics.fragments.SelectOpponentDialogFragment;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.network.CreateFailedException;
import com.example.kevin.fifastatistics.utils.MatchUtils;
import com.example.kevin.fifastatistics.utils.ToastUtils;
import com.example.kevin.fifastatistics.utils.UserUtils;

import lombok.RequiredArgsConstructor;

/**
 * Manager for adding new matches and series.
 */
@RequiredArgsConstructor
public class FifaEventManager implements SelectOpponentDialogFragment.SelectOpponentListener {

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
            SelectOpponentDialogFragment.newInstance(mUser, FifaEventManager.this).show(mActivity.getSupportFragmentManager());
        }

        public DialogFragment showAddMatchFragment(FifaActivity parentActivity, Friend opponent) {
            FragmentTransaction t = parentActivity.getSupportFragmentManager().beginTransaction();
            t.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            DialogFragment fragment = AddMatchDialogFragment.newInstance(mUser, opponent, this, mActivity);
            t.add(android.R.id.content, fragment).addToBackStack(null).commit();

            return fragment;
        }

        public abstract void startNewFlow(Friend opponent);
    }

    private class SeriesFlow extends Flow {

        @Override
        public void startNewFlow(Friend opponent) {
            Log.i("SERIES", "new series versus " + opponent.getName());
        }

        @Override
        public void onSaveMatch(Match match) {
            // TODO
        }
    }

    private class MatchFlow extends Flow {

        private DialogFragment mAddMatchFragment;
        private Player mOpponent;

        @Override
        public void startNewFlow(Friend opponent) {
            mOpponent = opponent;
            mAddMatchFragment = showAddMatchFragment(mActivity, opponent);
        }

        @Override
        public void onSaveMatch(Match match) {
            ProgressDialog d = new ProgressDialog(mActivity);
            d.setTitle("Uploading match");
            d.setMessage("Please wait...");
            d.setCancelable(false);
            try {
                d.show();
                MatchUtils.createMatch(match).subscribe(m -> {
                    d.cancel();
                    mUser.addMatch(match);
                    UserUtils.updateUser(mUser).subscribe();

                    NotificationSender.addMatch(mUser, mOpponent.getRegistrationToken(), match)
                            .subscribe(response -> {
                                if (!response.isSuccessful()) {
                                    Log.e("NOTIFICATION", "failed to send add match notification");
                                } else {
                                    Log.e("NOTIFICATION", "sending successful");
                                }
                            });
                    ToastUtils.showShortToast(mActivity, "Match created successfully");
                    mAddMatchFragment.dismiss();
                });
            } catch (CreateFailedException cfe) {
                d.cancel();
                ToastUtils.showShortToast(mActivity, "Failed to create match: " + cfe.getErrorCode().getMessage());
                // TODO save match for retry
            }
        }
    }
}
