package com.example.kevin.fifastatistics.adapters;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.databinding.ItemDateHeaderBinding;
import com.example.kevin.fifastatistics.databinding.ItemMatchBinding;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchProjection;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.viewmodels.MatchesItemViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MatchesRecyclerViewAdapter extends StickyEndlessProgressAdapter<ItemDateHeaderBinding, ItemMatchBinding, MatchProjection> {

    private Player mUser;

    public MatchesRecyclerViewAdapter(Player user) {
        super(R.layout.item_date_header, R.layout.item_match);
        mUser = user;
    }

    @Override
    protected BindingViewHolder<ItemMatchBinding, MatchProjection> createEventViewHolder(ItemMatchBinding binding) {
        return new MatchViewHolder(binding);
    }

    @Override
    protected BindingViewHolder<ItemDateHeaderBinding, MatchProjection> createHeaderViewHolder(ItemDateHeaderBinding binding) {
        return new HeaderViewHolder(binding);
    }

    public class MatchViewHolder extends BindingViewHolder<ItemMatchBinding, MatchProjection> {

        MatchViewHolder(ItemMatchBinding binding) {
            super(binding);
        }

        @Override
        public void bindItem(MatchProjection match) {
            MatchesItemViewModel viewModel = mBinding.getMatchItemViewModel();
            if (viewModel != null) {
                viewModel.setMatch(match, mUser);
            } else {
                viewModel = new MatchesItemViewModel(match, mUser);
                mBinding.setMatchItemViewModel(viewModel);
            }
        }
    }

    public static class HeaderViewHolder extends BindingViewHolder<ItemDateHeaderBinding, MatchProjection> {

        HeaderViewHolder(ItemDateHeaderBinding binding) {
            super(binding);
        }

        @Override
        void bindItem(MatchProjection item) {
            if (item.getDate() != null) {
                DateFormat format = SimpleDateFormat.getDateInstance();
                mBinding.setDate(format.format(item.getDate()));
            }
        }
    }
}
