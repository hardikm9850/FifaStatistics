package com.example.kevin.fifastatistics.viewmodels.card;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.support.annotation.PluralsRes;
import android.support.v4.util.Pair;
import android.support.v4.widget.NestedScrollView;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.example.kevin.fifastatistics.FifaApplication;
import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.adapters.CreateMatchEventCardAdapter;
import com.example.kevin.fifastatistics.data.Trie;
import com.example.kevin.fifastatistics.interfaces.Supplier;
import com.example.kevin.fifastatistics.models.databasemodels.footballers.Footballer;
import com.example.kevin.fifastatistics.models.databasemodels.footballers.MatchEvents;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;

import java.util.List;

public class CreateMatchEventsCardViewModel<T extends MatchEvents.MatchEventItem>
        extends RecyclerCardViewModel<T, CreateMatchEventCardAdapter<T>> {

    private static final String ERROR_MISSING_FIELDS;

    static {
        Resources r = FifaApplication.getContext().getResources();
        ERROR_MISSING_FIELDS = r.getString(R.string.error_missing_fields);
    }

    private Context mContext;
    private String mCountErrorMessage;
    private boolean mIsFieldMissing;
    private boolean mIsFieldCountsIncorrect;
    private @PluralsRes int mCountErrorMessageRes;

    public CreateMatchEventsCardViewModel(Context context, List<T> items, Trie<Footballer> footballers,
                                          Team teamWinner, Team teamLoser, Supplier<T> itemCreator) {
        super(items);
        mAdapter = new CreateMatchEventCardAdapter<>(context, items, footballers, teamWinner,
                teamLoser, itemCreator);
        mContext = context;
        setupErrorMessage(itemCreator);
    }

    private void setupErrorMessage(Supplier<T> itemCreator) {
        T dummyItem = itemCreator.get();
        if (dummyItem instanceof MatchEvents.GoalItem) {
            mCountErrorMessageRes = R.plurals.error_wrong_goals_count;
        } else if (dummyItem instanceof MatchEvents.CardItem) {
            mCountErrorMessageRes = R.plurals.error_wrong_cards_count;
        } else {
            mCountErrorMessageRes = R.plurals.error_wrong_injuries_count;
        }
    }

    public List<T> getItems() {
        return mAdapter.getItems();
    }

    public boolean validateFieldsAreFilled() {
        boolean isFieldMissing = !mAdapter.validateAllFieldsFilled();
        if (mIsFieldMissing != isFieldMissing) {
            mIsFieldMissing = isFieldMissing;
            notifyPropertyChanged(BR.errorMessageVisibility);
            notifyPropertyChanged(BR.errorMessage);
        }
        return !isFieldMissing;
    }

    public boolean validateCorrectTeamCounts(Pair<Integer, Integer> required, Team winner, Team loser) {
        Pair<Integer, Integer> counts = MatchEvents.calculateTotalsFor(getItems());
        boolean isFieldCountIncorrect = !required.equals(counts);
        if (mIsFieldCountsIncorrect != isFieldCountIncorrect) {
            mIsFieldCountsIncorrect = isFieldCountIncorrect;
            setupCountErrorMessage(required, winner, loser);
            notifyPropertyChanged(BR.errorMessageVisibility);
            notifyPropertyChanged(BR.errorMessage);
        }
        return !isFieldCountIncorrect;
    }

    private void setupCountErrorMessage(Pair<Integer, Integer> required, Team winner, Team loser) {
        Resources r = mContext.getResources();
        int first = required.first;
        mCountErrorMessage = r.getQuantityString(mCountErrorMessageRes,
                first, first, winner.getShortName(), required.second, loser.getShortName());
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
        return visibleIf(mIsFieldMissing || mIsFieldCountsIncorrect);
    }

    @Bindable
    public String getErrorMessage() {
        if (mIsFieldMissing) {
            return ERROR_MISSING_FIELDS;
        } else {
            return mCountErrorMessage;
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        mContext = null;
    }
}
