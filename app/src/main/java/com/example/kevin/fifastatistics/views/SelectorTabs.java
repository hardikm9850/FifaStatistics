package com.example.kevin.fifastatistics.views;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;

import com.example.kevin.fifastatistics.listeners.SimpleOnTabSelectedListener;

public class SelectorTabs extends TabLayout {

    private static final int SELECTED_TAB_ALPHA = 255;
    private static final int UNSELECTED_TAB_ALPHA = 128;

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
                if (tab.getIcon() != null) {
                    tab.getIcon().setAlpha(SELECTED_TAB_ALPHA);
                }
            }

            @Override
            public void onTabUnselected(Tab tab) {
                if (tab.getIcon() != null) {
                    tab.getIcon().setAlpha(UNSELECTED_TAB_ALPHA);
                }
            }
        });
    }

    public void setTabIcons(int[] resIds) {
        if (resIds != null) {
            removeAllTabs();
            for (int icon : resIds) {
                Tab tab = newTab().setIcon(icon);
                addTab(tab);
                if (!tab.isSelected() && tab.getIcon() != null) {
                    tab.getIcon().setAlpha(UNSELECTED_TAB_ALPHA);
                }
            }
        }
    }

    @BindingAdapter("selectorTabIcons")
    public static void setSelectorIcons(SelectorTabs tabLayout, int[] resIds) {
        tabLayout.setTabIcons(resIds);
    }

    @BindingAdapter("addOnTabSelectedListener")
    public static void addTabSelectedListener(TabLayout layout, OnTabSelectedListener listener) {
        layout.addOnTabSelectedListener(listener);
    }
}
