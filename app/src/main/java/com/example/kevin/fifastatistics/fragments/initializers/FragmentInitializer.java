package com.example.kevin.fifastatistics.fragments.initializers;

/**
 * Created by Kevin on 5/3/2016.
 */
public abstract class FragmentInitializer
{
    protected static final String PAGE_EXTRA = "page";

    /**
     * Begins the process of preparing the activity and its Toolbar, Tab Layout,
     * etc. for the fragment that will be displayed.
     */
    public final void beginInitialization() {
        setActivityTitle();
        initializeViewPagerFragments();
        prepareTabLayout();
        setCurrentFragmentForActivity();
    }

    /**
     * Set the title of the activity's toolbar to what is appropriate for the
     * specified fragment.
     */
    protected abstract void setActivityTitle();

    /**
     * Initialize the fragments that will be present in the activity's ViewPager,
     * and add them to it.
     */
    protected abstract void initializeViewPagerFragments();

    /**
     * Set the state of the Tab Layout (The starting page, whether it's visible,
     * etc.)
     */
    protected abstract void prepareTabLayout();

    /**
     * Sets the reference to the currently displayed fragment in the given
     * activity by calling
     * {@link com.example.kevin.fifastatistics.activities.FifaActivity#setCurrentFragment(com.example.kevin.fifastatistics.fragments.FifaFragment)}.
     * and supplying the fragment that will be displayed.
     */
    protected abstract void setCurrentFragmentForActivity();
}
