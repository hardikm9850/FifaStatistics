package com.example.kevin.fifastatistics.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.activities.FifaActivity;
import com.example.kevin.fifastatistics.fragments.AddMatchDialogFragment;
import com.example.kevin.fifastatistics.interfaces.OnMatchCreatedListener;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.utils.ResourceUtils;

public class CreateSeriesListItemViewModel extends BaseObservable {

    private Match mMatch;
    private User mUser;
    private Friend mOpponent;
    private FifaActivity mActivity;
    private AddMatchDialogFragment mDialogFragment;
    private OnMatchCreatedListener mOnMatchCreatedListener;
    private int mMatchNumber;

    public CreateSeriesListItemViewModel(FifaActivity activity, Match match, User currentUser, Friend opponent, int matchNumber) {
        mMatch = match;
        mActivity = activity;
        mUser = currentUser;
        mOpponent = opponent;
        mMatchNumber = matchNumber;
        mOnMatchCreatedListener = initializeMatchUpdateListener();
    }

    private OnMatchCreatedListener initializeMatchUpdateListener() {
        return (match -> {
            setMatch(match);
            if (mDialogFragment != null) {
                mDialogFragment.dismiss();
            }
        });
    }

    public void onItemClicked() {
        FragmentTransaction t = mActivity.getSupportFragmentManager().beginTransaction();
        t.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        mDialogFragment = AddMatchDialogFragment.newInstance(mUser, mOpponent, mOnMatchCreatedListener, mActivity);
        mDialogFragment.setMatch(mMatch);
        t.add(android.R.id.content, mDialogFragment).addToBackStack(null).commit();
    }

    public void setMatch(Match match) {
        mMatch = match;
        notifyChange();
    }

    @Bindable
    public String getMatchNumber() {
        return "0" + mMatchNumber;
    }

    @Bindable
    public int getIsPenaltiesVisible() {
        return (mMatch.getPenalties() != null) ? View.VISIBLE : View.GONE;
    }

    @Bindable
    public String getPenaltiesText() {
        return "(" + mMatch.getPenaltiesScore(mUser.getId()) + ")";
    }

    @Bindable
    public String getMatchResult() {
        return ((mMatch.didWin(mUser)) ? getResultPrefix(R.string.win) : getResultPrefix(R.string.loss))
                + " " + getMatchScore();
    }

    private String getResultPrefix(int id) {
        return ResourceUtils.getStringFromResourceId(id).substring(0, 1);
    }

    private String getMatchScore() {
        return mMatch.getMatchScore(mUser.getId());
    }
}
