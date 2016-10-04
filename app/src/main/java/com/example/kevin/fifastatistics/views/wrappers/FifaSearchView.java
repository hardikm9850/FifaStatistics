package com.example.kevin.fifastatistics.views.wrappers;

import android.app.Activity;
import android.content.Intent;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.activities.FifaActivity;
import com.example.kevin.fifastatistics.models.apiresponses.ApiListResponse;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.network.ApiAdapter;
import com.example.kevin.fifastatistics.utils.IntentFactory;
import com.example.kevin.fifastatistics.utils.ObservableUtils;
import com.example.kevin.fifastatistics.views.adapters.SearchAdapter;
import com.lapism.searchview.SearchView;

import java.util.List;

import rx.Observable;

/**
 * Wrapper class around {@link com.lapism.searchview.SearchView}.
 */
public class FifaSearchView {

    private static FifaSearchView mInstance;

    private SearchView mSearchView;
    private SearchAdapter mAdapter;
    private boolean mIsAdapterSet;

    /**
     * Get the SearchView instance. This method will not attach an adapter to the searchview.
     * This method makes a network call to retrieve the list of users.
     * @param activity The activity the searchview is in.
     * @param currentUser The current user
     * @return the search view, or null if the list of users cannot be retrieved
     */
    public static Observable<FifaSearchView> getInstance(FifaActivity activity, User currentUser) {
        if (mInstance == null) {
            return ApiAdapter.getFifaApi().getUsers()
                    .compose(ObservableUtils.applySchedulers())
                    .map(ApiListResponse::getItems)
                    .map(users -> { users.remove(currentUser); return users; })
                    .map(players -> mInstance = new FifaSearchView(activity, players))
                    .onErrorReturn(t -> null);
        } else {
            return Observable.just(mInstance);
        }
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

    private FifaSearchView(FifaActivity activity, List<? extends Player> users) {
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
        mSearchView.setNavigationIconArrowHamburger();
        mSearchView.setTheme(SearchView.THEME_LIGHT);
        mSearchView.setVoice(false);
        mSearchView.setHint("Search Users");
    }

    private void setSearchViewListeners(FifaActivity activity) {
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (mSearchView.getAdapter().getItemCount() == 1) {
                    launchPlayerActivity(activity, (SearchAdapter) mSearchView.getAdapter(), 0);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mSearchView.setOnOpenCloseListener(new SearchView.OnOpenCloseListener() {
            @Override
            public void onOpen() {
                activity.setNavigationLocked(true);
            }

            @Override
            public void onClose() {
                activity.setNavigationLocked(false);
            }
        });
    }

    private SearchAdapter initializeAdapter(FifaActivity activity, List<? extends Player> players) {
        SearchAdapter adapter = new SearchAdapter(activity, players);
        adapter.addOnItemClickListener((v, p) -> launchPlayerActivity(activity, adapter, p));

        return adapter;
    }

    private void launchPlayerActivity(Activity activity, SearchAdapter adapter, int position) {
        mSearchView.close(true);
        Player selectedUser = adapter.getPlayerAtPosition(position);
        Intent intent = IntentFactory.createPlayerActivityIntent(activity, selectedUser);
        activity.startActivity(intent);
    }
}