package com.example.kevin.fifastatistics.views;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.activities.FifaBaseActivity;
import com.example.kevin.fifastatistics.activities.PlayerActivty;
import com.example.kevin.fifastatistics.adapters.SearchAdapter;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.utils.IntentFactory;
import com.example.kevin.fifastatistics.utils.TransitionUtils;
import com.lapism.searchview.SearchView;

import java.util.List;

/**
 * Wrapper class around {@link com.lapism.searchview.SearchView}.
 */
public class FifaSearchView {

    private SearchView mSearchView;
    private SearchAdapter mAdapter;
    private boolean mIsAdapterSet;

    public static FifaSearchView getInstance(FifaBaseActivity activity, List<User> users) {
        return new FifaSearchView(activity, users);
    }

    public void show(boolean animate) {
        mSearchView.open(animate);
    }

    public void hide(boolean animate) {
        mSearchView.close(animate);
    }

    public boolean isSearchOpen() {
        return mSearchView.isSearchOpen();
    }

    private FifaSearchView(FifaBaseActivity activity, List<? extends Player> users) {
        mSearchView = (SearchView) activity.findViewById(R.id.searchView);
        setBasicSearchViewProperties();
        setSearchViewListeners(activity);

        mAdapter = initializeAdapter(activity, users);
        attachAdapter();
    }

    private void attachAdapter() {
        if (!mIsAdapterSet) {
            mIsAdapterSet = true;
            mSearchView.setAdapter(mAdapter);
        }
    }

    private void setBasicSearchViewProperties() {
        mSearchView.setVersion(SearchView.VERSION_MENU_ITEM);
        mSearchView.setVersionMargins(SearchView.VERSION_MARGINS_MENU_ITEM);
        mSearchView.setShouldClearOnClose(true);
        mSearchView.setTheme(SearchView.THEME_LIGHT);
        mSearchView.setVoice(false);
        mSearchView.setHint(R.string.search_players);
    }

    private void setSearchViewListeners(FifaBaseActivity activity) {

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (mSearchView.getAdapter().getItemCount() == 1) {
                    launchPlayerActivity(activity, (SearchAdapter) mSearchView.getAdapter(), null, 0);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private SearchAdapter initializeAdapter(FifaBaseActivity activity, List<? extends Player> players) {
        SearchAdapter adapter = new SearchAdapter(activity, players);
        adapter.addOnItemClickListener((v, p) -> launchPlayerActivity(activity, adapter, v, p));

        return adapter;
    }

    private void launchPlayerActivity(Activity activity, SearchAdapter adapter, View imageView, int position) {
        Player selectedUser = adapter.getPlayerAtPosition(position);
        Intent intent = IntentFactory.createPlayerActivityIntent(activity, selectedUser);
        intent.putExtra(PlayerActivty.EXTRA_ENTERED_FROM_SEARCH_BAR, true);
        ActivityOptionsCompat options = TransitionUtils.getSceneTransitionOptions(activity, imageView, R.string.transition_profile_image);
        activity.startActivityForResult(intent, Activity.RESULT_OK, options.toBundle());
    }
}