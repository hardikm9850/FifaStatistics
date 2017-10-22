package com.example.kevin.fifastatistics.viewmodels.card;

import android.content.Context;
import android.databinding.Bindable;

import com.example.kevin.fifastatistics.FifaApplication;
import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.managers.preferences.PrefsManager;
import com.example.kevin.fifastatistics.models.databasemodels.footballers.Leaders;
import com.example.kevin.fifastatistics.models.databasemodels.footballers.MatchEvents;
import com.example.kevin.fifastatistics.viewmodels.FifaBaseViewModel;

import static com.example.kevin.fifastatistics.models.databasemodels.footballers.Leaders.*;

public class LeadersCardViewModel extends FifaBaseViewModel {

    private static final String DEFAULT_LEADER_NAME = "-";
    private static final int DEFAULT_LEADER_VALUE = 0;
    private static final String MY_LEFT_HEADER;
    private static final String OPP_RIGHT_HEADER;
    private static final Leader DEFAULT_LEADER;

    static {
        MatchEvents.DummyPlayer player = new MatchEvents.DummyPlayer();
        player.setName(DEFAULT_LEADER_NAME);
        DEFAULT_LEADER = new Leader(player, DEFAULT_LEADER_VALUE);

        Context context = FifaApplication.getContext();
        MY_LEFT_HEADER = context.getString(R.string.you);
        OPP_RIGHT_HEADER = context.getString(R.string.opponent).substring(0, 3);
    }

    private LeaderSet mLeadersFor;
    private LeaderSet mLeadersAgainst;
    private String mUsername;
    private ActivityLauncher mLauncher;

    public LeadersCardViewModel(Leaders leaders, String username, ActivityLauncher launcher) {
        initUsername(username);
        initLeaders(leaders);
        mLauncher = launcher;
    }

    private void initUsername(String username) {
        if (username != null && username.equals(PrefsManager.getUserName())) {
            username = MY_LEFT_HEADER;
        }
        mUsername = username;
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

    public String getLeftHeader() {
        return mUsername;
    }

    public String getRightHeader() {
        return OPP_RIGHT_HEADER;
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

    @Override
    public void destroy() {
        super.destroy();
        mLauncher = null;
    }
}
