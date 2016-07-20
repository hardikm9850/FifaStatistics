package com.example.kevin.fifastatistics.views.wrappers;

import android.content.Intent;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.activities.FifaActivity;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.utils.IntentFactory;
import com.example.kevin.fifastatistics.views.adapters.SearchViewAdapter;
import com.lapism.searchview.view.SearchCodes;
import com.lapism.searchview.view.SearchView;

import java.util.List;

/**
 * Wrapper class around {@link com.lapism.searchview.view.SearchView}.
 */
public class FifaSearchView {
    private static final int THEME_TYPE = SearchCodes.THEME_LIGHT;
    private static final int ANIMATION_DURATION = 240;

    private static FifaSearchView mInstance;
    private SearchView mSearchView;
    private SearchViewAdapter mAdapter;

    /**
     * Get the SearchView instance. This method will not attach an adapter to the searchview.
     *
     * @param activity The activity the searchview is in.
     * @param users    The list of users to be displayed by the searchview
     * @return the search view
     */
    public static FifaSearchView getInstance(FifaActivity activity, List<User> users) {
        if (mInstance == null) {
            mInstance = new FifaSearchView(activity, users);
        }
        return mInstance;
    }

    /**
     * Attaches the default adapter to the searchview. Must be called from the UI Thread
     * (attaching the adpater begins filtering).
     */
    public void attachAdapter() {
        mSearchView.setAdapter(mAdapter);
    }

    public void show(boolean animate) {
        mSearchView.show(animate);
    }

    public void hide(boolean animate) {
        mSearchView.hide(animate);
    }

    public boolean isSearchOpen() {
        return mSearchView.isSearchOpen();
    }

    private FifaSearchView(FifaActivity activity, List<User> users) {
        mSearchView = (SearchView) activity.findViewById(R.id.searchView);
        setBasicSearchViewProperties();
        setSearchViewListeners(activity);

        mAdapter = initializeAdapter(activity, users);
    }

    private void setBasicSearchViewProperties() {
        mSearchView.setStyle(SearchCodes.STYLE_MENU_ITEM_CLASSIC);
        mSearchView.setVersion(SearchCodes.VERSION_MENU_ITEM);
        mSearchView.setTheme(THEME_TYPE);
        mSearchView.setDivider(true);
        mSearchView.setAnimationDuration(ANIMATION_DURATION);
        mSearchView.setVoice(false);
        mSearchView.setHint("Search Users");
    }

    private void setSearchViewListeners(FifaActivity activity) {
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchView.hide(false);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mSearchView.setOnSearchViewListener(new SearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                activity.setNavigationLocked(true);
            }

            @Override
            public void onSearchViewClosed() {
                activity.setNavigationLocked(false);
            }
        });
    }

    private SearchViewAdapter initializeAdapter(FifaActivity activity, List<User> users) {
        SearchViewAdapter adapter = new SearchViewAdapter(activity, THEME_TYPE, users);

        adapter.setOnItemClickListener((view, position) -> {
            // TODO determine whether friend or not
            mSearchView.hide(true);
            User user = adapter.getUserAtPosition(position);
            Intent intent = IntentFactory.createPlayerActivityIntent(activity, user);
            activity.startActivity(intent);
        });
        return adapter;
    }
}