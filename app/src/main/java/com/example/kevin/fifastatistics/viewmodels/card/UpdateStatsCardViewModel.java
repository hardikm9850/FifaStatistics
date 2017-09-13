package com.example.kevin.fifastatistics.viewmodels.card;

import android.databinding.Bindable;

import com.example.kevin.fifastatistics.activities.MatchUpdateActivity;
import com.example.kevin.fifastatistics.databinding.CardUpdateStatsBinding;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchUpdate;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;

public class UpdateStatsCardViewModel extends MatchStatsViewModel {

    private MatchUpdate.Builder mUpdateBuilder;

    public UpdateStatsCardViewModel(Match match, MatchUpdate update, User user,
                                    CardUpdateStatsBinding binding, MatchUpdateActivity.MatchEditType type) {
        super(match, update, user, binding, type, null);
        mUpdateBuilder = new MatchUpdate.Builder(update);
    }

    public void init(Match match, MatchUpdate update) {
        mMatch = match == null ? Match.empty() : match;
        mUpdateBuilder = new MatchUpdate.Builder(update);
        mMatchUpdate = update;
        notifyChange();
    }

    @Override
    protected StatConsumers getGoalsConsumers() {
        return new StatConsumers(
                i -> mUpdateBuilder.statsFor().goals(i),
                i -> mUpdateBuilder.statsAgainst().goals(i));
    }

    @Override
    protected StatConsumers getShotsConsumers() {
        return new StatConsumers(
                s -> mUpdateBuilder.statsFor().shots(s),
                s -> mUpdateBuilder.statsAgainst().shots(s));
    }

    @Override
    protected StatConsumers getShotsOnTargetConsumers() {
        return new StatConsumers(
                s -> mUpdateBuilder.statsFor().shotsOnTarget(s),
                s -> mUpdateBuilder.statsAgainst().shotsOnTarget(s));
    }

    @Override
    protected StatConsumers getPossessionConsumers() {
        return new StatConsumers(
                p -> mUpdateBuilder.statsFor().possession(p),
                p -> mUpdateBuilder.statsAgainst().possession(p));
    }

    @Override
    protected StatConsumers getTacklesConsumers() {
        return new StatConsumers(
                t -> mUpdateBuilder.statsFor().tackles(t),
                t -> mUpdateBuilder.statsAgainst().tackles(t));
    }

    @Override
    protected StatConsumers getFoulsConsumers() {
        return new StatConsumers(
                f -> mUpdateBuilder.statsFor().fouls(f),
                f -> mUpdateBuilder.statsAgainst().fouls(f));
    }

    @Override
    protected StatConsumers getYellowCardsConsumers() {
        return new StatConsumers(
                y -> mUpdateBuilder.statsFor().yellowCards(y),
                y -> mUpdateBuilder.statsAgainst().yellowCards(y));
    }

    @Override
    protected StatConsumers getRedCardsConsumers() {
        return new StatConsumers(
                r -> mUpdateBuilder.statsFor().redCards(r),
                r -> mUpdateBuilder.statsAgainst().redCards(r));
    }

    @Override
    protected StatConsumers getOffsidesConsumers() {
        return new StatConsumers(
                o -> mUpdateBuilder.statsFor().offsides(o),
                o -> mUpdateBuilder.statsAgainst().offsides(o));
    }

    @Override
    protected StatConsumers getInjuriesConsumers() {
        return new StatConsumers(
                i -> mUpdateBuilder.statsFor().injuries(i),
                i -> mUpdateBuilder.statsAgainst().injuries(i));
    }

    @Override
    protected StatConsumers getCornersConsumers() {
        return new StatConsumers(
                c -> mUpdateBuilder.statsFor().corners(c),
                c -> mUpdateBuilder.statsAgainst().corners(c));
    }

    @Override
    protected StatConsumers getShotAccuracyConsumers() {
        return new StatConsumers(
                a -> mUpdateBuilder.statsFor().shotAccuracy(a),
                a -> mUpdateBuilder.statsAgainst().shotAccuracy(a));
    }

    @Override
    protected StatConsumers getPassAccuracyConsumers() {
        return new StatConsumers(
                a -> mUpdateBuilder.statsFor().passAccuracy(a),
                a -> mUpdateBuilder.statsAgainst().passAccuracy(a));
    }

    @Override
    protected StatConsumers getPenaltiesConsumers() {
        return new StatConsumers(a -> {}, a -> {});
    }

    @Override
    protected int getFor(String key, float matchVal) {
        Integer updateVal = mUpdateBuilder.build().getStatFor(key);
        return updateVal == null ? Math.round(matchVal) : updateVal;
    }

    @Override
    protected int getAgainst(String key, float matchVal) {
        Integer updateVal = mUpdateBuilder.build().getStatAgainst(key);
        return updateVal == null ? Math.round(matchVal) : updateVal;
    }

    public MatchUpdate build() {
        return mUpdateBuilder
                .creatorId(mUser.getId())
                .receiverId(mMatchUpdate != null ? mMatchUpdate.getReceiverId() : null)
                .matchId(mMatch.getId())
                .id(mMatchUpdate != null ? mMatchUpdate.getId() : null)
                .build();
    }
}
