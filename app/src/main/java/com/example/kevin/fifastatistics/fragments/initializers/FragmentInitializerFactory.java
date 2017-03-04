package com.example.kevin.fifastatistics.fragments.initializers;

import com.example.kevin.fifastatistics.activities.FifaBaseActivity;
import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.models.Constants;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.network.ApiAdapter;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Factory for generating fragment initializers.
 * <p>
 * Use the {@link #createFragmentInitializer(FifaBaseActivity)} method to create an initializer based
 * on the activity's Extras.
 */
public class FragmentInitializerFactory {

    private static final String FRAGMENT_EXTRA = "fragment";

    /**
     * Creates an initializer for the appropriate fragment, based on the
     * fragment extra passed to the activity. If the extra is null, then an
     * {@link OverviewFragmentInitializer} is returned.
     */
    public static FragmentInitializer createFragmentInitializer(FifaBaseActivity activity) {
        String fragment = getFragmentExtra(activity);
        switch (fragment) {
            case (Constants.OVERVIEW_FRAGMENT):
                return new OverviewFragmentInitializer();
            case (Constants.FRIENDS_FRAGMENT):
                return new FriendsFragmentInitializer();
            default:
                throw new IllegalStateException(fragment + " is not a valid fragment name!");
        }
    }

    private static String getFragmentExtra(FifaBaseActivity activity) {
        String extra = activity.getIntent().getStringExtra(FRAGMENT_EXTRA);
        activity.getIntent().removeExtra(FRAGMENT_EXTRA);

        return extra == null ? Constants.OVERVIEW_FRAGMENT : extra;
    }

    public static FragmentInitializer createFragmentInitializer(int drawerPosition, Player user) {
        switch (drawerPosition) {
            case 1:
                return FragmentInitializerFactory.createOverviewFragmentInitializer();
            case 3:
                return new MatchesFragmentInitializer(user);
            case 4:
                return FragmentInitializerFactory.createFriendsFragmentInitializer();
            case 5:
                ApiAdapter.getFifaApi().getUser(SharedPreferencesManager.getUser().getId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(SharedPreferencesManager::storeUser);
                break;
            case 6:
                ApiAdapter.getFifaApi().updateUser(SharedPreferencesManager.getUser().getId(), SharedPreferencesManager.getUser())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe();
                break;
        }
        return FragmentInitializerFactory.createOverviewFragmentInitializer();
    }

    public static FragmentInitializer createFriendsFragmentInitializer() {
        return new FriendsFragmentInitializer();
    }

    public static FragmentInitializer createOverviewFragmentInitializer() {
        return new OverviewFragmentInitializer();
    }
}
