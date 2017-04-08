package com.example.kevin.fifastatistics.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.adapters.EventStreamRecyclerViewAdapter;
import com.example.kevin.fifastatistics.databinding.FragmentEventStreamBinding;
import com.example.kevin.fifastatistics.interfaces.AdapterInteraction;
import com.example.kevin.fifastatistics.interfaces.OnBackPressedHandler;
import com.example.kevin.fifastatistics.interfaces.OnFilterCreatedListener;
import com.example.kevin.fifastatistics.listeners.EndlessRecyclerViewScrollListener;
import com.example.kevin.fifastatistics.models.ApiListResponse;
import com.example.kevin.fifastatistics.models.databasemodels.match.FifaEvent;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.utils.ToastUtils;
import com.example.kevin.fifastatistics.viewmodels.EventStreamFragmentViewModel;
import com.tonicartos.superslim.LayoutManager;

import java.util.List;
import java.util.Map;

import rx.Observable;

public abstract class EventStreamFragment<EVENT extends FifaEvent, ADAPTER extends EventStreamRecyclerViewAdapter<?, EVENT>>
        extends FifaBaseFragment implements EventStreamFragmentViewModel.OnEventsLoadedListener<EVENT>,
        OnBackPressedHandler, AdapterInteraction, OnFilterCreatedListener {

    protected Player mUser;
    private EventStreamFragmentViewModel<EVENT> mViewModel;
    private ADAPTER mAdapter;
    private FragmentEventStreamBinding mBinding;
    private RecyclerView mRecyclerView;
    private int mLoadingFailedMessage;

    public static <T extends EventStreamFragment> T newInstance(Class<T> fragmentClass, Player user, @StringRes int loadingFailedMessage) {
        T fragment = instantiateFragment(fragmentClass);
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        args.putInt(ARG_ERROR, loadingFailedMessage);
        fragment.setArguments(args);
        return fragment;
    }

    private static <T extends EventStreamFragment> T instantiateFragment(Class<T> fragmentClass) {
        try {
            return fragmentClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("failed to instantiate fragment: " + e.getMessage());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = (Player) getArguments().getSerializable(ARG_USER);
        mLoadingFailedMessage = getArguments().getInt(ARG_ERROR);
        mViewModel = new EventStreamFragmentViewModel<>(this, this, this::getLoadMoreObservable);
        setHasOptionsMenu(true);
    }

    public abstract Observable<ApiListResponse<EVENT>> getLoadMoreObservable(String nextUri);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_stream, container, false);
        mBinding.setProgressViewModel(mViewModel);
        mBinding.executePendingBindings();
        mRecyclerView = mBinding.matchesRecyclerview;
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initRecyclerView();
    }

    private void initRecyclerView() {
        mAdapter = getAdapter(mUser);
        LayoutManager manager = new LayoutManager(mBinding.getRoot().getContext());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(manager, ((page, count) -> {
            mViewModel.loadMore(page);
        })));
    }

    protected abstract ADAPTER getAdapter(Player user);

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel.loadEvents(getLoadEventsObservable());
    }

    protected abstract Observable<ApiListResponse<EVENT>> getLoadEventsObservable();

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
        inflater.inflate(R.menu.menu_event_stream, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.filter) {
            showFilterDialog();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void showFilterDialog() {
        DialogFragment dialog = getFilterDialogFragment();
        dialog.setTargetFragment(this, 0);
        dialog.show(getFragmentManager(), null);
    }

    protected abstract DialogFragment getFilterDialogFragment();

    @Override
    public void onEventsLoadSuccess(List<EVENT> matches) {
        mAdapter.setEvents(matches);
    }

    @Override
    public void onEventsLoadFailure() {
        ToastUtils.showShortToast(getActivity(), getString(mLoadingFailedMessage));
    }

    @Override
    public void notifyLoadingItems() {
        mAdapter.notifyLoadingMoreItems();
    }

    @Override
    public void notifyItemsInserted(int startPosition, int numberOfItems) {
        mAdapter.notifyEventsAdded(startPosition, numberOfItems);
    }

    @Override
    public void notifyNoMoreItemsToLoad() {
        mAdapter.notifyNoMoreItemsToLoad();
    }

    @Override
    public void onFilterCreated(Map<String, String> queryFilter) {
        mViewModel.showProgressBar();
        mViewModel.loadEvents(getFilterObservable(queryFilter));
    }

    protected abstract Observable<ApiListResponse<EVENT>> getFilterObservable(Map<String, String> queryFilter);

    @Override
    public boolean handleBackPress() {
        return false;
    }
}
