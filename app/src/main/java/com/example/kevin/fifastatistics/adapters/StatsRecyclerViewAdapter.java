package com.example.kevin.fifastatistics.adapters;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.kevin.fifastatistics.FifaApplication;
import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.databinding.StatsListItemBinding;
import com.example.kevin.fifastatistics.event.ColorChangeEvent;
import com.example.kevin.fifastatistics.event.EventBus;
import com.example.kevin.fifastatistics.models.databasemodels.match.TeamEvent;
import com.example.kevin.fifastatistics.models.databasemodels.user.Stats;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.viewmodels.item.StatItemViewModel;

public class StatsRecyclerViewAdapter extends RecyclerView.Adapter<StatsRecyclerViewAdapter.ViewHolder> {

    private static final int DEFAULT_RIGHT_COLOR;

    static {
        DEFAULT_RIGHT_COLOR = ContextCompat.getColor(FifaApplication.getContext(), R.color.statOpponentColor);
    }

    private float[] valuesFor;
    private float[] valuesAgainst;
    private String[] names;
    private TeamEvent mEvent;
    private boolean doShowDecimal;
    private int mLeftColor;
    private int mRightColor;

    public StatsRecyclerViewAdapter(User.StatsPair statsPair, boolean doShowDecimal, TeamEvent event) {
        this.valuesFor = statsPair.getStatsFor().buildValueSet();
        this.valuesAgainst = statsPair.getStatsAgainst().buildValueSet();
        this.doShowDecimal = doShowDecimal;
        mEvent = event;
        names = Stats.getNameSet();
        initColor();
    }

    private void initColor() {
        if (mEvent != null) {
            mRightColor = Color.parseColor(mEvent.getTeamLoser().getColor());
            mLeftColor = Color.parseColor(mEvent.getTeamWinner().getColor());
        } else {
            mRightColor = DEFAULT_RIGHT_COLOR;
            mLeftColor = FifaApplication.getAccentColor();
            EventBus.getInstance().observeEvents(ColorChangeEvent.class).subscribe(event -> {
                mLeftColor = event.color;
                notifyDataSetChanged();
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        StatsListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.stats_list_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        float vf = valuesFor[position];
        float va = valuesAgainst[position];
        String title = names[position];
        holder.bind(vf, va, mLeftColor, mRightColor, title, doShowDecimal);
    }

    @Override
    public int getItemCount() {
        return valuesFor.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private StatsListItemBinding mBinding;

        ViewHolder(StatsListItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bind(float leftVal, float rightVal, int leftColor, int rightColor, String title, boolean showDecimal) {
            StatItemViewModel viewModel = mBinding.getViewModel();
            if (viewModel != null) {
                viewModel.init(leftVal, rightVal, leftColor, rightColor, title);
            } else {
                viewModel = new StatItemViewModel(leftVal, rightVal, leftColor, rightColor, title, showDecimal);
                mBinding.setViewModel(viewModel);
            }
            mBinding.executePendingBindings();
        }
    }
}
