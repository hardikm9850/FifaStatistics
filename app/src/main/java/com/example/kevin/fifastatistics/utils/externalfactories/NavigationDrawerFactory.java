package com.example.kevin.fifastatistics.utils.externalfactories;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.activities.FifaActivity;
import com.example.kevin.fifastatistics.managers.FragmentInitializationManager;
import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.models.user.User;
import com.example.kevin.fifastatistics.network.FifaApiAdapter;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Factory class for building instances of the navigation drawer. Since the
 * main navigation drawer is shared throughout the app, this can be implemented
 * as a singleton.
 */
public class NavigationDrawerFactory
{
    private static final String DRAWER_PROFILE_TEXT = "FIFA Legend";
    private static final int BADGE_CORNER_RADIUS_DP = 20;
    private static final int FRIENDS_ITEM_IDENTIFIER = 1;
    private static int incomingRequestsCount = 0;
    private static int previousDrawerPosition = -1;

    private static User currentUser;
    private static BadgeStyle badgeStyle;
    private static ProfileDrawerItem profileDrawerItem;
    private static PrimaryDrawerItem overviewItem;
    private static PrimaryDrawerItem statisticsItem;
    private static PrimaryDrawerItem friendsItem;
    private static PrimaryDrawerItem starredItem;
    private static PrimaryDrawerItem settingsItem;

    /**
     * Gets the default drawer instance.
     * @param activity  The activity the drawer is being attached to
     * @return the drawer
     */
    public static Drawer getDefaultDrawerInstance(
            FifaActivity activity)
    {
        currentUser = SharedPreferencesManager.getUser();
        setIncomingRequestsCount();

        if (starredItem == null) {
            initializeActivityIndependentObjects();
        }
        return buildDrawer(activity);
    }

    private static void initializeActivityIndependentObjects()
    {
        initializeBadgeStyle();
        initializeOverviewItem();
        initializeSettingsItem();
        initializeFriendsItem();
        initializeStatisticsItem();
        initializeStarredItem();
    }

    private static Drawer buildDrawer(FifaActivity activity)
    {
        AccountHeader header = initializeDrawerBanner(activity);
        Drawer drawer = initializeDrawer(activity, header);
        setOnDrawerItemClickListener(activity, drawer);

        return drawer;
    }

    private static void setIncomingRequestsCount()
    {
        if (currentUser.getIncomingRequests() != null) {
            incomingRequestsCount = currentUser.getIncomingRequests().size();
        }
    }

    private static void initializeBadgeStyle()
    {
        badgeStyle = new BadgeStyle()
                .withColorRes(R.color.colorAccent)
                .withCornersDp(BADGE_CORNER_RADIUS_DP);
    }

    private static void initializeOverviewItem()
    {
        overviewItem = new PrimaryDrawerItem()
                .withName(R.string.overview)
                .withIcon(R.drawable.ic_home_black_24dp)
                .withIconTintingEnabled(true);
    }

    private static void initializeStatisticsItem()
    {
        statisticsItem = new PrimaryDrawerItem()
                .withName(R.string.statistics)
                .withIcon(R.drawable.ic_assessment_black_24dp)
                .withIconTintingEnabled(true);
    }

    private static void initializeFriendsItem()
    {
         friendsItem = new PrimaryDrawerItem()
                .withName(R.string.friends)
                .withIcon(R.drawable.ic_group_black_24dp)
                .withBadge(String.valueOf(incomingRequestsCount))
                 .withBadgeStyle(badgeStyle)
                 .withIdentifier(FRIENDS_ITEM_IDENTIFIER)
                .withIconTintingEnabled(true);
    }

    private static void initializeStarredItem()
    {
        starredItem = new PrimaryDrawerItem()
                .withName(R.string.starred)
                .withIcon(R.drawable.ic_star_black_24dp)
                .withIconTintingEnabled(true);
    }

    private static void initializeSettingsItem()
    {
        settingsItem = new PrimaryDrawerItem()
                .withName(R.string.settings)
                .withIcon(R.drawable.ic_settings_black_24dp)
                .withIconTintingEnabled(true);
    }

    private static AccountHeader initializeDrawerBanner(FifaActivity activity)
    {
        initializeDrawerImageLoader();
        initializeProfileDrawerItem();
        return initializeAccountHeader(activity);
    }

    private static void initializeDrawerImageLoader()
    {
        DrawerImageLoader.init(new AbstractDrawerImageLoader()
        {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder)
            {
                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.displayImage(currentUser.getImageUrl(), imageView);
            }
        });
    }

    private static void initializeProfileDrawerItem()
    {
        profileDrawerItem = new ProfileDrawerItem()
                .withName(currentUser.getName())
                .withEmail(DRAWER_PROFILE_TEXT)
                .withIcon(currentUser.getImageUrl());
    }

    private static AccountHeader initializeAccountHeader(FifaActivity activity)
    {
        return new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(profileDrawerItem)
                .build();
    }

    private static Drawer initializeDrawer(FifaActivity activity, AccountHeader header)
    {
        return new DrawerBuilder()
                .withActivity(activity)
                .withAccountHeader(header)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .withToolbar(activity.getToolbar())
                .addDrawerItems(
                        overviewItem,
                        statisticsItem,
                        friendsItem,
                        starredItem,
                        new DividerDrawerItem(),
                        settingsItem
                )
                .build();
    }

    private static void setOnDrawerItemClickListener(FifaActivity activity, Drawer drawer)
    {
        drawer.setOnDrawerItemClickListener((view, position, drawerItem) ->
        {
            if (position == previousDrawerPosition) {
                drawer.closeDrawer();
                return true;
            }
            previousDrawerPosition = position;

            if (position == 1) {
                FragmentInitializationManager.initializeOverviewFragment(activity);
            }
            else if (position == 3) {
                FragmentInitializationManager.initializeFriendsFragment(activity);
            }
            else if (position == 4) {
                currentUser.deleteIncomingRequests();
                currentUser.deleteFriends();
                SharedPreferencesManager.storeUser(currentUser);
            }
            else if (position == 6) {
                Log.i("DRAWER", "position: " + position);
                FifaApiAdapter.getService().updateUser(currentUser.getId(), currentUser)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe();
            }
            return false;
        });
    }
}
