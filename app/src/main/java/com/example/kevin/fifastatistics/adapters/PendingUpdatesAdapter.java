package com.example.kevin.fifastatistics.adapters;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.databinding.ItemPendingUpdateBinding;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchUpdate;
import com.example.kevin.fifastatistics.viewmodels.item.PendingUpdateItemViewModel;

import java.util.List;

public class PendingUpdatesAdapter extends
        AbstractCardAdapter<MatchUpdate, ItemPendingUpdateBinding, PendingUpdateItemViewModel> {

    public PendingUpdatesAdapter(List<MatchUpdate> updates, ActivityLauncher launcher) {
        super(updates, launcher, R.layout.item_pending_update);
    }

    @Override
    protected PendingUpdateItemViewModel getBindingViewModel(ItemPendingUpdateBinding binding) {
        return binding.getViewModel();
    }

    @Override
    protected PendingUpdateItemViewModel createViewModel(MatchUpdate item, ActivityLauncher launcher, boolean isLastItem, int color) {
        return new PendingUpdateItemViewModel(item, launcher, color, isLastItem);
    }
}
