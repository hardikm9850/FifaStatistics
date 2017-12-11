package com.example.kevin.fifastatistics.viewmodels.item;

import android.content.res.Resources;
import android.databinding.Bindable;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;

import com.example.kevin.fifastatistics.FifaApplication;
import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.models.databasemodels.footballers.MatchEvents;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;
import com.example.kevin.fifastatistics.utils.MinuteFormatter;

import static com.example.kevin.fifastatistics.models.databasemodels.footballers.MatchEvents.DummyPlayer;
import static com.example.kevin.fifastatistics.models.databasemodels.footballers.MatchEvents.MatchEventItem;

public class MatchEventItemViewModel<T extends MatchEventItem> extends ItemViewModel<T> {

    private static final String OWN_GOAL;

    static {
        Resources r = FifaApplication.getContext().getResources();
        OWN_GOAL = " " + r.getString(R.string.own_goal_code);
    }

    private DummyPlayer mPlayer;
    private Team mTeam;

    public MatchEventItemViewModel(T item, ActivityLauncher launcher, boolean isLastItem,
                                   Team teamWinner, Team teamLoser) {
        super(item, launcher, isLastItem);
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
        if (mPlayer != null) {
            String name = mPlayer.getName();
            return isOwnGoal() ? name + OWN_GOAL : name;
        } else {
            return null;
        }
    }

    private boolean isOwnGoal() {
        return mItem instanceof MatchEvents.GoalItem && ((MatchEvents.GoalItem) mItem).isOwnGoal();
    }

    @Bindable
    public String getMinute() {
        return mItem != null ? MinuteFormatter.format(mItem.getMinute()) : null;
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
