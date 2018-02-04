package com.example.kevin.fifastatistics.views;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.example.kevin.fifastatistics.listeners.SimpleOnTabSelectedListener;

public class SelectorTabs extends TabLayout {

    private static final int SELECTED_TAB_ALPHA = 255;
    private static final int UNSELECTED_TAB_ALPHA = 128;

    private int mInitialTabIndex;

    public SelectorTabs(Context context) {
        this(context, null);
    }

    public SelectorTabs(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectorTabs(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addTabChangeListener();
    }

    private void addTabChangeListener() {
        addOnTabSelectedListener(new SimpleOnTabSelectedListener() {
            @Override
            public void onTabSelected(Tab tab) {
                setDrawableAlpha(tab, SELECTED_TAB_ALPHA);
            }

            @Override
            public void onTabUnselected(Tab tab) {
                setDrawableAlpha(tab, UNSELECTED_TAB_ALPHA);
            }

            private void setDrawableAlpha(Tab tab, int alpha) {
                if (tab.getIcon() != null) {
                    tab.getIcon().setAlpha(alpha);
                }
            }
        });
    }

    public void setTabIcons(int[] resIds) {
        if (resIds != null) {
            removeAllTabs();
            for (int icon : resIds) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), icon);
                Tab tab = newTab().setIcon(drawable.mutate());
                addTab(tab);
                if (tab.getPosition() == mInitialTabIndex) {
                    tab.select();
                }
                if (!tab.isSelected() && tab.getIcon() != null) {
                    tab.getIcon().setAlpha(UNSELECTED_TAB_ALPHA);
                }
            }
        }
    }

    private void setInitialTabIndex(int index) {
        mInitialTabIndex = index;
        if (getTabCount() - 1 >= index) {
            Tab tab = getTabAt(index);
            if (tab != null) {
                tab.select();
            }
        }
    }

    @BindingAdapter("selectorTabIcons")
    public static void setSelectorIcons(SelectorTabs tabLayout, int[] resIds) {
        tabLayout.setTabIcons(resIds);
    }

    @BindingAdapter("addOnTabSelectedListener")
    public static void addTabSelectedListener(TabLayout layout, OnTabSelectedListener listener) {
        if (listener != null) {
            layout.addOnTabSelectedListener(listener);
        }
    }

    @BindingAdapter("tabTitles")
    public static void setTabTitles(TabLayout layout, String[] titles) {
        if (titles != null && layout.getTabCount() == 0) {
            for (String title : titles) {
                Tab tab = layout.newTab().setText(title);
                layout.addTab(tab);
            }
        }
    }

    @BindingAdapter("initialPosition")
    public static void setTabPosition(SelectorTabs tabs, int index) {
        tabs.setInitialTabIndex(index);
    }
}
