package com.example.kevin.fifastatistics.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ScrollView;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.models.databasemodels.match.Penalties;
import com.example.kevin.fifastatistics.models.databasemodels.user.Stats;
import com.example.kevin.fifastatistics.models.databasemodels.user.User.StatsPair;

public class AddMatchListLayout extends ScrollView {

    private AddMatchListItem mGoalsItem;
    private AddMatchListItem mShotsItem;
    private AddMatchListItem mShotsOnTargetItem;
    private AddMatchListItem mPossessionItem;
    private AddMatchListItem mTacklesItem;
    private AddMatchListItem mFoulsItem;
    private AddMatchListItem mYellowCardsItem;
    private AddMatchListItem mRedCardsItem;
    private AddMatchListItem mOffsidesItem;
    private AddMatchListItem mInjuriesItem;
    private AddMatchListItem mShotAccuracyItem;
    private AddMatchListItem mPassAccuracyItem;
    private AddMatchListItem mPenalties;

    public AddMatchListLayout(Context c, AttributeSet attributeSet) {
        super(c, attributeSet);

        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.add_match_list_layout, this);

        mGoalsItem = (AddMatchListItem) findViewById(R.id.goals);
        mGoalsItem.setTitle(getResources().getString(R.string.goals));

        mShotsItem = (AddMatchListItem) findViewById(R.id.shots);
        mShotsItem.setTitle(getResources().getString(R.string.shots));

        mShotsOnTargetItem = (AddMatchListItem) findViewById(R.id.shots_on_target);
        mShotsOnTargetItem.setTitle(getResources().getString(R.string.shots_on_target));

        mPossessionItem = (AddMatchListItem) findViewById(R.id.possession);
        mPossessionItem.setTitle(getResources().getString(R.string.possession_percent));

        mTacklesItem = (AddMatchListItem) findViewById(R.id.tackles);
        mTacklesItem.setTitle(getResources().getString(R.string.tackles));

        mFoulsItem = (AddMatchListItem) findViewById(R.id.fouls);
        mFoulsItem.setTitle(getResources().getString(R.string.fouls));

        mYellowCardsItem = (AddMatchListItem) findViewById(R.id.yellow_cards);
        mYellowCardsItem.setTitle(getResources().getString(R.string.yellow_cards));

        mRedCardsItem = (AddMatchListItem) findViewById(R.id.red_cards);
        mRedCardsItem.setTitle(getResources().getString(R.string.red_cards));

        mOffsidesItem = (AddMatchListItem) findViewById(R.id.offsides);
        mOffsidesItem.setTitle(getResources().getString(R.string.offsides));

        mInjuriesItem = (AddMatchListItem) findViewById(R.id.injuries);
        mInjuriesItem.setTitle(getResources().getString(R.string.injuries));

        mShotAccuracyItem = (AddMatchListItem) findViewById(R.id.shot_accuracy);
        mShotAccuracyItem.setTitle(getResources().getString(R.string.shot_accuracy_percent));

        mPassAccuracyItem = (AddMatchListItem) findViewById(R.id.pass_accuracy);
        mPassAccuracyItem.setTitle(getResources().getString(R.string.pass_accuracy_percent));

        mPenalties = (AddMatchListItem) findViewById(R.id.penalties);
        mPenalties.setTitle(getResources().getString(R.string.penalties));
    }

    public void setValues(StatsPair statsPair) {
        Stats leftStats = statsPair.getStatsFor();
        mGoalsItem.setLeftText(leftStats.getGoals());
        mShotsItem.setLeftText(leftStats.getShots());
        mShotsOnTargetItem.setLeftText(leftStats.getShotsOnTarget());
        mPossessionItem.setLeftText(leftStats.getPossession());
        mTacklesItem.setLeftText(leftStats.getTackles());
        mFoulsItem.setLeftText(leftStats.getFouls());
        mYellowCardsItem.setLeftText(leftStats.getYellowCards());
        mRedCardsItem.setLeftText(leftStats.getRedCards());
        mOffsidesItem.setLeftText(leftStats.getOffsides());
        mInjuriesItem.setLeftText(leftStats.getInjuries());
        mShotAccuracyItem.setLeftText(leftStats.getShotAccuracy());
        mPassAccuracyItem.setLeftText(leftStats.getPassAccuracy());

        Stats rightStats = statsPair.getStatsAgainst();
        mGoalsItem.setRightText(rightStats.getGoals());
        mShotsItem.setRightText(rightStats.getShots());
        mShotsOnTargetItem.setRightText(rightStats.getShotsOnTarget());
        mPossessionItem.setRightText(rightStats.getPossession());
        mTacklesItem.setRightText(rightStats.getTackles());
        mFoulsItem.setRightText(rightStats.getFouls());
        mYellowCardsItem.setRightText(rightStats.getYellowCards());
        mRedCardsItem.setRightText(rightStats.getRedCards());
        mOffsidesItem.setRightText(rightStats.getOffsides());
        mInjuriesItem.setRightText(rightStats.getInjuries());
        mShotAccuracyItem.setRightText(rightStats.getShotAccuracy());
        mPassAccuracyItem.setRightText(rightStats.getPassAccuracy());
    }

    /**
     * Return a StatsPair with the values from the text fields.
     * @throws NumberFormatException if any of the values aren't ints.
     */
    public StatsPair getValues() {
        Stats statsFor = new Stats();
        statsFor.setGoals(getLeftGoals());
        statsFor.setShots(getLeftShots());
        statsFor.setShotsOnTarget(getLeftShotsOnTarget());
        statsFor.setPossession(getLeftPossession());
        statsFor.setTackles(getLeftTackles());
        statsFor.setFouls(getLeftFouls());
        statsFor.setYellowCards(getLeftYellowCards());
        statsFor.setRedCards(getLeftRedCards());
        statsFor.setOffsides(getLeftOffsides());
        statsFor.setInjuries(getLeftInjuries());
        statsFor.setShotAccuracy(getLeftShotAccuracy());
        statsFor.setPassAccuracy(getLeftPassAccuracy());

        Stats statsAgainst = new Stats();
        statsAgainst.setGoals(getRightGoals());
        statsAgainst.setShots(getRightShots());
        statsAgainst.setShotsOnTarget(getRightShotsOnTarget());
        statsAgainst.setPossession(getRightPossession());
        statsAgainst.setTackles(getRightTackles());
        statsAgainst.setFouls(getRightFouls());
        statsAgainst.setYellowCards(getRightYellowCards());
        statsAgainst.setRedCards(getRightRedCards());
        statsAgainst.setOffsides(getRightOffsides());
        statsAgainst.setInjuries(getRightInjuries());
        statsAgainst.setShotAccuracy(getRightShotAccuracy());
        statsAgainst.setPassAccuracy(getRightPassAccuracy());

        return new StatsPair(statsFor, statsAgainst);
    }

    public Penalties getPenalties() {
        return isPenaltiesEmpty() ? null : new Penalties(getLeftPenalties(), getRightPenalties());
    }

    private boolean isPenaltiesEmpty() {
        return mPenalties.getLeftText().isEmpty() || mPenalties.getRightText().isEmpty();
    }

    public int getLeftGoals() {
        return Integer.valueOf(mGoalsItem.getLeftText());
    }

    public int getRightGoals() {
        return Integer.valueOf(mGoalsItem.getRightText());
    }

    public int getLeftShots() {
        return Integer.valueOf(mShotsItem.getLeftText());
    }

    public int getRightShots() {
        return Integer.valueOf(mShotsItem.getRightText());
    }

    public int getLeftShotsOnTarget() {
        return Integer.valueOf(mShotsOnTargetItem.getLeftText());
    }

    public int getRightShotsOnTarget() {
        return Integer.valueOf(mShotsOnTargetItem.getRightText());
    }

    public int getLeftPossession() {
        return Integer.valueOf(mPossessionItem.getLeftText());
    }

    public int getRightPossession() {
        return Integer.valueOf(mPossessionItem.getRightText());
    }

    public int getLeftTackles() {
        return Integer.valueOf(mTacklesItem.getLeftText());
    }

    public int getRightTackles() {
        return Integer.valueOf(mTacklesItem.getRightText());
    }

    public int getLeftFouls() {
        return Integer.valueOf(mFoulsItem.getLeftText());
    }

    public int getRightFouls() {
        return Integer.valueOf(mFoulsItem.getRightText());
    }

    public int getLeftYellowCards() {
        return Integer.valueOf(mYellowCardsItem.getLeftText());
    }

    public int getRightYellowCards() {
        return Integer.valueOf(mYellowCardsItem.getRightText());
    }

    public int getLeftRedCards() {
        return Integer.valueOf(mRedCardsItem.getLeftText());
    }

    public int getRightRedCards() {
        return Integer.valueOf(mRedCardsItem.getRightText());
    }

    public int getLeftOffsides() {
        return Integer.valueOf(mOffsidesItem.getLeftText());
    }

    public int getRightOffsides() {
        return Integer.valueOf(mOffsidesItem.getRightText());
    }

    public int getLeftInjuries() {
        return Integer.valueOf(mInjuriesItem.getLeftText());
    }

    public int getRightInjuries() {
        return Integer.valueOf(mInjuriesItem.getRightText());
    }

    public int getLeftShotAccuracy() {
        return Integer.valueOf(mShotAccuracyItem.getLeftText());
    }

    public int getRightShotAccuracy() {
        return Integer.valueOf(mShotAccuracyItem.getRightText());
    }

    public int getLeftPassAccuracy() {
        return Integer.valueOf(mPassAccuracyItem.getLeftText());
    }

    public int getRightPassAccuracy() {
        return Integer.valueOf(mPassAccuracyItem.getRightText());
    }

    public int getLeftPenalties() {
        return Integer.valueOf(mPenalties.getLeftText());
    }

    public int getRightPenalties() {
        return Integer.valueOf(mPenalties.getRightText());
    }
}
