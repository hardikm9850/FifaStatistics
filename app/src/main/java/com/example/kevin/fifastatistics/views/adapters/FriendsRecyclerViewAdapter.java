package com.example.kevin.fifastatistics.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.fragments.FriendsFragment.OnListFragmentInteractionListener;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Friend} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class FriendsRecyclerViewAdapter
        extends RecyclerView.Adapter<FriendsRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Friend> mUsers;
    private final OnListFragmentInteractionListener mListener;
    private ImageLoader imageLoader;

    public FriendsRecyclerViewAdapter(ArrayList<Friend> users,
                                      OnListFragmentInteractionListener
                                              listener) {
        mUsers = users;
        mListener = listener;
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_friends, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mUsers.get(position);
        holder.mNameView.setText(mUsers.get(position).getName());

        imageLoader.displayImage(mUsers.get(position).getImageUrl(), holder.mImageView);
        holder.mView.setOnClickListener(view -> {
                if (mListener != null) {
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            });
    }

    @Override
    public int getItemCount() {
        if (mUsers == null) {
            return 0;
        }
        else {
            return mUsers.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final ImageView mImageView;
        public Friend mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.name);
            mImageView = (ImageView) view.findViewById(R.id.user_image);
        }
    }
}
