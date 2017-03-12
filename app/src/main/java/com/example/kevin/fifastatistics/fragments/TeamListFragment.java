package com.example.kevin.fifastatistics.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.adapters.TeamListAdapter;
import com.example.kevin.fifastatistics.databinding.FragmentTeamsBinding;
import com.example.kevin.fifastatistics.models.databasemodels.league.League;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;
import com.example.kevin.fifastatistics.utils.ColorUtils;
import com.example.kevin.fifastatistics.viewmodels.TeamItemViewModel;
import com.example.kevin.fifastatistics.viewmodels.TeamListFragmentViewModel;

import java.util.List;

public class TeamListFragment extends FifaBaseFragment implements TeamListFragmentViewModel.OnTeamsLoadedListener {

    private static final String ARG_LEAGUE = "league";

    private TeamListFragmentViewModel mViewModel;
    private TeamListAdapter mAdapter;
    private FragmentTeamsBinding mBinding;
    private TeamItemViewModel.OnTeamClickListener mTeamClickListener;

    public static TeamListFragment newInstance(League league) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_LEAGUE, league);
        TeamListFragment fragment = new TeamListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TeamItemViewModel.OnTeamClickListener) {
            mTeamClickListener = (TeamItemViewModel.OnTeamClickListener) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        League league = (League) getArguments().getSerializable(ARG_LEAGUE);
        mViewModel = new TeamListFragmentViewModel(this, league);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_teams, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ColorUtils.setProgressBarColor(mBinding.progress, mColor);
        initRecyclerView();
    }

    private void initRecyclerView() {
        mAdapter = new TeamListAdapter(mTeamClickListener);
        mBinding.setProgressViewModel(mViewModel);
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel.loadTeams();
    }

    @Override
    public void onStop() {
        mViewModel.unsubscribeAll();
        super.onStop();
    }

    @Override
    public void onTeamsLoaded(List<Team> teams) {
        if (mAdapter != null) {
            mAdapter.setTeams(teams);
        }
    }

    @Override
    public void onTeamsLoadFailure() {
        // TODO
    }
}
