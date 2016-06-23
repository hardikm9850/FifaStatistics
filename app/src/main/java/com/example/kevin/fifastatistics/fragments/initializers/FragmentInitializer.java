package com.example.kevin.fifastatistics.fragments.initializers;

import android.support.design.widget.TabLayout;

import com.example.kevin.fifastatistics.activities.FifaActivity;
import com.example.kevin.fifastatistics.activities.MainActivity;
import com.example.kevin.fifastatistics.views.adapters.ViewPagerAdapter;

/**
 * Prepares the activity for the fragment that will be displayed.
 * For example, it sets the activity title, sets the tab layout state, and initializes the
 * fragments in the activity's view pager.
 */
public abstract class FragmentInitializer {

    protected static final String PAGE_EXTRA = MainActivity.PAGE_EXTRA;

    /**
     * Set the title of the activity's toolbar to what is appropriate for the
     * specified fragment.
     */
    public abstract void setActivityTitle(FifaActivity activity);

    /**
     * Initialize the fragments that will be present in the activity's ViewPager,
     * and add them to it.
     */
    public abstract void changeAdapterDataSet(ViewPagerAdapter adapter);

    /**
     * Set the state of the Tab Layout (The starting page, whether it's visible,
     * etc.)
     */
    public abstract void setTabLayoutVisibility(TabLayout tabLayout);
}
