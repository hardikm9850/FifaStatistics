package com.example.kevin.fifastatistics.views;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.v4.widget.DrawerLayout;
import android.widget.ImageView;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.activities.FifaBaseActivity;
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

    private Drawer mDrawer;
    private int mSelectedColor;

    public static FifaNavigationDrawer newInstance(FifaBaseActivity activity, @ColorInt int selectedColor) {
        return new FifaNavigationDrawer(activity, selectedColor);
    }

    public void setOnDrawerItemClickListener(Drawer.OnDrawerItemClickListener listener) {
        mDrawer.setOnDrawerItemClickListener(listener);
    }

    public void closeDrawer() {
        mDrawer.closeDrawer();
    }

    public void updateColors(@ColorInt int color) {
        mSelectedColor = color;
        for (IDrawerItem item : mDrawer.getDrawerItems()) {
            if (item instanceof PrimaryDrawerItem) {
                ((PrimaryDrawerItem) item).withSelectedTextColor(color).withSelectedIconColor(color);
                mDrawer.updateItem(item);
            }
        }
    }

    public void setLocked(boolean locked) {
        int mode = locked ? DrawerLayout.LOCK_MODE_LOCKED_CLOSED : DrawerLayout.LOCK_MODE_UNLOCKED;
        mDrawer.getDrawerLayout().setDrawerLockMode(mode);
    }

    public void setPosition(int position) {
        mDrawer.setSelectionAtPosition(position);
    }

    private FifaNavigationDrawer(FifaBaseActivity activity, int color) {
        mSelectedColor = color;
        User user = SharedPreferencesManager.getUser();

        int incomingRequestsCount = user.getIncomingRequests().size();
        BadgeStyle badgeStyle = initializeBadgeStyle();
        PrimaryDrawerItem friendsItem = initializeFriendsItem(badgeStyle, incomingRequestsCount);
        PrimaryDrawerItem matchesItem = initializeMatchesItem();
        PrimaryDrawerItem overviewItem = initializeOverviewItem();
        PrimaryDrawerItem settingsItem = initializeSettingsItem();
        PrimaryDrawerItem statisticsItem = initializeStatisticsItem();
        PrimaryDrawerItem starredItem = initializeStarredItem();

        IDrawerItem[] items = {overviewItem, statisticsItem, matchesItem, friendsItem, starredItem,
                new DividerDrawerItem(), settingsItem};
        mDrawer = buildDrawer(user, activity, items);
    }

    private PrimaryDrawerItem getBaseDrawerItem(long identifier) {
        return new PrimaryDrawerItem().withIconTintingEnabled(true)
                .withSelectedTextColor(mSelectedColor)
                .withSelectedIconColor(mSelectedColor)
                .withIdentifier(identifier);
    }

    private BadgeStyle initializeBadgeStyle() {
        return new BadgeStyle()
                .withColorRes(R.color.colorAccent)
                .withCornersDp(BADGE_CORNER_RADIUS_DP);
    }

    private PrimaryDrawerItem initializeOverviewItem() {
        return getBaseDrawerItem(11)
                .withName(R.string.overview)
                .withIcon(R.drawable.ic_home_black_24dp);
    }

    private PrimaryDrawerItem initializeMatchesItem() {
        return getBaseDrawerItem(22)
                .withName(R.string.matches)
                .withIcon(R.drawable.soccer);
    }

    private PrimaryDrawerItem initializeStatisticsItem() {
        return getBaseDrawerItem(33)
                .withName(R.string.statistics)
                .withIcon(R.drawable.ic_assessment_black_24dp);
    }

    private PrimaryDrawerItem initializeFriendsItem(BadgeStyle style, int incomingRequestsCount) {
        return getBaseDrawerItem(44)
                .withName(R.string.players)
                .withIcon(R.drawable.ic_group_black_24dp)
                .withIdentifier(FRIENDS_ITEM_IDENTIFIER);
    }

    private PrimaryDrawerItem initializeStarredItem() {
        return getBaseDrawerItem(55)
//                .withName(R.string.starred)
                .withName("GET from server")
                .withIcon(R.drawable.ic_star_black_24dp);
    }

    private PrimaryDrawerItem initializeSettingsItem() {
        return getBaseDrawerItem(66)
                .withName(R.string.settings)
                .withIcon(R.drawable.ic_settings_black_24dp);
    }

    private Drawer buildDrawer(User user, FifaBaseActivity activity, IDrawerItem... items) {
        AccountHeader header = initializeDrawerBanner(user, activity);
        return initializeDrawer(activity, header, items);
    }

    private AccountHeader initializeDrawerBanner(User user, FifaBaseActivity activity) {
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

    private AccountHeader initializeAccountHeader(ProfileDrawerItem profile, FifaBaseActivity activity) {
        return new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(profile)
                .build();
    }

    private Drawer initializeDrawer(FifaBaseActivity activity, AccountHeader header, IDrawerItem... items) {
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
