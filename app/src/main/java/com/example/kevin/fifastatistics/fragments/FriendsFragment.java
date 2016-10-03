package com.example.kevin.fifastatistics.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.activities.FifaActivity;
import com.example.kevin.fifastatistics.managers.RetrievalManager;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.views.adapters.FriendsRecyclerViewAdapter;
import com.example.kevin.fifastatistics.views.wrappers.FifaSearchView;

import java.util.List;

import it.gmariotti.recyclerview.adapter.SlideInBottomAnimatorAdapter;

/**
 * A fragment representing a list of players.
 * <p>
 * Activities containing this fragment MUST implement the {@link FriendsFragmentInteractionListener}
 * interface.
 */
public class FriendsFragment extends Fragment implements FifaActivity.OnBackPressedHandler {

    public static final String viewArgument = "view";
    public static final int friendsView = 0;
    public static final int requestsView = 1;

    private static final int mColumnCount = 2;

    private FifaSearchView mSearchView;
    private FriendsFragmentInteractionListener mListener;
    private View mView = null;
    private boolean mIsSearchViewReady = false;

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
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_friends, container, false);

        RetrievalManager.getCurrentUser().subscribe(user -> {
            initializeSearchView(user);
            setAdapterDataSource(user);
        });
        return mView;
    }

    private void initializeSearchView(User user) {
        FifaSearchView.getInstance((FifaActivity) getActivity(), user).subscribe(sv -> {
            if (sv != null) {
                mSearchView = sv;
                mSearchView.attachAdapter();
                mIsSearchViewReady = true;
                getActivity().invalidateOptionsMenu();
            }
        });
    }

    private void setAdapterDataSource(User user) {
        int viewToShow = getArguments().getInt(viewArgument, 0);
        if (viewToShow == friendsView) {
            setAdapter(user.getFriends());
        } else {
            setAdapter(user.getIncomingRequests());
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
        if (context instanceof FriendsFragmentInteractionListener) {
            mListener = (FriendsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FriendsFragmentInteractionListener");
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

    public interface FriendsFragmentInteractionListener {
        void onFriendsFragmentInteraction(Friend friend);
    }

    private void setAdapter(List<Friend> friends) {
        if (mView instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) mView;
            FriendsRecyclerViewAdapter adapter = new FriendsRecyclerViewAdapter(friends, mListener);
            SlideInBottomAnimatorAdapter animatorAdapter = new SlideInBottomAnimatorAdapter(adapter, recyclerView);
            recyclerView.setLayoutManager(new GridLayoutManager(mView.getContext(), mColumnCount));
            recyclerView.setAdapter(animatorAdapter);
        }
    }
}
