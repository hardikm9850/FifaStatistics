package com.example.kevin.fifastatistics.viewmodels.card;

import android.content.Context;
import android.databinding.Bindable;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;

import com.example.kevin.fifastatistics.FifaApplication;
import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.event.ColorChangeEvent;
import com.example.kevin.fifastatistics.event.EventBus;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.managers.StatsPresenter;
import com.example.kevin.fifastatistics.models.databasemodels.footballers.Leaders;
import com.example.kevin.fifastatistics.models.databasemodels.footballers.MatchEvents;
import com.example.kevin.fifastatistics.models.databasemodels.match.Series;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.viewmodels.FifaBaseViewModel;

import static com.example.kevin.fifastatistics.models.databasemodels.footballers.Leaders.*;

public class LeadersCardViewModel extends FifaBaseViewModel {

    private static final String DEFAULT_LEADER_NAME = "-";
    private static final int DEFAULT_LEADER_VALUE = 0;
    private static final String MY_LEFT_HEADER;
    private static final String OPP_RIGHT_HEADER;
    private static final Leader DEFAULT_LEADER;
    private static final int DEFAULT_RIGHT_COLOR;

    static {
        MatchEvents.DummyPlayer player = new MatchEvents.DummyPlayer();
        player.setName(DEFAULT_LEADER_NAME);
        DEFAULT_LEADER = new Leader(player, DEFAULT_LEADER_VALUE);

        Context context = FifaApplication.getContext();
        MY_LEFT_HEADER = context.getString(R.string.you);
        OPP_RIGHT_HEADER = context.getString(R.string.opponent).substring(0, 3);

        DEFAULT_RIGHT_COLOR = ContextCompat.getColor(context, R.color.statOpponentColor);
    }

    private LeaderSet mLeadersFor;
    private LeaderSet mLeadersAgainst;
    private String mLeftHeader;
    private String mRightHeader;
    private int mLeftColor;
    private int mRightColor;
    private ActivityLauncher mLauncher;

    public static LeadersCardViewModel series(Series series, Player user, ActivityLauncher launcher) {
        Leaders leaders = series.lostBy(user) ? Leaders.swapped(series.getLeaders()) : series.getLeaders();
        boolean isPlayerIncluded = series.didInclude(user);
        LeadersCardViewModel viewModel = new LeadersCardViewModel(leaders, user.getName(), launcher, isPlayerIncluded);
        StatsPresenter presenter = new StatsPresenter(series.getTotalStats(), series, user.getName());
        viewModel.setColors(Color.parseColor(presenter.getLeftColor()), Color.parseColor(presenter.getRightColor()));
        viewModel.mLeftHeader = presenter.getLeftHeader();
        viewModel.mRightHeader = presenter.getRightHeader();
        return viewModel;
    }

    public LeadersCardViewModel(Leaders leaders, String username, ActivityLauncher launcher,
                                boolean isMyLeaders) {
        initHeaders(username, isMyLeaders);
        initColors();
        initLeaders(leaders);
        mLauncher = launcher;
    }

    private void initHeaders(String username, boolean isMyLeaders) {
        if (isMyLeaders) {
            username = MY_LEFT_HEADER;
        }
        mLeftHeader = username;
        mRightHeader = OPP_RIGHT_HEADER;
    }

    private void initColors() {
        mLeftColor = FifaApplication.getAccentColor();
        EventBus.getInstance().observeEvents(ColorChangeEvent.class).subscribe(event -> {
            mLeftColor = event.color;
            notifyChange();
        });
        mRightColor = DEFAULT_RIGHT_COLOR;
    }

    private void initLeaders(Leaders leaders) {
        leaders = leaders == null ? Leaders.empty() : leaders;
        leaders = initNullLeaders(leaders);
        mLeadersFor = leaders.getLeadersFor();
        mLeadersAgainst = leaders.getLeadersAgainst();
    }

    private Leaders initNullLeaders(Leaders leaders) {
        if (leaders.getLeadersFor() == null) {
            leaders.setLeadersFor(new LeaderSet());
        }
        if (leaders.getLeadersAgainst() == null) {
            leaders.setLeadersAgainst(new LeaderSet());
        }
        leaders.getLeadersFor().initNullLeadersWith(DEFAULT_LEADER);
        leaders.getLeadersAgainst().initNullLeadersWith(DEFAULT_LEADER);
        return leaders;
    }

    public void setLeaders(Leaders leaders) {
        initLeaders(leaders);
        notifyChange();
    }

    public void setColors(@ColorInt int leftColor, @ColorInt int rightColor) {
        mLeftColor = leftColor;
        mRightColor = rightColor;
    }

    public String getLeftHeader() {
        return mLeftHeader;
    }

    public String getRightHeader() {
        return mRightHeader;
    }

    @Bindable
    public Leader getGoalsFor() {
        return mLeadersFor.getGoals();
    }

    @Bindable
    public Leader getGoalsAgainst() {
        return mLeadersAgainst.getGoals();
    }

    @Bindable
    public Leader getYellowCardsFor() {
        return mLeadersFor.getYellowCards();
    }

    @Bindable
    public Leader getYellowCardsAgainst() {
        return mLeadersAgainst.getYellowCards();
    }

    @Bindable
    public Leader getRedCardsFor() {
        return mLeadersFor.getRedCards();
    }

    @Bindable
    public Leader getRedCardsAgainst() {
        return mLeadersAgainst.getRedCards();
    }
    
    @Bindable
    public Leader getInjuriesFor() {
        return mLeadersFor.getInjuries();
    }

    @Bindable
    public Leader getInjuriesAgainst() {
        return mLeadersAgainst.getInjuries();
    }

    @Bindable
    public int getLeftColor() {
        return mLeftColor;
    }

    @Bindable
    public int getRightColor() {
        return mRightColor;
    }

    @Override
    public void destroy() {
        super.destroy();
        mLauncher = null;
    }
}
