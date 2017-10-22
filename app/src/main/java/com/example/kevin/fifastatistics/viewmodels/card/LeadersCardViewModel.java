package com.example.kevin.fifastatistics.viewmodels.card;

import android.databinding.Bindable;

import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.models.databasemodels.footballers.Leaders;
import com.example.kevin.fifastatistics.models.databasemodels.footballers.MatchEvents;
import com.example.kevin.fifastatistics.viewmodels.FifaBaseViewModel;

import static com.example.kevin.fifastatistics.models.databasemodels.footballers.Leaders.*;

public class LeadersCardViewModel extends FifaBaseViewModel {

    private static final String DEFAULT_LEADER_NAME = "-";
    private static final int DEFAULT_LEADER_VALUE = 0;
    private static final Leader DEFAULT_LEADER;

    static {
        MatchEvents.DummyPlayer player = new MatchEvents.DummyPlayer();
        player.setName(DEFAULT_LEADER_NAME);
        DEFAULT_LEADER = new Leader(player, DEFAULT_LEADER_VALUE);
    }

    private LeaderSet mLeadersFor;
    private LeaderSet mLeadersAgainst;
    private ActivityLauncher mLauncher;

    public LeadersCardViewModel(Leaders leaders, ActivityLauncher launcher) {
        initLeaders(leaders);
        mLauncher = launcher;
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
