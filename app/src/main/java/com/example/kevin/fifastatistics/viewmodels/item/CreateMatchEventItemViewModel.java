package com.example.kevin.fifastatistics.viewmodels.item;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.adapters.SearchViewBindingAdapter;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.design.widget.TabLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;

import com.android.databinding.library.baseAdapters.BR;
import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.adapters.MatchEventFootballerAdapter;
import com.example.kevin.fifastatistics.data.MinMaxInputFilter;
import com.example.kevin.fifastatistics.data.Trie;
import com.example.kevin.fifastatistics.listeners.SimpleOnTabSelectedListener;
import com.example.kevin.fifastatistics.models.databasemodels.footballers.Footballer;
import com.example.kevin.fifastatistics.models.databasemodels.footballers.MatchEvents;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.views.HeadshotView;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.example.kevin.fifastatistics.models.databasemodels.footballers.MatchEvents.CardItem;
import static com.example.kevin.fifastatistics.models.databasemodels.footballers.MatchEvents.CardType;
import static com.example.kevin.fifastatistics.models.databasemodels.footballers.MatchEvents.GoalItem;
import static com.example.kevin.fifastatistics.models.databasemodels.footballers.MatchEvents.InjuryItem;
import static com.example.kevin.fifastatistics.models.databasemodels.footballers.MatchEvents.InjuryType;
import static com.example.kevin.fifastatistics.models.databasemodels.footballers.MatchEvents.MatchEventItem;

public class CreateMatchEventItemViewModel<T extends MatchEventItem> extends ItemViewModel<T> {

    private static final InputFilter MINUTE_INPUT_FILTER = new MinMaxInputFilter(1, Match.MAX_MINUTE);
    private static final int[] CARD_ICONS;
    private static final int[] INJURY_ICONS;
    private static final Map<Integer, InjuryType> INJURY_TYPE_MAPPING;
    private static final Map<Integer, CardType> CARD_TYPE_MAPPING;

    static {
        INJURY_TYPE_MAPPING = new HashMap<>(3);
        INJURY_TYPE_MAPPING.put(0, InjuryType.MINOR);
        INJURY_TYPE_MAPPING.put(1, InjuryType.MAJOR);
        INJURY_TYPE_MAPPING.put(2, InjuryType.SEVERE);
        CARD_TYPE_MAPPING = new HashMap<>(3);
        CARD_TYPE_MAPPING.put(0, CardType.YELLOW);
        CARD_TYPE_MAPPING.put(1, CardType.DIRECT_RED);
        CARD_TYPE_MAPPING.put(2, CardType.YELLOW_RED);
        CARD_ICONS = new int[] {R.drawable.ic_soccer_yellow_card, R.drawable.ic_soccer_red_card, R.drawable.ic_soccer_yellow_red_card};
        INJURY_ICONS = new int[] {R.drawable.icons8_bandage, R.drawable.icons8_hospital, R.drawable.icons8_ambulance};
    }

    private MatchEventFootballerAdapter mFootballerAdapter;
    private Team mTeamWinner;
    private Team mTeamLoser;
    private Team mCurrentTeam;
    private View mChangeTeamButton;

    public CreateMatchEventItemViewModel(Context context, T item, boolean isLastItem, Team teamWinner,
                                         Team teamLoser, Trie<Footballer> footballers) {
        super(item, null, isLastItem);
        initTeams(teamWinner, teamLoser);
        mFootballerAdapter = new MatchEventFootballerAdapter(context, footballers);
    }

    private void initTeams(Team teamWinner, Team teamLoser) {
        mTeamWinner = teamWinner;
        mTeamLoser = teamLoser;
        mCurrentTeam = mItem.isForWinner() ? mTeamWinner : mTeamLoser;
    }

    public void setTeams(Team teamWinner, Team teamLoser) {
        initTeams(teamWinner, teamLoser);
        notifyPropertyChanged(BR.teamImageUrl);
        notifyPropertyChanged(BR.teamColor);
    }

    public void onMinuteUpdated(Editable s) {
        Integer minute = TextUtils.isEmpty(s.toString()) ? null : Integer.valueOf(s.toString());
        if (minute != null) {
            mItem.setMinute(minute);
        }
    }

    public void onChangeTeam(View changeTeamButton) {
        mChangeTeamButton = changeTeamButton;
        mChangeTeamButton.setClickable(false);
        if (mCurrentTeam != null && mCurrentTeam.equals(mTeamWinner)) {
            mCurrentTeam = mTeamLoser;
            mItem.setForWinner(false);
        } else {
            mCurrentTeam = mTeamWinner;
            mItem.setForWinner(true);
        }
        notifyPropertyChanged(BR.teamImageUrl);
        notifyPropertyChanged(BR.teamColor);
    }

    public int getTabSelectorVisibility() {
        return visibleIf(!(mItem instanceof GoalItem));
    }

    public int getOwnGoalSelectorVisibility() {
        return visibleIf(mItem instanceof GoalItem);
    }

    public int[] getSelectorIcons() {
        if (mItem instanceof CardItem) {
            return CARD_ICONS;
        } else if (mItem instanceof InjuryItem) {
            return INJURY_ICONS;
        } else {
            return null;
        }
    }

    @Bindable
    public String getTeamImageUrl() {
        return mCurrentTeam != null ? mCurrentTeam.getCrestUrl() : null;
    }

    @ColorInt
    @Bindable
    public int getTeamColor() {
        return mCurrentTeam != null ? Color.parseColor(mCurrentTeam.getColor()) : getColor(R.color.transparent);
    }

    @Bindable
    public MatchEvents.DummyPlayer getPlayer() {
        return mItem.getPlayer() != null ? mItem.getPlayer() : null;
    }

    @Bindable
    public String getPlayerName() {
        return mItem.getPlayer() != null ? mItem.getPlayer().getName() : null;
    }

    public MatchEventFootballerAdapter getAdapter() {
        return mFootballerAdapter;
    }

    public SearchViewBindingAdapter.OnSuggestionClick getOnSuggestionClick() {
        return position -> {
            Footballer footballer = mFootballerAdapter.getItem(position);
            mItem.setPlayer(new MatchEvents.DummyPlayer(footballer));
            notifyPropertyChanged(BR.playerName);
            notifyPropertyChanged(BR.player);
            return true;
        };
    }

    public View.OnClickListener getOnClearListener() {
        return v -> {
            mItem.setPlayer(null);
            notifyPropertyChanged(BR.playerName);
            notifyPropertyChanged(BR.player);
        };
    }

    public HeadshotView.OnColorAnimationCompleteListener onColorAnimationComplete() {
        return () -> {
            if (mChangeTeamButton != null) {
                mChangeTeamButton.setClickable(true);
            }
        };
    }

    public TabLayout.OnTabSelectedListener getOnTabSelectedListener() {
        return new SimpleOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (mItem instanceof CardItem) {
                    CardType type = CARD_TYPE_MAPPING.get(position);
                    ((CardItem) mItem).setType(type);
                } else {
                    InjuryType type = INJURY_TYPE_MAPPING.get(position);
                    ((InjuryItem) mItem).setType(type);
                }
            }
        };
    }

    public CompoundButton.OnCheckedChangeListener getOnOwnGoalChangedListener() {
        return (buttonView, isChecked) -> ((GoalItem) mItem).setOwnGoal(isChecked);
    }

    public InputFilter getMinuteInputFilter() {
        return MINUTE_INPUT_FILTER;
    }
}

