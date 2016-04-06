package com.example.kevin.fifastatistics.fragments.friendsfragment;

import android.content.Context;
import android.graphics.Color;
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

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.activities.MainActivity;
import com.example.kevin.fifastatistics.models.user.Friend;
import com.example.kevin.fifastatistics.models.user.User;
import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

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
    private static MaterialSearchView mSearchView;

    private OnListFragmentInteractionListener mListener;

    private User mUser;
    private View view = null;

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

        SharedPreferencesManager handler = SharedPreferencesManager.getInstance(getContext());
        mUser = handler.getUser();

        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_friends_list, container, false);
        mSearchView = (MaterialSearchView) getActivity().findViewById(R.id.search_view);
        initializeSearchView();

        int viewToShow = getArguments().getInt(viewArgument, 0);
        if (viewToShow == friendsView) {
            setAdapter(mUser.getFriends());
        }
        else {
            setAdapter(mUser.getIncomingRequests());
        }
        return view;
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
                mSearchView.closeSearch();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        Log.i(TAG, "Creating options menu from fragment!");
        inflater.inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setVisible(true);
        mSearchView.setMenuItem(searchItem);

        menu.findItem(R.id.friend_requests).setVisible(false);
    }

    public static void closeSearchView()
    {
        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
        }
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Friend friend);
    }

    // TODO do in background
    private void initializeSearchView() {
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        Log.i(TAG, "Setting on search view listener####");
        mSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                MainActivity.lockNavigationDrawer();
                Log.i(TAG, "Showing Search View!");
            }

            @Override
            public void onSearchViewClosed() {
                MainActivity.unlockNavigationDrawer();
                Log.i(TAG, "Closing Search View!");
            }
        });

        mSearchView.setVoiceSearch(false);
        mSearchView.setHint("Search Users");
        mSearchView.setHintTextColor(Color.LTGRAY);
    }


    private void setAdapter(ArrayList<Friend> users)
    {
        if (view instanceof RecyclerView)
        {
            RecyclerView recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), mColumnCount));
            recyclerView.setAdapter(new MyFriendsRecyclerViewAdapter(users, mListener));
        }
    }
}
