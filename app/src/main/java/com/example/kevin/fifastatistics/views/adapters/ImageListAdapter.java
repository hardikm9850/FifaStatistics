package com.example.kevin.fifastatistics.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Adapter for a list that has images.
 */
public class ImageListAdapter extends BaseAdapter {

    private List<Friend> mFriends;
    private ImageLoader mImageLoader;
    private LayoutInflater mInflater;

    public ImageListAdapter(Context context, List<Friend> friends) {
        mFriends = friends;
        mImageLoader = ImageLoader.getInstance();
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mFriends.size();
    }

    @Override
    public Object getItem(int position) {
        return mFriends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.image_list_item, parent, false);
            holder = new ViewHolder();
            holder.mTextView = (TextView) convertView.findViewById(R.id.text);
            holder.mImageView = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mTextView.setText(mFriends.get(position).getName());
        mImageLoader.displayImage(mFriends.get(position).getImageUrl(), holder.mImageView);
        return convertView;
    }

    private static class ViewHolder {
        public ImageView mImageView;
        public TextView mTextView;
    }
}
