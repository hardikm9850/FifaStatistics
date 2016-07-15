package com.example.kevin.fifastatistics.views.wrappers;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.widget.ImageView;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.activities.FifaActivity;
import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Wrapper class around {@link com.mikepenz.materialdrawer.Drawer}.
 */
public class FifaNavigationDrawer {

    private static final String DRAWER_PROFILE_TEXT = "FIFA Legend";
    private static final int BADGE_CORNER_RADIUS_DP = 20;
    private static final int FRIENDS_ITEM_IDENTIFIER = 1;

    private static FifaNavigationDrawer mInstance;

    private Drawer mDrawer;

    /**
     * Return the navigation drawer instance.
     *
     * @param activity the activity the drawer will be in
     * @return the drawer
     */
    public static FifaNavigationDrawer getInstance(FifaActivity activity) {
        if (mInstance == null) {
            mInstance = new FifaNavigationDrawer(activity);
        }
        return mInstance;
    }

    /**
     * Sets the onDrawerItemClickListener.
     *
     * @param listener the listener
     */
    public void setOnDrawerItemClickListener(Drawer.OnDrawerItemClickListener listener) {
        mDrawer.setOnDrawerItemClickListener(listener);
    }

    /**
     * Close the drawer.
     */
    public void closeDrawer() {
        mDrawer.closeDrawer();
    }

    /**
     * Set whether the drawer should be locked or not.
     *
     * @param locked true if the drawer should be locked, false otherwise
     */
    public void setLocked(boolean locked) {
        int mode = locked ? DrawerLayout.LOCK_MODE_LOCKED_CLOSED : DrawerLayout.LOCK_MODE_UNLOCKED;
        mDrawer.getDrawerLayout().setDrawerLockMode(mode);
    }

    private FifaNavigationDrawer(FifaActivity activity) {
        User user = SharedPreferencesManager.getUser();

        int incomingRequestsCount = user.getIncomingRequests().size();
        BadgeStyle badgeStyle = initializeBadgeStyle();
        PrimaryDrawerItem friendsItem = initializeFriendsItem(badgeStyle, incomingRequestsCount);

        PrimaryDrawerItem overviewItem = initializeOverviewItem();
        PrimaryDrawerItem settingsItem = initializeSettingsItem();
        PrimaryDrawerItem statisticsItem = initializeStatisticsItem();
        PrimaryDrawerItem starredItem = initializeStarredItem();

        IDrawerItem[] items = {overviewItem, statisticsItem, friendsItem, starredItem,
                new DividerDrawerItem(), settingsItem};
        mDrawer = buildDrawer(user, activity, items);
    }

    private BadgeStyle initializeBadgeStyle() {
        return new BadgeStyle()
                .withColorRes(R.color.colorAccent)
                .withCornersDp(BADGE_CORNER_RADIUS_DP);
    }

    private PrimaryDrawerItem initializeOverviewItem() {
        return new PrimaryDrawerItem()
                .withName(R.string.overview)
                .withIcon(R.drawable.ic_home_black_24dp)
                .withIconTintingEnabled(true);
    }

    private PrimaryDrawerItem initializeStatisticsItem() {
        return new PrimaryDrawerItem()
                .withName(R.string.statistics)
                .withIcon(R.drawable.ic_assessment_black_24dp)
                .withIconTintingEnabled(true);
    }

    private PrimaryDrawerItem initializeFriendsItem(BadgeStyle style, int incomingRequestsCount) {
        return new PrimaryDrawerItem()
                .withName(R.string.friends)
                .withIcon(R.drawable.ic_group_black_24dp)
                .withBadge(String.valueOf(incomingRequestsCount))
                .withBadgeStyle(style)
                .withIdentifier(FRIENDS_ITEM_IDENTIFIER)
                .withIconTintingEnabled(true);
    }

    private PrimaryDrawerItem initializeStarredItem() {
        return new PrimaryDrawerItem()
//                .withName(R.string.starred)
                .withName("GET from server")
                .withIcon(R.drawable.ic_star_black_24dp)
                .withIconTintingEnabled(true);
    }

    private PrimaryDrawerItem initializeSettingsItem() {
        return new PrimaryDrawerItem()
//                .withName(R.string.settings)
                .withName("PUT to server")
                .withIcon(R.drawable.ic_settings_black_24dp)
                .withIconTintingEnabled(true);
    }

    private Drawer buildDrawer(User user, FifaActivity activity, IDrawerItem... items) {
        AccountHeader header = initializeDrawerBanner(user, activity);
        return initializeDrawer(activity, header, items);
    }

    private AccountHeader initializeDrawerBanner(User user, FifaActivity activity) {
        initializeDrawerImageLoader(user);
        ProfileDrawerItem profile = initializeProfileDrawerItem(user);
        return initializeAccountHeader(profile, activity);
    }

    private void initializeDrawerImageLoader(User user) {
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.displayImage(user.getImageUrl(), imageView);
            }
        });
    }

    private ProfileDrawerItem initializeProfileDrawerItem(User user) {
        return new ProfileDrawerItem()
                .withName(user.getName())
                .withEmail(DRAWER_PROFILE_TEXT)
                .withIcon(user.getImageUrl());
    }

    private AccountHeader initializeAccountHeader(ProfileDrawerItem profile, FifaActivity activity) {
        return new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(profile)
                .build();
    }

    private Drawer initializeDrawer(FifaActivity activity, AccountHeader header, IDrawerItem... items) {
        return new DrawerBuilder()
                .withActivity(activity)
                .withAccountHeader(header)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .withToolbar(activity.getToolbar())
                .addDrawerItems(items)
                .build();
    }
}
