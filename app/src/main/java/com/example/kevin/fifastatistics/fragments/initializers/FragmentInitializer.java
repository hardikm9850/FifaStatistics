package com.example.kevin.fifastatistics.fragments.initializers;

import android.support.design.widget.TabLayout;
import android.view.View;

import com.example.kevin.fifastatistics.activities.FifaBaseActivity;
import com.example.kevin.fifastatistics.activities.MainActivity;
import com.example.kevin.fifastatistics.adapters.FragmentAdapter;

/**
 * Prepares the activity for the fragment that will be displayed.
 */
public interface FragmentInitializer {

    String PAGE_EXTRA = MainActivity.PAGE_EXTRA;

    /**
     * Set the title of the activity's toolbar to what is appropriate for the
     * specified fragment.
     */
    void setActivityTitle(FifaBaseActivity activity);

    /**
     * Initialize the fragments that will be present in the activity's ViewPager,
     * and add them to it.
     */
    void changeAdapterDataSet(FragmentAdapter adapter);

    /**
     * Set the state of the Tab Layout (The starting page, whether it's visible,
     * etc.)
     */
    void setTabLayoutVisibility(TabLayout tabLayout);

    /**
     * Set the visibility of the floating action menu.
     */
    void setFabVisibility(View menu);
}
