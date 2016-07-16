package com.example.kevin.fifastatistics.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.activities.FifaActivity;
import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.models.apiresponses.ApiListResponse;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.network.FifaApiAdapter;
import com.example.kevin.fifastatistics.views.wrappers.FifaSearchView;
import com.example.kevin.fifastatistics.views.GridRecyclerView;
import com.example.kevin.fifastatistics.views.adapters.FriendsRecyclerViewAdapter;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A fragment representing a list of players.
 * <p>
 * Implements the {@link FifaFragment} interface, as all fragments in this project should.
 * <p>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FriendsFragment extends Fragment implements FifaFragment {

    public static final String viewArgument = "view";
    public static final int friendsView = 0;
    public static final int requestsView = 1;

    private static final int mColumnCount = 2;

    private FifaSearchView mSearchView;
    private OnListFragmentInteractionListener mListener;
    private User mUser;
    private View mView = null;
    private boolean searchItemIsReady = false;

    public FriendsFragment() {
    }

    public static FriendsFragment newInstance(int view) {
        FriendsFragment fragment = new FriendsFragment();
        Bundle args = new Bundle();
        args.putInt(viewArgument, view);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUser = SharedPreferencesManager.getUser();
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_friends_list, container, false);

        beginSearchViewInitialization();
        setAdapterDataSource();
        return mView;
    }

    private void beginSearchViewInitialization() {
        FifaApiAdapter.getService().getUsers()
                .map(ApiListResponse::getItems)
                .flatMap(this::initializeSearchView)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sv -> {
                    mSearchView.attachAdapter();
                    searchItemIsReady = true;
                    getActivity().invalidateOptionsMenu();
                });
    }

    private Observable<FifaSearchView> initializeSearchView(List<User> users) {
        users.remove(mUser);
        mSearchView = FifaSearchView.getInstance((FifaActivity) getActivity(), users);
        return Observable.just(mSearchView);
    }

    private void setAdapterDataSource() {
        int viewToShow = getArguments().getInt(viewArgument, 0);
        if (viewToShow == friendsView) {
            setAdapter(mUser.getFriends());
        } else {
            setAdapter(mUser.getIncomingRequests());
        }
    }

    @Override
    public void onDestroyView() {
        mView = null;
        mSearchView = null;
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        inflater.inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setVisible(searchItemIsReady);
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

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Friend friend);
    }

    private void setAdapter(List<Friend> friends) {
        if (mView instanceof GridRecyclerView) {
            GridRecyclerView recyclerView = (GridRecyclerView) mView;
            FriendsRecyclerViewAdapter adapter = new FriendsRecyclerViewAdapter(friends, mListener);

            recyclerView.setLayoutManager(new GridLayoutManager(mView.getContext(), mColumnCount));
            recyclerView.setAdapter(adapter);
        }
    }
}
