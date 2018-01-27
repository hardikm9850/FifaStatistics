package com.example.kevin.fifastatistics.viewmodels.card;

import android.content.Context;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
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

    private boolean mIsFieldMissing;

    public CreateMatchEventsCardViewModel(Context context, List<T> items, Trie<Footballer> footballers,
                                          Team teamWinner, Team teamLoser, Supplier<T> itemCreator) {
        super(items);
        mAdapter = new CreateMatchEventCardAdapter<>(context, items, footballers, teamWinner,
                teamLoser, itemCreator);
    }

    public List<T> getItems() {
        return mAdapter.getItems();
    }

    public boolean validateFieldsAreFilled() {
        boolean isFieldMissing = !mAdapter.validateAllFieldsFilled();
        if (mIsFieldMissing != isFieldMissing) {
            mIsFieldMissing = isFieldMissing;
            notifyPropertyChanged(BR.errorMessageVisibility);
        }
        return !isFieldMissing;
    }

    public void setCount(int count) {
        mAdapter.setCount(count);
        notifyPropertyChanged(BR.visibility);
    }

    public void setTeamWinner(Team team) {
        mAdapter.setTeamWinner(team);
    }

    public void setTeamLoser(Team team) {
        mAdapter.setTeamLoser(team);
    }

    @NonNull
    @Override
    protected CreateMatchEventCardAdapter<T> createAdapter() {
        return mAdapter;
    }

    public void onErrorVisibilityChanged(View errorMessage, int visibility, NestedScrollView parent) {
        if (visibility == View.VISIBLE) {
            parent.requestChildFocus(errorMessage, errorMessage);
        }
    }

    @Override
    @Bindable
    public int getVisibility() {
        return mAdapter.getItemCount() > 0 ? View.VISIBLE : View.GONE;
    }

    @Bindable
    public int getErrorMessageVisibility() {
        return visibleIf(mIsFieldMissing);
    }
}
