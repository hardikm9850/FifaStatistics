package com.example.kevin.fifastatistics.viewmodels.item;

import android.app.Activity;
import android.content.Intent;
import android.databinding.Bindable;
import android.support.annotation.ColorInt;
import android.view.View;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.activities.MatchActivity;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;

import java.util.Locale;

public class SeriesMatchItemViewModel extends ItemViewModel<Match> {

    private static final String RESULT_WIN = "W";
    private static final String RESULT_LOSS = "L";

    private String mScore;
    private String mLeftTeamCode;
    private String mRightTeamCode;
    private String mLeftTeamResult;
    private String mRightTeamResult;
    private int mLeftPenalties;
    private int mRightPenalties;
    private int mNumber;

    public SeriesMatchItemViewModel(ActivityLauncher launcher, Match match, User currentUser,
                                    Player seriesWinner, int matchNumber, boolean isLastItem) {
        super(match, launcher, isLastItem);
        mNumber = matchNumber;
        initVales(currentUser, seriesWinner);
    }

    private void initVales(User currentUser, Player seriesWinner) {
        if (currentUser.participatedIn(mItem)) {
            if (currentUser.didLose(mItem)) {
                setLoserLeftWinnerRight();
            } else {
                setWinnerLeftLoserRight();
            }
        } else {
            if (mItem.wonBy(seriesWinner)) {
                setWinnerLeftLoserRight();
            } else {
                setLoserLeftWinnerRight();
            }
        }
    }

    private void setLoserLeftWinnerRight() {
        mScore = mItem.getScoreLoser() + " - " + mItem.getScoreWinner();
        mLeftPenalties = mItem.getPenaltiesAgainst();
        mRightPenalties = mItem.getPenaltiesFor();
        mLeftTeamCode = mItem.getTeamLoser().getCode();
        mRightTeamCode = mItem.getTeamWinner().getCode();
        mLeftTeamResult = RESULT_LOSS;
        mRightTeamResult = RESULT_WIN;
    }

    private void setWinnerLeftLoserRight() {
        mScore = mItem.getScoreWinner() + " - " + mItem.getScoreLoser();
        mLeftPenalties = mItem.getPenaltiesFor();
        mRightPenalties = mItem.getPenaltiesAgainst();
        mLeftTeamCode = mItem.getTeamWinner().getCode();
        mRightTeamCode = mItem.getTeamLoser().getCode();
        mLeftTeamResult = RESULT_WIN;
        mRightTeamResult = RESULT_LOSS;
    }

    @Bindable
    public String getMatchNumber() {
        return "GM" + String.format(Locale.CANADA, "%02d", mNumber);
    }

    @Bindable
    public String getScore() {
        return mScore;
    }

    @Bindable
    public int getPenaltiesVisibility() {
        return (mItem.getPenalties() != null) ? View.VISIBLE : View.INVISIBLE;
    }

    public String getLeftPenalties() {
        return formatPenalties(mLeftPenalties);
    }

    public String getRightPenalties() {
        return formatPenalties(mRightPenalties);
    }

    private String formatPenalties(int penalties) {
        return "(" + penalties + ")";
    }

    public String getLeftTeamCode() {
        return mLeftTeamCode;
    }

    public String getRightTeamCode() {
        return mRightTeamCode;
    }

    public String getLeftTeamResult() {
        return mLeftTeamResult;
    }

    public String getRightTeamResult() {
        return mRightTeamResult;
    }

    @ColorInt
    public int getLeftResultColor() {
        return getColorForResult(mLeftTeamResult);
    }

    @ColorInt
    public int getRightResultColor() {
        return getColorForResult(mRightTeamResult);
    }

    @ColorInt
    private int getColorForResult(String result) {
        return result.equals(RESULT_WIN) ? getColor(R.color.green_winner) : getColor(R.color.red_loser);
    }

    public void onClick() {
        if (mItem != null && mLauncher != null) {
            Intent intent = MatchActivity.getLaunchIntent(mLauncher.getContext(), mItem.getId());
            mLauncher.launchActivity(intent, Activity.RESULT_OK, null);
        }
    }
}
