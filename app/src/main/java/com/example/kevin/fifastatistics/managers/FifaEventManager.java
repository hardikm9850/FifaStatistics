package com.example.kevin.fifastatistics.managers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.activities.CreateMatchActivity;
import com.example.kevin.fifastatistics.fragments.CreateSeriesMatchListFragment;
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

    public void setMatchFlow(boolean isPartOfSeries) {
        setMatchFlow(null, isPartOfSeries);
    }

    /** Set the type of flow to a new match type */
    public void setMatchFlow(OnMatchCreatedListener listener, boolean isPartOfSeries) {
        mFlow = new MatchFlow(listener, isPartOfSeries);
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

        private OnMatchCreatedListener mListener;
        private ProgressDialog mMatchUploadingDialog;
        private boolean mIsPartOfSeries;
        private boolean mIsSaving;

        MatchFlow(OnMatchCreatedListener listener, boolean isPartOfSeries) {
            initProgressDialog();
            mListener = listener;
            mIsPartOfSeries = isPartOfSeries;
        }

        private void initProgressDialog() {
            mMatchUploadingDialog = new ProgressDialog(mActivity);
            mMatchUploadingDialog.setTitle(mActivity.getString(R.string.uploading_match));
            mMatchUploadingDialog.setMessage(mActivity.getString(R.string.please_wait));
            mMatchUploadingDialog.setCancelable(false);
        }

        @Override
        public void startNewFlow(Player opponent) {
//            mAddMatchFragment = showAddMatchFragment(mActivity, opponent);
            Intent intent = mIsPartOfSeries ?
                    CreateMatchActivity.getPartOfSeriesIntent(mActivity, opponent, null, null,
                            null) :
                    CreateMatchActivity.getIndividualMatchIntent(mActivity, opponent, null);
            mActivity.startActivityForResult(intent, CreateSeriesMatchListFragment.CREATE_SERIES_REQUEST_CODE);
        }

        private void onSaveMatch(Match match) {
            mMatchUploadingDialog.show();
            MatchUtils.createMatch(match, this, this);
        }

        @Override
        public void onMatchCreated(Match match) {
            if (!mIsPartOfSeries && !mIsSaving) {
                mIsSaving = true;
                onSaveMatch(match);
            } else {
                mMatchUploadingDialog.cancel();
                ToastUtils.showShortToast(mActivity, "Match created successfully");
                RetrievalManager.syncCurrentUserWithServer();
            }
        }

        @Override
        public void handleError(String message, Throwable throwable) {
            mIsSaving = false;
            mMatchUploadingDialog.dismiss();
            ToastUtils.showShortToast(mActivity, message);
            // TODO save match for retry
        }

        @Override
        public boolean handleBackPress() {
            return false;
        }
    }
}
