package com.example.kevin.fifastatistics.adapters;

import android.content.Context;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.data.Trie;
import com.example.kevin.fifastatistics.databinding.ItemCreateMatchEventBinding;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.interfaces.Supplier;
import com.example.kevin.fifastatistics.models.databasemodels.footballers.Footballer;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;
import com.example.kevin.fifastatistics.utils.CollectionUtils;
import com.example.kevin.fifastatistics.viewmodels.item.CreateMatchEventItemViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static com.example.kevin.fifastatistics.models.databasemodels.footballers.MatchEvents.MatchEventItem;

public class CreateMatchEventCardAdapter<T extends MatchEventItem>
        extends AbstractCardAdapter<T, ItemCreateMatchEventBinding, CreateMatchEventItemViewModel<T>> {

    private Context mContext;
    private Trie<Footballer> mFootballers;
    private Team mTeamWinner;
    private Team mTeamLoser;
    private Supplier<T> mItemConstructor;

    public CreateMatchEventCardAdapter(Context context, List<T> items, Trie<Footballer> footballers,
                                       Team teamWinner, Team teamLoser, Supplier<T> itemConstructor) {
        super(items, null, R.layout.item_create_match_event);
        mContext = context;
        mTeamWinner = teamWinner;
        mTeamLoser = teamLoser;
        mFootballers = footballers;
        mItemConstructor = itemConstructor;
        initItems();
    }

    private void initItems() {
        if (mItems != null) {
            ListIterator<T> iterator = mItems.listIterator();
            while (iterator.hasNext()) {
                if (iterator.next() == null) {
                    iterator.set(mItemConstructor.get());
                }
            }
        }
    }

    public void setTeamWinner(Team teamWinner) {
        mTeamWinner = teamWinner;
        notifyDataSetChanged();
    }

    public void setTeamLoser(Team teamLoser) {
        mTeamLoser = teamLoser;
        notifyDataSetChanged();
    }

    public List<T> getItems() {
        return mItems;
    }

    public void setCount(int count) {
        if (count <= 0) {
            mItems = null;
            notifyDataSetChanged();
        } else if (count < CollectionUtils.getSize(mItems)) {
            removeExtraEvents(count);
        } else if (count > CollectionUtils.getSize(mItems)) {
            addNewEvents(count);
        }
    }

    private void removeExtraEvents(int newCount) {
        int oldSize = mItems.size();
        int eventsToRemove = oldSize - newCount;
        int startPosition = oldSize - eventsToRemove;
        mItems.subList(startPosition, oldSize).clear();
        notifyItemRangeRemoved(startPosition, eventsToRemove);
    }

    private void addNewEvents(int newCount) {
        int difference = newCount;
        int oldCount = CollectionUtils.getSize(mItems);
        if (mItems == null) {
            mItems = new ArrayList<T>(newCount);
        } else {
            difference = newCount - oldCount;
        }
        for (int i = 0; i < difference; i++) {
            mItems.add(mItemConstructor.get());
        }
        notifyItemRangeInserted(oldCount, difference);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected CreateMatchEventItemViewModel<T> getBindingViewModel(ItemCreateMatchEventBinding binding) {
        return binding.getViewModel();
    }

    @Override
    protected CreateMatchEventItemViewModel<T> createViewModel(T item, ActivityLauncher launcher, boolean isLastItem, int color) {
        return new CreateMatchEventItemViewModel<>(mContext, item, isLastItem, mTeamWinner, mTeamLoser, mFootballers);
    }

    @Override
    protected void onRebind(CreateMatchEventItemViewModel<T> viewModel) {
        viewModel.setTeams(mTeamWinner, mTeamLoser);
    }
}
