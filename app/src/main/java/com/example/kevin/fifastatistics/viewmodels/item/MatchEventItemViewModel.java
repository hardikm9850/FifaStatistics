package com.example.kevin.fifastatistics.viewmodels.item;

import android.databinding.Bindable;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;
import com.example.kevin.fifastatistics.utils.MinuteFormatter;

import static com.example.kevin.fifastatistics.models.databasemodels.footballers.MatchEvents.DummyPlayer;
import static com.example.kevin.fifastatistics.models.databasemodels.footballers.MatchEvents.MatchEventItem;

public class MatchEventItemViewModel<T extends MatchEventItem> extends ItemViewModel<T> {

    private DummyPlayer mPlayer;
    private Team mTeam;

    public MatchEventItemViewModel(T item, ActivityLauncher launcher, boolean isLastItem,
                                   Team teamWinner, Team teamLoser) {
        super(item, launcher, 0, isLastItem);
        onSetItem(item);
        mTeam = item != null ? (item.isForWinner() ? teamWinner : teamLoser) : null;
    }

    @Override
    protected void onSetItem(MatchEventItem item) {
        mPlayer = item != null ? item.getPlayer() : null;
    }

    @Bindable
    public String getPlayerImageUrl() {
        return mPlayer != null ? mPlayer.getHeadshotImgUrl() : null;
    }

    @Bindable
    public String getTeamImageUrl() {
        return mTeam != null ? mTeam.getCrestUrl() : null;
    }

    @Bindable
    public String getTeamCode() {
        return mTeam != null ? mTeam.getCode() : null;
    }

    @Bindable
    public String getName() {
        return mPlayer != null ? mPlayer.getName() : null;
    }

    @Bindable
    public String getMinute() {
        return mItem != null ? MinuteFormatter.format(mItem.getMinute(), mItem.getInjuryTime()) : null;
    }

    @Bindable
    public Drawable getIcon() {
        return mItem != null ? getDrawable(mItem.getIcon()) : null;
    }

    @ColorInt
    @Bindable
    public int getMinuteTextColor() {
        if (mItem.isGoldenGoal() && mIsLastItem) {
            return getColor(R.color.gold);
        } else {
            return getColor(android.R.color.white);
        }
    }

    @ColorInt
    @Bindable
    public int getTeamColor() {
        return mTeam != null ? Color.parseColor(mTeam.getColor()) : getColor(R.color.transparent);
    }

    public void onImageClick() {
        // OPEN FOOTBALLER ACTIVITY
    }
}
