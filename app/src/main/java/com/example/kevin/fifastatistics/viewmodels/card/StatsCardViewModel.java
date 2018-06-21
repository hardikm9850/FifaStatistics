package com.example.kevin.fifastatistics.viewmodels.card;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;

import com.example.kevin.fifastatistics.FifaApplication;
import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.event.ColorChangeEvent;
import com.example.kevin.fifastatistics.event.EventBus;
import com.example.kevin.fifastatistics.listeners.SimpleOnTabSelectedListener;
import com.example.kevin.fifastatistics.managers.StatsPresenter;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.match.Series;
import com.example.kevin.fifastatistics.models.databasemodels.user.Stats;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.viewmodels.FifaBaseViewModel;
import com.example.kevin.fifastatistics.viewmodels.item.StatItemViewModel;

import java.util.ArrayList;
import java.util.List;

public class StatsCardViewModel extends FifaBaseViewModel {

    public static final String MY_STATS_LEFT_HEADER;
    private static final String PLAYER_STATS_RIGHT_HEADER;
    private static final String[] PLAYER_TAB_TITLES;
    private static final String[] SERIES_TAB_TITLES;
    private static final int DEFAULT_RIGHT_COLOR;

    static {
        Context context = FifaApplication.getContext();
        MY_STATS_LEFT_HEADER = context.getString(R.string.you);
        PLAYER_STATS_RIGHT_HEADER = context.getString(R.string.opponent).substring(0, 3);
        DEFAULT_RIGHT_COLOR = ContextCompat.getColor(FifaApplication.getContext(), R.color.statOpponentColor);

        String averages = context.getString(R.string.averages);
        String records = context.getString(R.string.records);
        String totals = context.getString(R.string.totals);
        PLAYER_TAB_TITLES = new String[] {averages, records, totals};
        SERIES_TAB_TITLES = new String[] {averages, totals};
    }

    private List<User.StatsPair> mStatsGroup = new ArrayList<>();
    private String[] mTabTitles;
    private String mLeftHeader;
    private String mRightHeader;
    private boolean[] mShowDecimalForStatsAtPosition;
    private int mLeftColor;
    private int mRightColor;
    
    private StatItemViewModel mGoals;
    private StatItemViewModel mShots;
    private StatItemViewModel mShotsOnTarget;
    private StatItemViewModel mPossession;
    private StatItemViewModel mTackles;
    private StatItemViewModel mFouls;
    private StatItemViewModel mYellowCards;
    private StatItemViewModel mRedCards;
    private StatItemViewModel mOffsides;
    private StatItemViewModel mInjuries;
    private StatItemViewModel mCorners;
    private StatItemViewModel mShotAccuracy;
    private StatItemViewModel mPassAccuracy;

    public static StatsCardViewModel myStats(User me) {
        return new StatsCardViewModel(me, MY_STATS_LEFT_HEADER);
    }

    public static StatsCardViewModel playerStats(User player) {
        return new StatsCardViewModel(player, player.getFirstName());
    }

    public static StatsCardViewModel matchStats(Match match, String username) {
        return new StatsCardViewModel(match, username);
    }

    public static StatsCardViewModel seriesStats(Series series, String username) {
        return new StatsCardViewModel(series, username);
    }
    
    private StatsCardViewModel(Match match, String username) {
        StatsPresenter presenter = new StatsPresenter(match.getStats(), match, username);
        mLeftHeader = presenter.getLeftHeader();
        mRightHeader = presenter.getRightHeader();
        mStatsGroup.add(presenter.getStats());
        mShowDecimalForStatsAtPosition = new boolean[] {false};
        initColorForEvent(presenter);
        initStatViewModels();
    }

    private StatsCardViewModel(Series series, String username) {
        StatsPresenter avgPresenter = new StatsPresenter(series.getAverageStats(), series, username);
        StatsPresenter totalPresenter = new StatsPresenter(series.getTotalStats(), series, username);
        mLeftHeader = avgPresenter.getLeftHeader();
        mRightHeader = avgPresenter.getRightHeader();
        mStatsGroup.add(avgPresenter.getStats());
        mStatsGroup.add(totalPresenter.getStats());
        mShowDecimalForStatsAtPosition = new boolean[] {true, false};
        mTabTitles = SERIES_TAB_TITLES;
        initColorForEvent(avgPresenter);
        initStatViewModels();
    }
    
    private void initColorForEvent(StatsPresenter presenter) {
        mRightColor = Color.parseColor(presenter.getRightColor());
        mLeftColor = Color.parseColor(presenter.getLeftColor());
    }
    
    private StatsCardViewModel(User user, String leftHeader) {
        mLeftHeader = leftHeader;
        mRightHeader = PLAYER_STATS_RIGHT_HEADER;
        mStatsGroup.add(user.getAverageStats());
        mStatsGroup.add(user.getRecordStats());
        mStatsGroup.add(user.getTotalStats());
        mShowDecimalForStatsAtPosition = new boolean[] {true, false, false};
        mTabTitles = PLAYER_TAB_TITLES;
        initColorForPlayer();
        initStatViewModels();
    }
    
    private void initColorForPlayer() {
        mRightColor = DEFAULT_RIGHT_COLOR;
        mLeftColor = FifaApplication.getAccentColor();
        EventBus.getInstance().observeEvents(ColorChangeEvent.class).subscribe(event -> {
            mLeftColor = event.color;
            notifyChange();
        });
    }
    
    private void initStatViewModels() {
        mGoals = newStatItemWithTitle(R.string.goals);
        mShots = newStatItemWithTitle(R.string.shots);
        mShotsOnTarget = newStatItemWithTitle(R.string.shots_on_target);
        mPossession = newStatItemWithTitle(R.string.possession_percent);
        mTackles = newStatItemWithTitle(R.string.tackles);
        mFouls = newStatItemWithTitle(R.string.fouls);
        mYellowCards = newStatItemWithTitle(R.string.yellow_cards);
        mRedCards = newStatItemWithTitle(R.string.red_cards);
        mOffsides = newStatItemWithTitle(R.string.offsides);
        mInjuries = newStatItemWithTitle(R.string.injuries);
        mCorners = newStatItemWithTitle(R.string.corners);
        mShotAccuracy = newStatItemWithTitle(R.string.shot_accuracy_percent);
        mPassAccuracy = newStatItemWithTitle(R.string.pass_accuracy_percent);
        setCurrentStats(0);
    }
    
    private StatItemViewModel newStatItemWithTitle(@StringRes int titleRes) {
        String title = FifaApplication.getContext().getString(titleRes);
        return new StatItemViewModel(mLeftColor, mRightColor, title, mShowDecimalForStatsAtPosition[0]);
    }
    
    private void setCurrentStats(int position) {
        User.StatsPair stats = mStatsGroup.get(position);
        Stats statsFor = stats.getStatsFor();
        Stats statsAgainst = stats.getStatsAgainst();
        boolean showDecimal = mShowDecimalForStatsAtPosition[position];
        mGoals.setValues(statsFor.getGoals(), statsAgainst.getGoals(), showDecimal);
        mShots.setValues(statsFor.getShots(), statsAgainst.getShots(), showDecimal);
        mShotsOnTarget.setValues(statsFor.getShotsOnTarget(), statsAgainst.getShotsOnTarget(), showDecimal);
        mPossession.setValues(statsFor.getPossession(), statsAgainst.getPossession(), showDecimal);
        mTackles.setValues(statsFor.getTackles(), statsAgainst.getTackles(), showDecimal);
        mFouls.setValues(statsFor.getFouls(), statsAgainst.getFouls(), showDecimal);
        mYellowCards.setValues(statsFor.getYellowCards(), statsAgainst.getYellowCards(), showDecimal);
        mRedCards.setValues(statsFor.getRedCards(), statsAgainst.getRedCards(), showDecimal);
        mOffsides.setValues(statsFor.getOffsides(), statsAgainst.getOffsides(), showDecimal);
        mInjuries.setValues(statsFor.getInjuries(), statsAgainst.getInjuries(), showDecimal);
        mCorners.setValues(statsFor.getCorners(), statsAgainst.getCorners(), showDecimal);
        mShotAccuracy.setValues(statsFor.getShotAccuracy(), statsAgainst.getShotAccuracy(), showDecimal);
        mPassAccuracy.setValues(statsFor.getPassAccuracy(), statsAgainst.getPassAccuracy(), showDecimal);
    }

    public int getTabVisibility() {
        return visibleIf(mStatsGroup.size() > 1);
    }

    public String getLeftHeader() {
        return mLeftHeader;
    }

    public String getRightHeader() {
        return mRightHeader;
    }
    
    public String[] getTabTitles() {
        return mTabTitles;
    }
    
    public TabLayout.OnTabSelectedListener getOnTabSelectedListener() {
        return new SimpleOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                setCurrentStats(position);
            }
        };
    }

    public StatItemViewModel getGoals() {
        return mGoals;
    }

    public StatItemViewModel getShots() {
        return mShots;
    }

    public StatItemViewModel getShotsOnTarget() {
        return mShotsOnTarget;
    }

    public StatItemViewModel getPossession() {
        return mPossession;
    }

    public StatItemViewModel getTackles() {
        return mTackles;
    }

    public StatItemViewModel getFouls() {
        return mFouls;
    }

    public StatItemViewModel getYellowCards() {
        return mYellowCards;
    }

    public StatItemViewModel getRedCards() {
        return mRedCards;
    }

    public StatItemViewModel getOffsides() {
        return mOffsides;
    }

    public StatItemViewModel getInjuries() {
        return mInjuries;
    }

    public StatItemViewModel getCorners() {
        return mCorners;
    }

    public StatItemViewModel getShotAccuracy() {
        return mShotAccuracy;
    }

    public StatItemViewModel getPassAccuracy() {
        return mPassAccuracy;
    }
}
