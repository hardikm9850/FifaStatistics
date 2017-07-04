package com.example.kevin.fifastatistics.adapters;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.databinding.ItemSeriesBinding;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.models.databasemodels.match.SeriesProjection;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.viewmodels.EventViewModel;
import com.example.kevin.fifastatistics.viewmodels.SeriesItemViewModel;

public class SeriesStreamAdapter extends EventStreamRecyclerViewAdapter<ItemSeriesBinding, SeriesProjection> {

    private ActivityLauncher mLauncher;

    public SeriesStreamAdapter(Player user, ActivityLauncher launcher) {
        super(user, R.layout.item_series);
        mLauncher = launcher;
    }

    @Override
    protected EventViewModel<SeriesProjection> getBindingViewModel(ItemSeriesBinding binding) {
        return binding.getSeriesItemViewModel();
    }

    @Override
    protected void bindNewEventViewModel(ItemSeriesBinding binding, SeriesProjection event, Player user) {
        SeriesItemViewModel viewModel = new SeriesItemViewModel(event, user, mLauncher);
        binding.setSeriesItemViewModel(viewModel);
    }
}
