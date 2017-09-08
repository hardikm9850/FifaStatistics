package com.example.kevin.fifastatistics.viewmodels.card;

import android.view.View;

import com.example.kevin.fifastatistics.BR;
import com.example.kevin.fifastatistics.activities.MatchUpdateActivity;
import com.example.kevin.fifastatistics.databinding.CardUpdateStatsBinding;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.interfaces.Consumer;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;

public class CreateStatsCardViewModel extends MatchStatsViewModel {

    private CreateStatsInteraction mInteraction;

    public CreateStatsCardViewModel(Match match, User user, CardUpdateStatsBinding binding,
                                    CreateStatsInteraction interaction, ActivityLauncher launcher) {
        super(match, null, user, binding, MatchUpdateActivity.MatchEditType.CREATE, launcher);
        mInteraction = interaction;
    }

    @Override
    protected StatConsumers getGoalsConsumers() {
        return createConsumers(
                s -> {
                    mMatch.getStatsFor().setGoals(s);
                    mInteraction.onGoalCountChanged(getGoalCount());
                    notifyPropertyChanged(BR.penaltiesItemVisibility);
                },
                s -> {
                    mMatch.getStatsAgainst().setGoals(s);
                    mInteraction.onGoalCountChanged(getGoalCount());
                    notifyPropertyChanged(BR.penaltiesItemVisibility);
                }
        );
    }

    @Override
    protected StatConsumers getShotsConsumers() {
        return createConsumers(
                s -> mMatch.getStatsFor().setShots(s),
                s -> mMatch.getStatsAgainst().setShots(s)
        );
    }

    @Override
    protected StatConsumers getShotsOnTargetConsumers() {
        return createConsumers(
                s -> mMatch.getStatsFor().setShotsOnTarget(s),
                s -> mMatch.getStatsAgainst().setShotsOnTarget(s)
        );
    }

    @Override
    protected StatConsumers getPossessionConsumers() {
        return createConsumers(
                s -> mMatch.getStatsFor().setPossession(s),
                s -> mMatch.getStatsAgainst().setPossession(s)
        );
    }

    @Override
    protected StatConsumers getTacklesConsumers() {
        return createConsumers(
                s -> mMatch.getStatsFor().setTackles(s),
                s -> mMatch.getStatsAgainst().setTackles(s)
        );
    }

    @Override
    protected StatConsumers getFoulsConsumers() {
        return createConsumers(
                s -> mMatch.getStatsFor().setFouls(s),
                s -> mMatch.getStatsAgainst().setFouls(s)
        );
    }

    @Override
    protected StatConsumers getYellowCardsConsumers() {
        return createConsumers(
                s -> {
                    mMatch.getStatsFor().setYellowCards(s);
                    mInteraction.onCardCountChanged(getCardCount());
                },
                s -> {
                    mMatch.getStatsAgainst().setYellowCards(s);
                    mInteraction.onCardCountChanged(getCardCount());
                }
        );
    }

    @Override
    protected StatConsumers getRedCardsConsumers() {
        return createConsumers(
                s -> {
                    mMatch.getStatsFor().setRedCards(s);
                    mInteraction.onCardCountChanged(getCardCount());
                },
                s -> {
                    mMatch.getStatsAgainst().setRedCards(s);
                    mInteraction.onCardCountChanged(getCardCount());
                }
        );
    }

    @Override
    protected StatConsumers getOffsidesConsumers() {
        return createConsumers(
                s -> mMatch.getStatsFor().setOffsides(s),
                s -> mMatch.getStatsAgainst().setOffsides(s)
        );
    }

    @Override
    protected StatConsumers getInjuriesConsumers() {
        return createConsumers(
                s -> {
                    mMatch.getStatsFor().setInjuries(s);
                    mInteraction.onInjuryCountChanged(getInjuryCount());
                },
                s -> {
                    mMatch.getStatsAgainst().setInjuries(s);
                    mInteraction.onInjuryCountChanged(getInjuryCount());
                }
        );
    }

    @Override
    protected StatConsumers getCornersConsumers() {
        return createConsumers(
                s -> mMatch.getStatsFor().setCorners(s),
                s -> mMatch.getStatsAgainst().setCorners(s)
        );
    }

    @Override
    protected StatConsumers getShotAccuracyConsumers() {
        return createConsumers(
                s -> mMatch.getStatsFor().setShotAccuracy(s),
                s -> mMatch.getStatsAgainst().setShotAccuracy(s)
        );
    }

    @Override
    protected StatConsumers getPassAccuracyConsumers() {
        return createConsumers(
                s -> mMatch.getStatsFor().setPassAccuracy(s),
                s -> mMatch.getStatsAgainst().setPassAccuracy(s)
        );
    }

    @Override
    protected int getFor(String key, float matchVal) {
        return Math.round(matchVal);
    }

    @Override
    protected int getAgainst(String key, float matchVal) {
        return Math.round(matchVal);
    }

    @Override
    public void destroy() {
        super.destroy();
        mInteraction = null;
    }

    @Override
    public int getHeaderVisibility() {
        return View.GONE;
    }

    @Override
    public int getPenaltiesItemVisibility() {
        return arePenaltiesRequired() ? View.VISIBLE : View.GONE;
    }

    private boolean arePenaltiesRequired() {
        return mMatch.getScoreWinner() ==  mMatch.getScoreLoser();
    }

    private StatConsumers createConsumers(Consumer<Integer> forCon, Consumer<Integer> againstCon) {
        return new StatConsumers(
                v ->  {
                    forCon.accept(v);
                    mInteraction.onMatchUpdated(mMatch);
                },
                v -> {
                    againstCon.accept(v);
                    mInteraction.onMatchUpdated(mMatch);
                });
    }

    private int getCardCount() {
        return mMatch.getStats().getCardCount();
    }

    private int getInjuryCount() {
        return mMatch.getStats().getInjuryCount();
    }

    private int getGoalCount() {
        return mMatch.getScoreWinner() + mMatch.getScoreLoser();
    }

    public interface CreateStatsInteraction {
        void onGoalCountChanged(int count);
        void onCardCountChanged(int count);
        void onInjuryCountChanged(int count);
        void onMatchUpdated(Match match);
    }
}
