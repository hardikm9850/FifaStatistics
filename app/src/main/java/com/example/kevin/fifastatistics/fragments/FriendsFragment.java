package com.example.kevin.fifastatistics.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.models.apiresponses.UserListResponse;
import com.example.kevin.fifastatistics.models.user.Friend;
import com.example.kevin.fifastatistics.models.user.User;
import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.network.FifaApiAdapter;
import com.example.kevin.fifastatistics.utils.factories.SearchViewFactory;
import com.example.kevin.fifastatistics.views.GridRecyclerView;
import com.example.kevin.fifastatistics.views.adapters.FriendsRecyclerViewAdapter;
import com.example.kevin.fifastatistics.views.adapters.SearchAdapter;

import com.lapism.searchview.view.SearchView;

import java.util.ArrayList;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A fragment representing a list of users.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FriendsFragment extends Fragment {

    public static final String viewArgument = "view";
    public static final int friendsView = 0;
    public static final int requestsView = 1;

    private static final String TAG = "Friends Fragment";
    private static final int mColumnCount = 2;

    private static SearchView mSearchView;
    private static SearchAdapter mSearchAdapter;

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
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getUser();
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    private void getUser()
    {
        SharedPreferencesManager handler = SharedPreferencesManager.getInstance(getContext());
        mUser = handler.getUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_friends_list, container, false);
        mSearchView = (SearchView) getActivity().findViewById(R.id.searchView);

        FifaApiAdapter.getService().getUsers()
                .map(UserListResponse::getUsers)
                .flatMap(this::initializeSearchView)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(b -> setSearchAdapterAndInvalidateMenu());

        setAdapterDataSource();

        return view;
    }

    private Observable<Boolean> initializeSearchView(ArrayList<User> users)
    {
        mSearchView = SearchViewFactory.createUserSearchView(this, users);
        return Observable.just(true);
    }

    private void setSearchAdapterAndInvalidateMenu()
    {
        mSearchView.setAdapter(mSearchAdapter);

        searchItemIsReady = true;
        getActivity().invalidateOptionsMenu();
    }

    private void setAdapterDataSource()
    {
        int viewToShow = getArguments().getInt(viewArgument, 0);
        if (viewToShow == friendsView) {
            setAdapter(mUser.getFriends());
        }
        else {
            setAdapter(mUser.getIncomingRequests());
        }
    }

    @Override
    public void onDestroyView()
    {
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
    public void onPause()
    {
        super.onPause();
        if (mSearchView != null) {
            if (mSearchView.isSearchOpen()) {
                mSearchView.hide(false);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        Log.i(TAG, "Creating options menu from fragment!");
        inflater.inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setVisible(searchItemIsReady);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.action_search) {
            mSearchView.show(true);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public static boolean isSearchOpen()
    {
        return mSearchView.isSearchOpen();
    }

    public static void closeSearchView()
    {
        mSearchView.hide(true);
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Friend friend);
    }

    private void setAdapter(ArrayList<Friend> friends)
    {
        if (view instanceof GridRecyclerView) {
            GridRecyclerView recyclerView = (GridRecyclerView) view;
            FriendsRecyclerViewAdapter adapter =
                    new FriendsRecyclerViewAdapter(friends, mListener);

            recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), mColumnCount));
            recyclerView.setAdapter(adapter);
        }
    }

    public void setSearchAdapter(SearchAdapter adapter)
    {
        mSearchAdapter = adapter;
    }
}
