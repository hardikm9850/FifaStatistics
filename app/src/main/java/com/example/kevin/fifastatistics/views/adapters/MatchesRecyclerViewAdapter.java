package com.example.kevin.fifastatistics.views.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.databinding.ItemMatchBinding;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchProjection;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.viewmodel.MatchesItemViewModel;

import java.util.ArrayList;
import java.util.List;

public class MatchesRecyclerViewAdapter extends RecyclerView.Adapter<MatchesRecyclerViewAdapter.ViewHolder> {

    private List<MatchProjection> mMatchProjections;
    private Player mUser;

    public MatchesRecyclerViewAdapter(Player user) {
        mMatchProjections = new ArrayList<>();
        mUser = user;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemMatchBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_match, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position < mMatchProjections.size()) {
            holder.bindMatch(mMatchProjections.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mMatchProjections.size();
    }

    public void setMatches(List<MatchProjection> matches) {
        mMatchProjections = matches;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemMatchBinding mBinding;

        public ViewHolder(ItemMatchBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bindMatch(MatchProjection match) {
            MatchesItemViewModel viewModel = mBinding.getMatchItemViewModel();
            if (viewModel != null) {
                viewModel.setMatch(match, mUser);
            } else {
                viewModel = new MatchesItemViewModel(match, mUser);
                mBinding.setMatchItemViewModel(viewModel);
            }
            mBinding.executePendingBindings();
        }
    }
}
