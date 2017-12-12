package com.example.kevin.fifastatistics.viewmodels.card;

import android.content.Context;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.example.kevin.fifastatistics.adapters.CreateMatchEventCardAdapter;
import com.example.kevin.fifastatistics.data.Trie;
import com.example.kevin.fifastatistics.interfaces.Supplier;
import com.example.kevin.fifastatistics.models.databasemodels.footballers.Footballer;
import com.example.kevin.fifastatistics.models.databasemodels.footballers.MatchEvents;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;

import java.util.List;

public class CreateMatchEventsCardViewModel<T extends MatchEvents.MatchEventItem>
        extends RecyclerCardViewModel<T, CreateMatchEventCardAdapter<T>> {

    public CreateMatchEventsCardViewModel(Context context, List<T> items, Trie<Footballer> footballers,
                                          Team teamWinner, Team teamLoser, Supplier<T> itemCreator) {
        super(items);
        mAdapter = new CreateMatchEventCardAdapter<>(context, items, footballers, teamWinner,
                teamLoser, itemCreator);
    }

    public void setCount(int count) {
        mAdapter.setCount(count);
        notifyPropertyChanged(BR.visibility);
    }

    @NonNull
    @Override
    protected CreateMatchEventCardAdapter<T> createAdapter() {
        return mAdapter;
    }

    @Override
    @Bindable
    public int getVisibility() {
        return mAdapter.getItemCount() > 0 ? View.VISIBLE : View.GONE;
    }
}