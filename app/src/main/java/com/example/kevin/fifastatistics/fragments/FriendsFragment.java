package com.example.kevin.fifastatistics.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.activities.FifaBaseActivity;
import com.example.kevin.fifastatistics.adapters.FriendsRecyclerViewAdapter;
import com.example.kevin.fifastatistics.databinding.FragmentFriendsBinding;
import com.example.kevin.fifastatistics.interfaces.OnBackPressedHandler;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.utils.ToastUtils;
import com.example.kevin.fifastatistics.utils.UiUtils;
import com.example.kevin.fifastatistics.viewmodels.PlayersFragmentViewModel;
import com.example.kevin.fifastatistics.views.FifaSearchView;

import java.util.List;

import it.gmariotti.recyclerview.adapter.SlideInBottomAnimatorAdapter;

public class FriendsFragment extends FifaBaseFragment implements OnBackPressedHandler, PlayersFragmentViewModel.OnPlayersLoadedListener {

    private static final int PORTRAIT_COLUMN_COUNT = 2;
    private static final int LANDSCAPE_COLUMN_COUNT = 3;

    private FifaSearchView mSearchView;
    private RecyclerView mRecyclerView;
    private PlayersFragmentViewModel mViewModel;
    private int mColumnCount;
    private boolean mIsSearchViewReady = false;

    public static FriendsFragment newInstance() {
        return new FriendsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentFriendsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_friends, container, false);
        mRecyclerView = binding.list;
        mViewModel = new PlayersFragmentViewModel(this);
        binding.setProgressViewModel(mViewModel);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getColumns();
        mViewModel.loadPlayers();
    }

    private void getColumns() {
        mColumnCount = UiUtils.isLandscape(getContext()) ? LANDSCAPE_COLUMN_COUNT : PORTRAIT_COLUMN_COUNT;
    }

    @Override
    public void onPlayersLoaded(List<User> players) {
        initializeSearchView(players);
        setAdapter(players);
    }

    @Override
    public void onPlayersLoadFailed() {
        ToastUtils.showShortToast(getActivity(), R.string.error_loading_players);
    }

    private void initializeSearchView(List<User> users) {
        mSearchView = FifaSearchView.getInstance((FifaBaseActivity) getActivity(), users);
        mIsSearchViewReady = true;
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onDestroyView() {
        mSearchView = null;
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSearchView != null && mSearchView.isSearchOpen()) {
            mSearchView.hide(false);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_players, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setVisible(mIsSearchViewReady);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            mSearchView.show(true);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean handleBackPress() {
        if (mSearchView != null && mSearchView.isSearchOpen()) {
            mSearchView.hide(true);
            return true;
        } else {
            return false;
        }
    }

    private void setAdapter(List<? extends Player> players) {
        FriendsRecyclerViewAdapter adapter = new FriendsRecyclerViewAdapter(players, this);
        SlideInBottomAnimatorAdapter<FriendsRecyclerViewAdapter.PlayerItemViewHolder> animatorAdapter =
                new SlideInBottomAnimatorAdapter<>(adapter, mRecyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mRecyclerView.getContext(), mColumnCount));
        mRecyclerView.setAdapter(animatorAdapter);
    }
}
