package com.example.kevin.fifastatistics.views.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.models.user.User;

import com.example.kevin.fifastatistics.utils.BitmapUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchViewAdapter extends BaseAdapter implements Filterable {

    private static final ImageLoader imageLoader = ImageLoader.getInstance();

    private ArrayList<User> data;
    private ArrayList<User> suggestions;
    private LayoutInflater inflater;
    private boolean ellipsize;

    private boolean isFirstFilterRun = true;

    public SearchViewAdapter(Context context, ArrayList<User> suggestions) {
        inflater = LayoutInflater.from(context);
        data = suggestions;
        this.suggestions = suggestions;
    }

    public SearchViewAdapter(Context context, ArrayList<User> suggestions, boolean ellipsize) {
        inflater = LayoutInflater.from(context);
        data = suggestions;
        this.suggestions = suggestions;
        this.ellipsize = ellipsize;
    }


    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (!TextUtils.isEmpty(constraint)) {

                    // Retrieve the autocomplete results.
                    List<User> searchData = new ArrayList<>();

                    for (User user : suggestions) {
                        if (user.getName().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                            searchData.add(user);
                        }
                    }

                    // Assign the data to the FilterResults
                    filterResults.values = searchData;
                    filterResults.count = searchData.size();
                    isFirstFilterRun = false;
                }
                else if (!isFirstFilterRun) {
                    filterResults.values = suggestions;
                    filterResults.count = suggestions.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results.values != null) {
                    data = (ArrayList<User>) results.values;
                    notifyDataSetChanged();
                }
            }
        };
        return filter;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SuggestionsViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.suggest_item, parent, false);
            viewHolder = new SuggestionsViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SuggestionsViewHolder) convertView.getTag();
        }

        User user = (User) getItem(position);
        Log.i("ADAPTER", user.getName());

        Observable.just(imageLoader.loadImageSync(user.getImageUrl()))
                .map(BitmapUtils::getCircleBitmap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(b -> setImageView(b, viewHolder.imageView));

        viewHolder.textView.setText(user.getName());
        if (ellipsize) {
            viewHolder.textView.setSingleLine();
            viewHolder.textView.setEllipsize(TextUtils.TruncateAt.END);
        }

        return convertView;
    }

    private void setImageView(Bitmap bitmap, ImageView imageView)
    {
        imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 160, 160, false));
    }

    private class SuggestionsViewHolder {

        TextView textView;
        ImageView imageView;

        public SuggestionsViewHolder(View convertView) {
            textView = (TextView) convertView.findViewById(R.id.suggestion_text);
            imageView = (ImageView) convertView.findViewById(R.id.suggestion_icon);
        }
    }
}
