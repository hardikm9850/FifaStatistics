package com.example.kevin.fifastatistics.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.kevin.fifastatistics.FifaApplication;
import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.databinding.ItemPendingUpdateBinding;
import com.example.kevin.fifastatistics.event.ColorChangeEvent;
import com.example.kevin.fifastatistics.event.EventBus;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchUpdate;
import com.example.kevin.fifastatistics.utils.CollectionUtils;
import com.example.kevin.fifastatistics.viewmodels.PendingUpdateItemViewModel;

import java.util.List;

public class PendingUpdatesAdapter extends RecyclerView.Adapter<PendingUpdatesAdapter.PendingUpdateViewHolder> {

    private List<MatchUpdate> mUpdates;
    private ActivityLauncher mLauncher;
    private int mButtonColor;

    public PendingUpdatesAdapter(List<MatchUpdate> updates, ActivityLauncher launcher) {
        mUpdates = updates;
        mLauncher = launcher;
        mButtonColor = FifaApplication.getAccentColor();
        subscribeToColorChangeEvents();
    }

    private void subscribeToColorChangeEvents() {
        EventBus.getInstance().observeEvents(ColorChangeEvent.class).subscribe(event -> {
            mButtonColor = event.color;
            notifyDataSetChanged();
        });
    }

    public void setUpdates(List<MatchUpdate> updates) {
        mUpdates = updates;
        notifyDataSetChanged();
    }

    public boolean removeUpdate(MatchUpdate update) {
        int indexRemoved = CollectionUtils.remove(mUpdates, update);
        if (indexRemoved >= 0) {
            notifyItemRemoved(indexRemoved);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public PendingUpdateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemPendingUpdateBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_pending_update, parent, false);
        return new PendingUpdateViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(PendingUpdateViewHolder holder, int position) {
        if (position < CollectionUtils.getSize(mUpdates)) {
            holder.bindUpdate(mUpdates.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return CollectionUtils.getSize(mUpdates);
    }

    public class PendingUpdateViewHolder extends RecyclerView.ViewHolder {

        private ItemPendingUpdateBinding mBinding;

        PendingUpdateViewHolder(ItemPendingUpdateBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bindUpdate(MatchUpdate update) {
            PendingUpdateItemViewModel viewModel = mBinding.getViewModel();
            if (viewModel != null) {
                viewModel.setUpdate(update);
            } else {
                viewModel = new PendingUpdateItemViewModel(update, mLauncher, mButtonColor);
                mBinding.setViewModel(viewModel);
            }
        }
    }
}
