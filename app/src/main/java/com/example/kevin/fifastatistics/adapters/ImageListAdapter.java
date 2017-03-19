package com.example.kevin.fifastatistics.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Adapter for a list that has images.
 */
public class ImageListAdapter extends BaseAdapter {

    private List<? extends Player> mPlayers;
    private ImageLoader mImageLoader;
    private LayoutInflater mInflater;

    public ImageListAdapter(Context context, List<? extends Player> friends) {
        mPlayers = friends;
        mImageLoader = ImageLoader.getInstance();
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setPlayers(List<? extends Player> players) {
        mPlayers = players;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mPlayers.size();
    }

    @Override
    public Object getItem(int position) {
        return mPlayers.get(position);
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
        holder.mTextView.setText(mPlayers.get(position).getName());
        mImageLoader.displayImage(mPlayers.get(position).getImageUrl(), holder.mImageView);
        return convertView;
    }

    private static class ViewHolder {
        ImageView mImageView;
        TextView mTextView;
    }
}
