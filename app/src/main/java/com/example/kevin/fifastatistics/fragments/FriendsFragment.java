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
import android.widget.TextView;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.activities.MainActivity;
import com.example.kevin.fifastatistics.models.apiresponses.UserListResponse;
import com.example.kevin.fifastatistics.models.user.Friend;
import com.example.kevin.fifastatistics.models.user.User;
import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.network.FifaApiAdapter;
import com.example.kevin.fifastatistics.views.adapters.FriendsRecyclerViewAdapter;


import com.example.kevin.fifastatistics.views.adapters.SearchAdapter;
import com.lapism.searchview.adapter.SearchItem;
import com.lapism.searchview.view.SearchCodes;
import com.lapism.searchview.view.SearchView;

import java.util.ArrayList;
import java.util.List;

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

    private static final int mColumnCount = 2;
    private static final String TAG = "Friends Fragment";
    private static final String REQUESTS = "Requests";
    private static SearchView mSearchView;
    private SearchAdapter mSearchAdapter;

    private OnListFragmentInteractionListener mListener;

    private RecyclerView recyclerView;
    private FriendsRecyclerViewAdapter adapter;
    private ArrayList<Friend> friends;
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

    private void setSearchAdapterAndInvalidateMenu()
    {
        mSearchView.setAdapter(mSearchAdapter);
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
        Log.i(TAG, "Destroying in friends fragment!!!!!");
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
            Log.i(TAG, "showing search view!!!!");
            mSearchView.show(true);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public static void closeSearchView()
    {
        if (mSearchView.isSearchOpen()) {
            mSearchView.hide(true);
        }
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Friend friend);
    }

    private Observable<Boolean> initializeSearchView(ArrayList<User> users)
    {
        mSearchView.setStyle(SearchCodes.STYLE_MENU_ITEM_CLASSIC);
        mSearchView.setVersion(SearchCodes.VERSION_MENU_ITEM);
        mSearchView.setTheme(SearchCodes.THEME_LIGHT);
        mSearchView.setDivider(true);
        mSearchView.setAnimationDuration(300);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                mSearchView.hide(false);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                return false;
            }
        });
        mSearchView.setOnSearchViewListener(new SearchView.SearchViewListener()
        {
            @Override
            public void onSearchViewShown() {
                Log.i(TAG, "Showing search view");
                MainActivity.lockNavigationDrawer();
            }

            @Override
            public void onSearchViewClosed() {
                MainActivity.unlockNavigationDrawer();
            }
        });

        List<SearchItem> mResultsList = new ArrayList<>();
        List<SearchItem> mSuggestionsList = new ArrayList<>();
        mSearchAdapter = new SearchAdapter(getContext(), mResultsList, mSuggestionsList, SearchCodes.THEME_LIGHT, users);
        mSearchAdapter.setOnItemClickListener((view, position) -> {
                mSearchView.hide(false);
                TextView textView = (TextView) view.findViewById(R.id
                        .textView_item_text);
            });

        mSearchView.setVoice(false);
        mSearchView.setHint("Search Users");
        searchItemIsReady = true;

        return Observable.just(true);
    }


    private void setAdapter(ArrayList<Friend> users)
    {
        friends = users;
        if (view instanceof RecyclerView)
        {
            recyclerView = (RecyclerView) view;
            adapter = new FriendsRecyclerViewAdapter(friends, mListener);

            recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), mColumnCount));
            recyclerView.setAdapter(adapter);
        }
    }
}
