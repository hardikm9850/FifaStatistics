package com.example.kevin.fifastatistics.utils.factories;


import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.widget.TextView;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.activities.FifaActivity;
import com.example.kevin.fifastatistics.fragments.FriendsFragment;
import com.example.kevin.fifastatistics.models.user.User;
import com.example.kevin.fifastatistics.views.adapters.SearchAdapter;
import com.lapism.searchview.view.SearchCodes;
import com.lapism.searchview.view.SearchView;

import java.util.ArrayList;
import java.util.Collections;

public class SearchViewFactory
{
    private static final int THEME_TYPE = SearchCodes.THEME_LIGHT;
    private static final int ANIMATION_DURATION = 300;
    private static SearchView mSearchView;
    private static Context mContext;

    /**
     * Creates a search view that searches for users, and initializes the
     * adapter. The adapter is not set to the search view; that must be done
     * externally.
     */
    public static SearchView createUserSearchView(FriendsFragment fragment,
            ArrayList<User> users)
    {
        mContext = fragment.getActivity();
        FifaActivity activity = (FifaActivity) fragment.getActivity();
        mSearchView = (SearchView) activity.findViewById(R.id.searchView);

        setBasicSearchViewProperties();
        setSearchViewListeners(activity);
        initializeAdapter(fragment, users);

        return mSearchView;
    }

    private static void setBasicSearchViewProperties()
    {
        mSearchView.setStyle(SearchCodes.STYLE_MENU_ITEM_CLASSIC);
        mSearchView.setVersion(SearchCodes.VERSION_MENU_ITEM);
        mSearchView.setTheme(THEME_TYPE);
        mSearchView.setDivider(true);
        mSearchView.setAnimationDuration(ANIMATION_DURATION);
        mSearchView.setVoice(false);
        mSearchView.setHint("Search Users");
    }

    private static void setSearchViewListeners(FifaActivity activity)
    {
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

    private static void initializeAdapter(FriendsFragment fragment,
                                          ArrayList<User> users)
    {
        SearchAdapter adapter = new SearchAdapter(mContext, Collections.emptyList(),
                Collections.emptyList(), THEME_TYPE, users);

        adapter.setOnItemClickListener((view, position) -> {
            mSearchView.hide(false);
            TextView textView = (TextView) view.findViewById(R.id
                    .textView_item_text);
        });

        fragment.setSearchAdapter(adapter);
    }
}