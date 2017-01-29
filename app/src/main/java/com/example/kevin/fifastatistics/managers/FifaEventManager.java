package com.example.kevin.fifastatistics.managers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.example.kevin.fifastatistics.activities.FifaBaseActivity;
import com.example.kevin.fifastatistics.fragments.AddMatchDialogFragment;
import com.example.kevin.fifastatistics.fragments.SelectOpponentDialogFragment;
import com.example.kevin.fifastatistics.interfaces.OnBackPressedHandler;
import com.example.kevin.fifastatistics.interfaces.OnMatchCreatedListener;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.network.CreateFailedException;
import com.example.kevin.fifastatistics.utils.IntentFactory;
import com.example.kevin.fifastatistics.utils.MatchUtils;
import com.example.kevin.fifastatistics.utils.ToastUtils;

import lombok.RequiredArgsConstructor;

/**
 * Manager for adding new matches and series.
 */
@RequiredArgsConstructor
public class FifaEventManager implements SelectOpponentDialogFragment.SelectOpponentListener, OnBackPressedHandler {

    private final FifaBaseActivity mActivity;
    private final User mUser;
    private Flow mFlow;

    public static FifaEventManager newInstance(FifaBaseActivity activity, User user) {
        return new FifaEventManager(activity, user);
    }

    public void setMatchFlow() {
        setMatchFlow(null);
    }

    /** Set the type of flow to a new match type */
    public void setMatchFlow(OnMatchCreatedListener listener) {
        mFlow = new MatchFlow(listener);
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

    @Override
    public boolean handleBackPress() {
        return (mFlow != null) && mFlow.handleBackPress();
    }

    /** Represents the type of flow (Match or Series) */
    private abstract class Flow implements OnBackPressedHandler {

        public void startNewFlow() {
            SelectOpponentDialogFragment.newInstance(mUser, FifaEventManager.this).show(mActivity.getSupportFragmentManager());
        }

        public abstract void startNewFlow(Friend opponent);
    }

    private class SeriesFlow extends Flow {

        @Override
        public void startNewFlow(Friend opponent) {
            Intent newSeriesIntent = IntentFactory.createNewSeriesActivityIntent(mActivity, opponent);
            mActivity.startActivity(newSeriesIntent);
        }

        @Override
        public boolean handleBackPress() {
            return false;
        }
    }

    private class MatchFlow extends Flow {

        private OnMatchCreatedListener mOnMatchCreatedListener;
        private AddMatchDialogFragment mAddMatchFragment;
        private Player mOpponent;

        public MatchFlow(OnMatchCreatedListener listener) {
            if (listener == null) {
                mOnMatchCreatedListener = (this::onSaveMatch);
            } else {
                mOnMatchCreatedListener = listener;
            }
        }

        @Override
        public void startNewFlow(Friend opponent) {
            mOpponent = opponent;
            mAddMatchFragment = showAddMatchFragment(mActivity, opponent);
        }

        private AddMatchDialogFragment showAddMatchFragment(FifaBaseActivity parentActivity, Friend opponent) {
            FragmentTransaction t = parentActivity.getSupportFragmentManager().beginTransaction();
            t.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            AddMatchDialogFragment fragment = AddMatchDialogFragment.newInstance(mUser, opponent, mOnMatchCreatedListener, mActivity);
            t.add(android.R.id.content, fragment).addToBackStack(null).commit();

            return fragment;
        }

        private void onSaveMatch(Match match) {
            ProgressDialog d = new ProgressDialog(mActivity);
            d.setTitle("Uploading match");
            d.setMessage("Please wait...");
            d.setCancelable(false);
            try {
                d.show();
                MatchUtils.createMatch(match).subscribe(m -> {
                    d.cancel();
                    NotificationSender.addMatch(mUser, mOpponent.getRegistrationToken(), match)
                            .subscribe(response -> {
                                if (!response.isSuccessful()) {
                                    Log.e("NOTIFICATION", "failed to send add match notification");
                                    // TODO add resend notification to tasks
                                } else {
                                    Log.e("NOTIFICATION", "sending successful");
                                }
                            });
                    ToastUtils.showShortToast(mActivity, "Match created successfully");
                    mAddMatchFragment.dismiss();
                    RetrievalManager.syncCurrentUserWithServer();
                });
            } catch (CreateFailedException cfe) {
                d.cancel();
                ToastUtils.showShortToast(mActivity, "Failed to create match: " + cfe.getErrorCode().getMessage());
                // TODO save match for retry
            }
        }

        @Override
        public boolean handleBackPress() {
            return (mAddMatchFragment != null) && (mAddMatchFragment.isVisible()) &&
                    mAddMatchFragment.handleBackPress();
        }
    }
}
