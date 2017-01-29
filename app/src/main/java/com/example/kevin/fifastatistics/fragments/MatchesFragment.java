package com.example.kevin.fifastatistics.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.databinding.FragmentMatchesBinding;
import com.example.kevin.fifastatistics.interfaces.OnBackPressedHandler;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchProjection;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.utils.ToastUtils;
import com.example.kevin.fifastatistics.viewmodel.MatchesFragmentViewModel;
import com.example.kevin.fifastatistics.views.adapters.MatchesRecyclerViewAdapter;

import java.util.List;

import it.gmariotti.recyclerview.adapter.AlphaAnimatorAdapter;
import it.gmariotti.recyclerview.adapter.SlideInBottomAnimatorAdapter;

public class MatchesFragment extends FifaBaseFragment implements MatchesFragmentViewModel.OnMatchesLoadedListener, OnBackPressedHandler {

    private MatchesFragmentViewModel mViewModel;
    private MatchesRecyclerViewAdapter mAdapter;
    private FragmentMatchesBinding mBinding;
    private RecyclerView mRecyclerView;
    private Player mUser;

    public static MatchesFragment newInstance(Player user) {
        MatchesFragment fragment = new MatchesFragment();
        fragment.mViewModel = new MatchesFragmentViewModel(fragment, user);
        fragment.mUser = user;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_matches, container, false);
        mBinding.setMatchesViewModel(mViewModel);
        mBinding.executePendingBindings();
        mRecyclerView = mBinding.matchesRecyclerview;
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mAdapter = new MatchesRecyclerViewAdapter(mUser);
        SlideInBottomAnimatorAdapter<MatchesRecyclerViewAdapter.ViewHolder> animatorAdapter = new SlideInBottomAnimatorAdapter<>(mAdapter, mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mBinding.getRoot().getContext()));
        mRecyclerView.setAdapter(animatorAdapter);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel.loadMatches();
    }

    @Override
    public void onStop() {
        mViewModel.unsubscribeAll();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        mViewModel = null;
        super.onDestroyView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO
        return false;
    }

    @Override
    public void onMatchesLoadSuccess(List<MatchProjection> matches) {
        mAdapter.setMatches(matches);
    }

    @Override
    public void onMatchesLoadFailure() {
        ToastUtils.showShortToast(getActivity(), getString(R.string.matches_load_failed));
    }

    @Override
    public boolean handleBackPress() {
        return false;
    }
}
