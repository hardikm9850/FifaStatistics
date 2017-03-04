package com.example.kevin.fifastatistics.adapters;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.databinding.ItemMatchBinding;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchProjection;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.viewmodels.EventViewModel;
import com.example.kevin.fifastatistics.viewmodels.MatchesItemViewModel;

public class MatchStreamRecyclerViewAdapter extends EventStreamRecyclerViewAdapter<ItemMatchBinding, MatchProjection> {

    public MatchStreamRecyclerViewAdapter(Player user) {
        super(user, R.layout.item_match);
    }

    @Override
    protected EventViewModel<MatchProjection> getBindingViewModel(ItemMatchBinding binding) {
        return binding.getMatchItemViewModel();
    }

    @Override
    protected void bindNewEventViewModel(ItemMatchBinding binding, MatchProjection event, Player user) {
        MatchesItemViewModel viewModel = new MatchesItemViewModel(event, user);
        binding.setMatchItemViewModel(viewModel);
    }
}
