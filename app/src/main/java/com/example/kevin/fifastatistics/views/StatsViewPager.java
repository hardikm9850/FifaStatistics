package com.example.kevin.fifastatistics.views;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.example.kevin.fifastatistics.adapters.StatsPagerAdapter;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;

import java.util.List;

public class StatsViewPager extends ViewPager {

    public StatsViewPager(Context context) {
        super(context);
    }

    public StatsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Will wrap its height to the height of its largest child.
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        for(int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if(h > height) {
                height = h;
            }
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @BindingAdapter({"stats", "title"})
    public static void setUser(ViewPager pager, List<User.StatsPair> stats, String title) {
        if (stats != null) {
            pager.setAdapter(new StatsPagerAdapter(pager.getContext(), stats, title));
            pager.setCurrentItem(0);
        }
    }
}
