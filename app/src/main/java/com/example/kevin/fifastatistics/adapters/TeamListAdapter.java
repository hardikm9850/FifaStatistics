package com.example.kevin.fifastatistics.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.databinding.ItemTeamBinding;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;
import com.example.kevin.fifastatistics.utils.CollectionUtils;
import com.example.kevin.fifastatistics.viewmodels.TeamItemViewModel;

import java.util.List;

public class TeamListAdapter extends RecyclerView.Adapter<TeamListAdapter.TeamViewHolder> {

    private List<Team> mTeams;
    private TeamItemViewModel.OnTeamClickListener mListener;

    public TeamListAdapter(TeamItemViewModel.OnTeamClickListener listener) {
        mListener = listener;
    }

    @Override
    public TeamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemTeamBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_team, parent, false);
        return new TeamViewHolder(binding, mListener);
    }

    @Override
    public void onBindViewHolder(TeamViewHolder holder, int position) {
        if (position < CollectionUtils.getSize(mTeams)) {
            holder.bindTeam(mTeams.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return CollectionUtils.getSize(mTeams);
    }

    public void setTeams(List<Team> teams) {
        mTeams = teams;
        notifyDataSetChanged();
    }

    static class TeamViewHolder extends RecyclerView.ViewHolder {

        private ItemTeamBinding mBinding;
        private TeamItemViewModel.OnTeamClickListener mClickListener;

        TeamViewHolder(ItemTeamBinding binding, TeamItemViewModel.OnTeamClickListener listener) {
            super(binding.getRoot());
            mBinding = binding;
            mClickListener = listener;
        }

        void bindTeam(Team team) {
            TeamItemViewModel viewModel = mBinding.getViewModel();
            if (viewModel == null) {
                viewModel = new TeamItemViewModel(team, mClickListener);
            } else {
                viewModel.setTeam(team);
            }
            mBinding.setViewModel(viewModel);
        }
    }
}
