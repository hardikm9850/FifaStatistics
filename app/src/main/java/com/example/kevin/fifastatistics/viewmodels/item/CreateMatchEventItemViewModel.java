package com.example.kevin.fifastatistics.viewmodels.item;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.adapters.SearchViewBindingAdapter;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.adapters.MatchEventFootballerAdapter;
import com.example.kevin.fifastatistics.data.MinMaxInputFilter;
import com.example.kevin.fifastatistics.data.Trie;
import com.example.kevin.fifastatistics.interfaces.Supplier;
import com.example.kevin.fifastatistics.models.databasemodels.footballers.Footballer;
import com.example.kevin.fifastatistics.models.databasemodels.footballers.MatchEvents;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.views.HeadshotView;

import static com.example.kevin.fifastatistics.models.databasemodels.footballers.MatchEvents.MatchEventItem;

public class CreateMatchEventItemViewModel<T extends MatchEventItem> extends ItemViewModel<T> {

    private static final InputFilter MINUTE_INPUT_FILTER = new MinMaxInputFilter(1, Match.MAX_MINUTE);

    private MatchEventFootballerAdapter mAdapter;
    private Team mTeamWinner;
    private Team mTeamLoser;
    private Team mCurrentTeam;
    private View mChangeTeamButton;

    public CreateMatchEventItemViewModel(Context context, T item, boolean isLastItem, Team teamWinner,
                                         Team teamLoser, Trie<Footballer> footballers, Supplier<T> itemConstructor) {
        super(item != null ? item : itemConstructor.get(), null, isLastItem);
        initTeams(teamWinner, teamLoser);
        mAdapter = new MatchEventFootballerAdapter(context, footballers);
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
        return mAdapter;
    }

    public SearchViewBindingAdapter.OnSuggestionClick getOnSuggestionClick() {
        return position -> {
            Footballer footballer = mAdapter.getItem(position);
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

    public InputFilter getMinuteInputFilter() {
        return MINUTE_INPUT_FILTER;
    }
}

