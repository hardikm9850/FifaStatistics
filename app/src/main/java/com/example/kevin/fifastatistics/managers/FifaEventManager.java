package com.example.kevin.fifastatistics.managers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.fragments.AddMatchDialogFragment;
import com.example.kevin.fifastatistics.fragments.SelectOpponentDialogFragment;
import com.example.kevin.fifastatistics.interfaces.ErrorHandler;
import com.example.kevin.fifastatistics.interfaces.OnBackPressedHandler;
import com.example.kevin.fifastatistics.interfaces.OnMatchCreatedListener;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.utils.IntentFactory;
import com.example.kevin.fifastatistics.utils.MatchUtils;
import com.example.kevin.fifastatistics.utils.ToastUtils;

import lombok.RequiredArgsConstructor;

/**
 * Manager for adding new matches and series.
 */
@RequiredArgsConstructor
public class FifaEventManager implements SelectOpponentDialogFragment.SelectOpponentListener,
        OnBackPressedHandler, OnMatchCreatedListener {

    private final FragmentActivity mActivity;
    private final User mUser;
    private Flow mFlow;

    public static FifaEventManager newInstance(FragmentActivity activity, User user) {
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
    public void onOpponentClick(Player friend) {
        mFlow.startNewFlow(friend);
    }

    @Override
    public boolean handleBackPress() {
        return (mFlow != null) && mFlow.handleBackPress();
    }

    @Override
    public void onMatchCreated(Match match) {
        if (mFlow instanceof OnMatchCreatedListener) {
            ((OnMatchCreatedListener) mFlow).onMatchCreated(match);
        }
    }

    /** Represents the type of flow (Match or Series) */
    private abstract class Flow implements OnBackPressedHandler {

        void startNewFlow() {
            SelectOpponentDialogFragment.newInstance(FifaEventManager.this).show(mActivity.getSupportFragmentManager());
        }

        public abstract void startNewFlow(Player opponent);
    }

    private class SeriesFlow extends Flow {

        @Override
        public void startNewFlow(Player opponent) {
            Intent newSeriesIntent = IntentFactory.createNewSeriesActivityIntent(mActivity, opponent);
            mActivity.startActivity(newSeriesIntent);
        }

        @Override
        public boolean handleBackPress() {
            return false;
        }
    }

    private class MatchFlow extends Flow implements ErrorHandler, OnMatchCreatedListener {

        private AddMatchDialogFragment mAddMatchFragment;
        private Player mOpponent;
        private ProgressDialog mMatchUploadingDialog;

        MatchFlow(OnMatchCreatedListener listener) {
            initProgressDialog();
        }

        private void initProgressDialog() {
            mMatchUploadingDialog = new ProgressDialog(mActivity);
            mMatchUploadingDialog.setTitle(mActivity.getString(R.string.uploading_match));
            mMatchUploadingDialog.setMessage(mActivity.getString(R.string.please_wait));
            mMatchUploadingDialog.setCancelable(false);
        }

        @Override
        public void startNewFlow(Player opponent) {
            mOpponent = opponent;
            mAddMatchFragment = showAddMatchFragment(mActivity, opponent);
        }

        private AddMatchDialogFragment showAddMatchFragment(FragmentActivity parentActivity, Player opponent) {
            FragmentTransaction t = parentActivity.getSupportFragmentManager().beginTransaction();
            t.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            AddMatchDialogFragment fragment = AddMatchDialogFragment.newInstance(mUser, opponent);
            t.add(android.R.id.content, fragment).addToBackStack(null).commit();

            return fragment;
        }

        private void onSaveMatch(Match match) {
            mMatchUploadingDialog.show();
            MatchUtils.createMatch(match, this, this);
        }

        @Override
        public void onMatchCreated(Match match) {
            mMatchUploadingDialog.cancel();
            ToastUtils.showShortToast(mActivity, "Match created successfully");
            mAddMatchFragment.dismiss();
            RetrievalManager.syncCurrentUserWithServer();
        }

        @Override
        public void handleError(String message, Throwable throwable) {
            mMatchUploadingDialog.dismiss();
            ToastUtils.showShortToast(mActivity, message);
            // TODO save match for retry
        }

        @Override
        public boolean handleBackPress() {
            return (mAddMatchFragment != null) && (mAddMatchFragment.isVisible()) &&
                    mAddMatchFragment.handleBackPress();
        }
    }
}
