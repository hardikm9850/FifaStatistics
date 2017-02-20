package com.example.kevin.fifastatistics.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.databinding.ItemMatchBinding;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchProjection;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.viewmodels.MatchesItemViewModel;

import java.util.ArrayList;
import java.util.List;

public class MatchesRecyclerViewAdapter extends EndlessProgressAdapter {

    private static final int MATCH_ITEM_VIEW_TYPE = 1;

    private List<MatchProjection> mMatchProjections;
    private Player mUser;

    public MatchesRecyclerViewAdapter(Player user) {
        mMatchProjections = new ArrayList<>();
        mUser = user;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == MATCH_ITEM_VIEW_TYPE) {
            ItemMatchBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_match, parent, false);
            return new MatchViewHolder(binding);
        } else {
            return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MatchViewHolder) {
            MatchViewHolder matchHolder = (MatchViewHolder) holder;
            if (position < mMatchProjections.size()) {
                matchHolder.bindMatch(mMatchProjections.get(position));
            }
            matchHolder.mBinding.executePendingBindings();
        }
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = super.getItemViewType(position);
        if (viewType != INVALID_VIEW_TYPE) {
            return viewType;
        } else {
            return MATCH_ITEM_VIEW_TYPE;
        }
    }



    @Override
    public int getActualItemCount() {
        return mMatchProjections.size();
    }

    public void setMatches(List<MatchProjection> matches) {
        if (matches != null) {
            mMatchProjections = matches;
            notifyDataSetChanged();
        }
    }

    public class MatchViewHolder extends RecyclerView.ViewHolder {

        private ItemMatchBinding mBinding;

        MatchViewHolder(ItemMatchBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bindMatch(MatchProjection match) {
            MatchesItemViewModel viewModel = mBinding.getMatchItemViewModel();
            if (viewModel != null) {
                viewModel.setMatch(match, mUser);
            } else {
                viewModel = new MatchesItemViewModel(match, mUser);
                mBinding.setMatchItemViewModel(viewModel);
            }
        }


    }
}
