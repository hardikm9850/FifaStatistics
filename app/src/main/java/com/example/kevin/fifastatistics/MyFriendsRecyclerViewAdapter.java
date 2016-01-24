package com.example.kevin.fifastatistics;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kevin.fifastatistics.FriendsFragment.OnListFragmentInteractionListener;
import com.example.kevin.fifastatistics.User.User;
import com.example.kevin.fifastatistics.utils.GetAndSetImageFromUrl;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link User} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyFriendsRecyclerViewAdapter extends RecyclerView.Adapter<MyFriendsRecyclerViewAdapter.ViewHolder> {

    private List<User> mUsers;
    private final OnListFragmentInteractionListener mListener;

    public MyFriendsRecyclerViewAdapter(List<User> users, OnListFragmentInteractionListener listener) {
        mUsers = users;
        mListener = listener;
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
        
        new GetAndSetImageFromUrl(holder.mImageView).execute(mUsers.get(position).getImageUrl());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final ImageView mImageView;
        public User mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.name);
            mImageView = (ImageView) view.findViewById(R.id.user_image);
        }
    }
}
