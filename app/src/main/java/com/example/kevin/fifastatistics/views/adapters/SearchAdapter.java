package com.example.kevin.fifastatistics.views.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import com.example.kevin.fifastatistics.models.user.User;
import com.lapism.searchview.R;
import com.lapism.searchview.adapter.SearchItem;
import com.lapism.searchview.view.SearchCodes;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class SearchAdapter extends com.lapism.searchview.adapter.SearchAdapter
{

    private final List<Integer> mStartList = new ArrayList<>();
    private final Context mContext;
    private final int mTheme;
    private List<User> mSearchList;
    private List<User> mDataList = new ArrayList<>();
    private OnItemClickListener mItemClickListener;
    private ImageLoader mImageLoader = ImageLoader.getInstance();
    private int mKeyLength = 0;

    public SearchAdapter(Context context, List<SearchItem> searchList,
                         List<SearchItem> dataList, int theme, ArrayList<User> users) {
        super(context, searchList, dataList, theme);
        this.mContext = context;
        this.mSearchList =  new ArrayList<>();
        this.mDataList = users;
        this.mTheme = theme;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (!TextUtils.isEmpty(constraint)) {
                    List<User> searchData = new ArrayList<>();

                    mStartList.clear();
                    String key = constraint.toString().toLowerCase(Locale.getDefault());

                    for (User user : mDataList) {
                        String string = user.getName().toLowerCase(Locale
                                .getDefault());
                        if (string.contains(key)) {
                            searchData.add(user);
                            mStartList.add(string.indexOf(key));
                            mKeyLength = key.length();
                        }
                    }
                    filterResults.values = searchData;
                    filterResults.count = searchData.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results.values != null) {
                    mSearchList.clear();
                    List<?> result = (List<?>) results.values;
                    for (Object object : result) {
                        if (object instanceof User) {
                            mSearchList.add((User) object);
                        }
                    }
                    notifyDataSetChanged();
                }
            }
        };
    }

    @Override
    public ResultViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        final View sView = mInflater.inflate(R.layout.search_item, parent, false);

        return new ResultViewHolder(sView);
    }

    @Override
    public void onBindViewHolder(ResultViewHolder viewHolder, int position) {
        User user = mSearchList.get(position);

        int start = mStartList.get(position);
        int end = start + mKeyLength;

        mImageLoader.displayImage(user.getImageUrl(), viewHolder.icon_left);

        if (mTheme == SearchCodes.THEME_LIGHT) {
            viewHolder.text.setTextColor(ContextCompat.getColor(mContext, R.color.search_light_text));

            viewHolder.text.setText(user.getName(), TextView.BufferType.SPANNABLE);
            Spannable s = (Spannable) viewHolder.text.getText();
            s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.search_light_text_highlight)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (mTheme == SearchCodes.THEME_DARK) {
            viewHolder.text.setTextColor(ContextCompat.getColor(mContext, R.color.search_dark_text));

            viewHolder.text.setText(user.getName(), TextView.BufferType.SPANNABLE);
            Spannable s = (Spannable) viewHolder.text.getText();
            s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.search_dark_text_highlight)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    @Override
    public int getItemCount() {
        return mSearchList.size();
    }
}