package com.example.kevin.fifastatistics.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.databinding.FragmentFriendsItemBinding;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.viewmodels.FriendsItemViewModel;

import java.util.List;

public class FriendsRecyclerViewAdapter extends RecyclerView.Adapter<FriendsRecyclerViewAdapter.FriendsItemViewHolder> {

    private List<Friend> mUsers;
    private ActivityLauncher mLauncher;

    public FriendsRecyclerViewAdapter(List<Friend> users, ActivityLauncher launcher) {
        mUsers = users;
        mLauncher = launcher;
    }

    @Override
    public FriendsItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FragmentFriendsItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.fragment_friends_item, parent, false);
        return new FriendsItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final FriendsItemViewHolder holder, int position) {
        if (mUsers != null && position < mUsers.size()) {
            holder.bindFriend(mUsers.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mUsers == null ? 0 : mUsers.size();
    }

    public class FriendsItemViewHolder extends RecyclerView.ViewHolder {

        private FragmentFriendsItemBinding mBinding;

        FriendsItemViewHolder(FragmentFriendsItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bindFriend(Player friend) {
            FriendsItemViewModel viewModel = mBinding.getViewModel();
            if (viewModel != null) {
                viewModel.setFriend(friend);
            } else {
                viewModel = new FriendsItemViewModel(friend, mLauncher);
                mBinding.setViewModel(viewModel);
            }
        }
    }
}
