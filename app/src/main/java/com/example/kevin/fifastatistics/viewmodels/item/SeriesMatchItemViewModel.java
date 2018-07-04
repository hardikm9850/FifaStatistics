package com.example.kevin.fifastatistics.viewmodels.item;

import android.app.Activity;
import android.content.Intent;
import android.databinding.Bindable;

import com.example.kevin.fifastatistics.activities.MatchActivity;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;

import java.util.Locale;

public class SeriesMatchItemViewModel extends ItemViewModel<Match> {

    private Match mMatch;
    private String mScore;
    private String mLeftTeamCode;
    private String mRightTeamCode;
    private int mLeftPenalties;
    private int mRightPenalties;
    private int mNumber;

    public SeriesMatchItemViewModel(ActivityLauncher launcher, Match match, User currentUser, int matchNumber, boolean isLastItem) {
        super(match, launcher, isLastItem);
        mMatch = match;
        mNumber = matchNumber;
        initVales(currentUser);
    }

    private void initVales(User currentUser) {
        if (currentUser.didLose(mMatch)) {
            mScore = mMatch.getScoreLoser() + " - " + mMatch.getScoreWinner();
            mLeftPenalties = mMatch.getPenaltiesAgainst();
            mRightPenalties = mMatch.getPenaltiesFor();
            mLeftTeamCode = mMatch.getTeamLoser().getCode();
            mRightTeamCode = mMatch.getTeamWinner().getCode();
            //TODO W, L
        } else {
            mScore = mMatch.getScoreWinner() + " - " + mMatch.getScoreLoser();
            mLeftPenalties = mMatch.getPenaltiesFor();
            mRightPenalties = mMatch.getPenaltiesAgainst();
            mLeftTeamCode = mMatch.getTeamWinner().getCode();
            mRightTeamCode = mMatch.getTeamLoser().getCode();
        }
    }

    @Bindable
    public String getMatchNumber() {
        return String.format(Locale.CANADA, "%02d", mNumber);
    }

    @Bindable
    public String getScore() {
        return mScore;
    }

    @Bindable
    public int getPenaltiesVisibility() {
        return visibleIf(mMatch.getPenalties() != null);
    }

    public String getLeftPenalties() {
        return formatPenalties(mLeftPenalties);
    }

    public String getRightPenalties() {
        return formatPenalties(mRightPenalties);
    }

    public String getLeftTeamCode() {
        return mLeftTeamCode;
    }

    public String getRightTeamCode() {
        return mRightTeamCode;
    }

    private String formatPenalties(int penalties) {
        return "(" + mRightPenalties + ")";
    }

    public void onClick() {
        if (mMatch != null && mLauncher != null) {
            Intent intent = MatchActivity.getLaunchIntent(mLauncher.getContext(), mMatch.getId());
            mLauncher.launchActivity(intent, Activity.RESULT_OK, null);
        }
    }
}
