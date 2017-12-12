package com.example.kevin.fifastatistics.viewmodels.item;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.adapters.AdapterViewBindingAdapter;
import android.databinding.adapters.SearchViewBindingAdapter;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.android.databinding.library.baseAdapters.BR;
import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.adapters.MatchEventFootballerAdapter;
import com.example.kevin.fifastatistics.data.Trie;
import com.example.kevin.fifastatistics.interfaces.Supplier;
import com.example.kevin.fifastatistics.models.databasemodels.footballers.Footballer;
import com.example.kevin.fifastatistics.models.databasemodels.footballers.MatchEvents;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;

import static com.example.kevin.fifastatistics.models.databasemodels.footballers.MatchEvents.MatchEventItem;

public class CreateMatchEventItemViewModel<T extends MatchEventItem> extends ItemViewModel<T> {

    private MatchEventFootballerAdapter mAdapter;
    private Team mTeamWinner;
    private Team mTeamLoser;
    private Team mCurrentTeam;

    public CreateMatchEventItemViewModel(Context context, @NonNull T item, boolean isLastItem,
                                         Team teamWinner, Team teamLoser, Trie<Footballer> footballers, Supplier<T> itemConstructor) {
        super(itemConstructor.get(), null, isLastItem);
        mTeamWinner = mCurrentTeam = teamWinner;
        mTeamLoser = teamLoser;
        mAdapter = new MatchEventFootballerAdapter(context, footballers);
    }

    public void setTeamWinner(Team teamWinner) {
        if (mCurrentTeam != null && mCurrentTeam.equals(mTeamWinner)) {
            mCurrentTeam = mTeamWinner = teamWinner;
        }
        mTeamWinner = teamWinner;
        notifyPropertyChanged(BR.teamImageUrl);
    }

    public void setTeamLoser(Team teamLoser) {
        if (mCurrentTeam != null && mCurrentTeam.equals(mTeamLoser)) {
            mCurrentTeam = mTeamLoser = teamLoser;
        }
        mTeamLoser = teamLoser;
        notifyPropertyChanged(BR.teamImageUrl);
    }

    public void onMinuteUpdated(int minute) {
        mItem.setMinute(minute);
    }

    public void onPlayerSelected(Footballer footballer) {
        mItem.setPlayer(new MatchEvents.DummyPlayer(footballer));
        notifyPropertyChanged(BR.playerImageUrl);
    }

    public void onChangeTeam() {
        if (mCurrentTeam != null && mCurrentTeam.equals(mTeamWinner)) {
            mCurrentTeam = mTeamLoser;
        } else {
            mCurrentTeam = mTeamWinner;
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
    public String getPlayerImageUrl() {
        return mItem.getPlayer() != null ? mItem.getPlayer().getHeadshotImgUrl() : null;
    }

    @Bindable
    public String getPlayerName() {
        return mItem.getPlayer() != null ? mItem.getPlayer().getName() : null;
    }

    public MatchEventFootballerAdapter getAdapter() {
        return mAdapter;
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Object o = parent.getSelectedItem();
        Log.d("KEVIN!!", "Class of selected: " + o.getClass().getName());
    }

    public SearchViewBindingAdapter.OnSuggestionClick getOnSuggestionClick() {
        return position -> {
            Footballer footballer = mAdapter.getItem(position);
            mItem.setPlayer(new MatchEvents.DummyPlayer(footballer));
            notifyPropertyChanged(BR.playerName);
            return true;
        };
    }
}

