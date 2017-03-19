package com.example.kevin.fifastatistics.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.databinding.FragmentFriendsItemBinding;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.viewmodels.FriendsItemViewModel;

import java.util.List;

public class FriendsRecyclerViewAdapter extends RecyclerView.Adapter<FriendsRecyclerViewAdapter.PlayerItemViewHolder> {

    private List<? extends Player> mUsers;
    private ActivityLauncher mLauncher;

    public FriendsRecyclerViewAdapter(List<? extends Player> users, ActivityLauncher launcher) {
        mUsers = users;
        mLauncher = launcher;
    }

    @Override
    public PlayerItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FragmentFriendsItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.fragment_friends_item, parent, false);
        return new PlayerItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final PlayerItemViewHolder holder, int position) {
        if (mUsers != null && position < mUsers.size()) {
            holder.bindPlayer(mUsers.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mUsers == null ? 0 : mUsers.size();
    }

    public class PlayerItemViewHolder extends RecyclerView.ViewHolder {

        private FragmentFriendsItemBinding mBinding;

        PlayerItemViewHolder(FragmentFriendsItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bindPlayer(Player player) {
            FriendsItemViewModel viewModel = mBinding.getViewModel();
            if (viewModel != null) {
                viewModel.setFriend(player);
            } else {
                viewModel = new FriendsItemViewModel(player, mLauncher);
                mBinding.setViewModel(viewModel);
            }
        }
    }
}
