package com.example.kevin.fifastatistics.adapters;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.databinding.ItemMatchBinding;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchProjection;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.viewmodels.item.EventViewModel;
import com.example.kevin.fifastatistics.viewmodels.item.MatchesItemViewModel;

public class MatchStreamRecyclerViewAdapter extends EventStreamRecyclerViewAdapter<ItemMatchBinding, MatchProjection> {

    private ActivityLauncher mLauncher;

    public MatchStreamRecyclerViewAdapter(Player user, ActivityLauncher launcher) {
        super(user, R.layout.item_match);
        mLauncher = launcher;
    }

    @Override
    protected EventViewModel<MatchProjection> getBindingViewModel(ItemMatchBinding binding) {
        return binding.getMatchItemViewModel();
    }

    @Override
    protected void bindNewEventViewModel(ItemMatchBinding binding, MatchProjection event, Player user) {
        MatchesItemViewModel viewModel = new MatchesItemViewModel(event, user, mLauncher);
        binding.setMatchItemViewModel(viewModel);
    }
}
