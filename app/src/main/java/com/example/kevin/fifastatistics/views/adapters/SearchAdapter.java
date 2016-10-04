package com.example.kevin.fifastatistics.views.adapters;


import android.content.Context;
import android.graphics.Typeface;
import android.media.ThumbnailUtils;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.utils.BitmapUtils;
import com.lapism.searchview.SearchView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@SuppressWarnings({"WeakerAccess", "unused"})
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ResultViewHolder> implements Filterable {

    private static final int IMAGE_SIZE = 150;

    private String key = "";
    private List<? extends Player> mResultList = new ArrayList<>();
    private List<? extends Player> mSuggestionsList = new ArrayList<>();
    private List<OnItemClickListener> mItemClickListeners;
    private ImageLoader mImageLoader;

    public SearchAdapter(Context context) {
        getFilter().filter("");
        mImageLoader = ImageLoader.getInstance();
    }

    public SearchAdapter(Context context, List<? extends Player> suggestionsList) {
        mSuggestionsList = suggestionsList;
        mResultList = suggestionsList;
        mImageLoader = ImageLoader.getInstance();
        getFilter().filter("");
    }

    public List<? extends Player> getSuggestionsList() {
        return mSuggestionsList;
    }

    public void setSuggestionsList(List<? extends Player> suggestionsList) {
        mSuggestionsList = suggestionsList;
        mResultList = suggestionsList;
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
                    key = constraint.toString().toLowerCase(Locale.getDefault());

                    List<Player> results = new ArrayList<>();
                    for (Player player : mSuggestionsList) {
                        String string = player.getName().toLowerCase(Locale.getDefault());
                        if (string.startsWith(key)) {
                            results.add(player);
                        }
                    }
                    if (results.size() > 0) {
                        filterResults.values = results;
                        filterResults.count = results.size();
                    }
                } else {
                    key = "";
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                List<Player> dataSet = new ArrayList<>();

                if (results.values != null) {
                    List<?> result = (ArrayList<?>) results.values;
                    for (Object object : result) {
                        if (object instanceof Player) {
                            dataSet.add((Player) object);
                        }
                    }
                }
                setData(dataSet);
            }
        };
    }

    public void setData(List<? extends Player> data) {
        if (mResultList == null) {
            mResultList = data;
            notifyDataSetChanged();
        } else {
            int previousSize = mResultList.size();
            int nextSize = data.size();
            mResultList = data;
            if (previousSize == nextSize && nextSize != 0)
                notifyItemRangeChanged(0, previousSize);
            else if (previousSize > nextSize) {
                if (nextSize == 0)
                    notifyItemRangeRemoved(0, previousSize);
                else {
                    notifyItemRangeChanged(0, nextSize);
                    notifyItemRangeRemoved(nextSize - 1, previousSize);
                }
            } else {
                notifyItemRangeChanged(0, previousSize);
                notifyItemRangeInserted(previousSize, nextSize - previousSize);
            }
        }
    }

    @Override
    public ResultViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.search_list_item, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ResultViewHolder viewHolder, int position) {
        Player item = mResultList.get(position);

        viewHolder.icon_left.setScaleType(ImageView.ScaleType.CENTER_CROP);
        setIcon(item.getImageUrl(), viewHolder.icon_left);
        viewHolder.text.setTypeface((Typeface.create(SearchView.getTextFont(), SearchView.getTextStyle())));
        viewHolder.text.setTextColor(SearchView.getTextColor());

        String itemText = item.getName();
        String itemTextLower = itemText.toLowerCase(Locale.getDefault());

        if (itemTextLower.contains(key) && !key.isEmpty()) {
            SpannableString s = new SpannableString(itemText);
            s.setSpan(new ForegroundColorSpan(SearchView.getTextHighlightColor()), itemTextLower.indexOf(key), itemTextLower.indexOf(key) + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.text.setText(s, TextView.BufferType.SPANNABLE);
        } else {
            viewHolder.text.setText(item.getName());
        }
    }

    private void setIcon(String imageUrl, ImageView icon) {
        Observable.just(imageUrl)
                .map(mImageLoader::loadImageSync)
                .map(b -> b == null ? BitmapUtils.getBlankBitmap(IMAGE_SIZE, IMAGE_SIZE) : b)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(icon::setImageBitmap);
    }

    @Override
    public int getItemCount() {
        return mResultList.size();
    }

    public Player getPlayerAtPosition(int position) {
        return mResultList.get(position);
    }

    public void addOnItemClickListener(OnItemClickListener listener) {
        addOnItemClickListener(listener, null);
    }

    protected void addOnItemClickListener(OnItemClickListener listener, Integer position) {
        if (mItemClickListeners == null)
            mItemClickListeners = new ArrayList<>();
        if (position == null)
            mItemClickListeners.add(listener);
        else
            mItemClickListeners.add(position, listener);
    }

    @SuppressWarnings("UnusedParameters")
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final protected ImageView icon_left;
        final protected TextView text;

        public ResultViewHolder(View view) {
            super(view);
            icon_left = (ImageView) view.findViewById(R.id.imageView_item_icon_left);
            text = (TextView) view.findViewById(R.id.textView_item_text);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListeners != null) {
                for (OnItemClickListener listener : mItemClickListeners)
                    listener.onItemClick(v, getLayoutPosition());
            }
        }
    }

}
