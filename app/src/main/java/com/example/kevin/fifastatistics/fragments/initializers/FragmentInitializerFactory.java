package com.example.kevin.fifastatistics.fragments.initializers;

import com.example.kevin.fifastatistics.activities.FifaActivity;
import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.models.Constants;
import com.example.kevin.fifastatistics.network.FifaApiAdapter;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Factory for generating fragment initializers.
 * <p>
 * Use the {@link #createFragmentInitializer(FifaActivity)} method to create an initializer based
 * on the activity's Extras.
 */
public class FragmentInitializerFactory {

    private static final String FRAGMENT_EXTRA = "fragment";

    /**
     * Creates an initializer for the appropriate fragment, based on the
     * fragment extra passed to the activity. If the extra is null, then an
     * {@link OverviewFragmentInitializer} is returned.
     */
    public static FragmentInitializer createFragmentInitializer(FifaActivity activity) {
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

    private static String getFragmentExtra(FifaActivity activity) {
        String extra = activity.getIntent().getStringExtra(FRAGMENT_EXTRA);
        activity.getIntent().removeExtra(FRAGMENT_EXTRA);

        return extra == null ? Constants.OVERVIEW_FRAGMENT : extra;
    }

    public static FragmentInitializer createFragmentInitializer(int drawerPosition) {
        switch (drawerPosition) {
            case 1:
                return FragmentInitializerFactory.createOverviewFragmentInitializer();
            case 3:
                return FragmentInitializerFactory.createFriendsFragmentInitializer();
            case 4:
                FifaApiAdapter.getService().getUser(SharedPreferencesManager.getUser().getId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(SharedPreferencesManager::storeUser);
                break;
            case 6:
                FifaApiAdapter.getService().updateUser(SharedPreferencesManager.getUser().getId(), SharedPreferencesManager.getUser())
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
