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
import com.example.kevin.fifastatistics.models.SearchAdapterSearchViewPair;
import com.example.kevin.fifastatistics.models.apiresponses.ApiListResponse;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.network.FifaApiAdapter;
import com.example.kevin.fifastatistics.utils.externalfactories.SearchViewFactory;
import com.example.kevin.fifastatistics.views.GridRecyclerView;
import com.example.kevin.fifastatistics.views.adapters.FriendsRecyclerViewAdapter;

import com.lapism.searchview.view.SearchView;

import java.util.ArrayList;

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

    private static final String TAG = "Friends Fragment";
    private static final int mColumnCount = 2;

    private SearchView mSearchView;
    private OnListFragmentInteractionListener mListener;
    private User mUser;
    private View view = null;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friends_list, container, false);
        mSearchView = (SearchView) getActivity().findViewById(R.id.searchView);

        beginSearchViewInitialization();
        setAdapterDataSource();
        return view;
    }

    private void beginSearchViewInitialization() {
        FifaApiAdapter.getService().getUsers()
                .map(ApiListResponse::getItems)
                .flatMap(this::initializeSearchView)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setSearchAdapterAndInvalidateMenu);
    }

    private Observable<SearchAdapterSearchViewPair> initializeSearchView(ArrayList<User> users) {
        SearchAdapterSearchViewPair pair = SearchViewFactory.createUserSearchViewPair(this, users);
        return Observable.just(pair);
    }

    private void setSearchAdapterAndInvalidateMenu(SearchAdapterSearchViewPair pair) {
        mSearchView = pair.getSearchView();
        mSearchView.setAdapter(pair.getAdapter());

        searchItemIsReady = true;
        getActivity().invalidateOptionsMenu();
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
        view = null;
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
    public boolean handledBackPress() {
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

    private void setAdapter(ArrayList<Friend> friends) {
        if (view instanceof GridRecyclerView) {
            GridRecyclerView recyclerView = (GridRecyclerView) view;
            FriendsRecyclerViewAdapter adapter = new FriendsRecyclerViewAdapter(friends, mListener);

            recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), mColumnCount));
            recyclerView.setAdapter(adapter);
        }
    }
}
