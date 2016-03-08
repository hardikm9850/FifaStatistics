package com.example.kevin.fifastatistics.views;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.models.user.User;
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

/**
 * Class defining the main Navigation Drawer for the FIFA Statistics app.
 */
public class FifaNavigationDrawer {

    private Drawer drawer;
    private int previousPosition;

    public FifaNavigationDrawer(Toolbar toolbar,
                                final User currentUser,
                                int incomingRequestsCount,
                                Activity activity)
    {
        BadgeStyle badgeStyle = new BadgeStyle()
                .withColorRes(R.color.colorAccent)
                .withCornersDp(20);

        PrimaryDrawerItem overviewItem = new PrimaryDrawerItem()
                .withName(R.string.overview)
                .withIcon(R.drawable.ic_home_black_24dp)
                .withIconTintingEnabled(true);

        PrimaryDrawerItem statisticsItem = new PrimaryDrawerItem()
                .withName(R.string.statistics)
                .withIcon(R.drawable.ic_assessment_black_24dp)
                .withIconTintingEnabled(true);

        PrimaryDrawerItem friendsItem = new PrimaryDrawerItem()
                .withName(R.string.friends)
                .withIcon(R.drawable.ic_group_black_24dp)
                .withBadge(String.valueOf(incomingRequestsCount))
                .withBadgeStyle(badgeStyle)
                .withIdentifier(1)
                .withIconTintingEnabled(true);

        PrimaryDrawerItem starredItem = new PrimaryDrawerItem()
                .withName(R.string.starred)
                .withIcon(R.drawable.ic_star_black_24dp)
                .withIconTintingEnabled(true);

        PrimaryDrawerItem settingsItem = new PrimaryDrawerItem()
                .withName(R.string.settings)
                .withIcon(R.drawable.ic_settings_black_24dp)
                .withIconTintingEnabled(true);

        //initialize and create the image loader logic
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.displayImage(currentUser.getImageUrl(), imageView);
            }
        });

        // Create the account header
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withName(currentUser.getName())
                                .withEmail("FIFA Legend")
                                .withIcon(currentUser.getImageUrl())
                )
                .build();

        // Create the drawer
        drawer = new DrawerBuilder()
                .withActivity(activity)
                .withAccountHeader(headerResult)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .withToolbar(toolbar)
                .addDrawerItems(
                        overviewItem,
                        statisticsItem,
                        friendsItem,
                        starredItem,
                        new DividerDrawerItem(),
                        settingsItem
                )
                .build();

        previousPosition = drawer.getCurrentSelectedPosition();
    }

    public Drawer getDrawer() {
        return drawer;
    }

    /**
     * Locks the navigation drawer
     */
    public void lock() {
        drawer.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    /**
     * Unlocks the navigation drawer
     */
    public void unlock() {
        drawer.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    public int getPreviousSelectedPosition() {
        return previousPosition;
    }

    public void setPreviousSelectedPosition(int position) {
        previousPosition = position;
    }
}
