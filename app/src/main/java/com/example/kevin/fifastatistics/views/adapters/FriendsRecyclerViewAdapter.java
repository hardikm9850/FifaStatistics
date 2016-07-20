package com.example.kevin.fifastatistics.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.fragments.FriendsFragment;
import com.example.kevin.fifastatistics.fragments.FriendsFragment.FriendsFragmentInteractionListener;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Friend} and makes a call to the
 * specified {@link FriendsFragment.FriendsFragmentInteractionListener}.
 */
public class FriendsRecyclerViewAdapter
        extends RecyclerView.Adapter<FriendsRecyclerViewAdapter.ViewHolder> {

    private List<Friend> mUsers;
    private final FriendsFragmentInteractionListener mListener;
    private ImageLoader imageLoader;

    public FriendsRecyclerViewAdapter(List<Friend> users, FriendsFragmentInteractionListener listener) {
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
                    mListener.onFriendsFragmentInteraction(holder.mItem);
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
