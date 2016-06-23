package com.example.kevin.fifastatistics.utils.externalfactories;

import android.support.v4.widget.DrawerLayout;
import android.widget.TextView;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.activities.FifaActivity;
import com.example.kevin.fifastatistics.fragments.FriendsFragment;
import com.example.kevin.fifastatistics.models.SearchAdapterSearchViewPair;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.views.adapters.SearchViewAdapter;
import com.lapism.searchview.view.SearchCodes;
import com.lapism.searchview.view.SearchView;

import java.util.ArrayList;
import java.util.Collections;

public class SearchViewFactory
{
    private static final int THEME_TYPE = SearchCodes.THEME_LIGHT;
    private static final int ANIMATION_DURATION = 240;

    /**
     * Returns a pair consisting of the SearchView and the SearchViewAdapter.
     * <p>
     * A pair of the two is returned rather than just the SearchView with the
     * adapter already set in order to facilitate initialization in a non-UI
     * thread.
     * the setAdapter() method of SearchView includes a call to startFilter(),
     * which must be performed on the UI Thread. Therefore, setting the adapter
     * here and attempting to initialize the SearchView in the background would
     * result in an exception.
     */
    public static SearchAdapterSearchViewPair createUserSearchViewPair(
            FriendsFragment fragment,
            ArrayList<User> users)
    {
        FifaActivity activity = (FifaActivity) fragment.getActivity();
        SearchView searchView = (SearchView) activity.findViewById(R.id.searchView);

        setBasicSearchViewProperties(searchView);
        setSearchViewListeners(activity, searchView);
        SearchViewAdapter adapter = initializeAdapter(fragment, users, searchView);

        return new SearchAdapterSearchViewPair(searchView, adapter);
    }

    private static void setBasicSearchViewProperties(SearchView searchView)
    {
        searchView.setStyle(SearchCodes.STYLE_MENU_ITEM_CLASSIC);
        searchView.setVersion(SearchCodes.VERSION_MENU_ITEM);
        searchView.setTheme(THEME_TYPE);
        searchView.setDivider(true);
        searchView.setAnimationDuration(ANIMATION_DURATION);
        searchView.setVoice(false);
        searchView.setHint("Search Users");
    }

    private static void setSearchViewListeners(FifaActivity activity, SearchView searchView)
    {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.hide(false);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnSearchViewListener(new SearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                activity.getDrawer().getDrawerLayout().setDrawerLockMode(
                        DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }

            @Override
            public void onSearchViewClosed() {
                activity.getDrawer().getDrawerLayout().setDrawerLockMode(
                        DrawerLayout.LOCK_MODE_UNLOCKED);
            }
        });
    }

    private static SearchViewAdapter initializeAdapter(FriendsFragment fragment,
                                                       ArrayList<User> users,
                                                       SearchView searchView)
    {
        SearchViewAdapter adapter = new SearchViewAdapter(fragment.getActivity(),
                Collections.emptyList(),
                Collections.emptyList(),
                THEME_TYPE, users);

        adapter.setOnItemClickListener((view, position) -> {
            searchView.hide(false);
            TextView textView = (TextView) view.findViewById(R.id
                    .textView_item_text);
        });

        return adapter;
    }
}