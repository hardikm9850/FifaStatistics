package com.example.kevin.fifastatistics.friendsfragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.restclient.RestClient;
import com.example.kevin.fifastatistics.user.Friend;
import com.example.kevin.fifastatistics.user.User;
import com.example.kevin.fifastatistics.utils.PreferenceHandler;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A fragment representing a list of users.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FriendsFragment extends Fragment {

//    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final int mColumnCount = 2;
    private static final String REQUESTS = "Requests";

    private RestClient client = RestClient.getInstance();

    private OnListFragmentInteractionListener mListener;
    private ProgressDialog pDialog;

    private User mUser;
    private ArrayList<Friend> friendsList = new ArrayList<>();
    private View view = null;

    public FriendsFragment() {
    }

//    // TODO: Customize parameter initialization
//    @SuppressWarnings("unused")
//    public static FriendsFragment newInstance(int columnCount) {
//        FriendsFragment fragment = new FriendsFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_COLUMN_COUNT, columnCount);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceHandler handler = PreferenceHandler.getInstance(getContext());
        mUser = handler.getUser();

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friends_list, container, false);

        int viewToShow = getArguments().getInt("view", 0);
        if (viewToShow == 0) {
            setAdapter(mUser.getFriends());
        }
        else {
            getActivity().setTitle(REQUESTS);
            setAdapter(mUser.getIncomingRequests());
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        if (item.getItemId() == R.id.friend_requests) {
            setAdapter(mUser.getIncomingRequests());
            getActivity().setTitle(REQUESTS);
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void setAdapter(ArrayList<Friend> users)
    {
        if (view instanceof RecyclerView)
        {
            RecyclerView recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), mColumnCount));
            recyclerView.setAdapter(new MyFriendsRecyclerViewAdapter(users, mListener));
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Friend friend);
    }

    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetUsers extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(view.getContext());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try
            {
                JsonNode users = client.getUsers();
                int size = users.size();
                JsonNode user;
                for (int i = 0; i < size; i++)
                {
                    user = users.get(i);
                    friendsList.add(new Friend(
                            user.get("_links").get("self").get("href").asText(),
                            user.get("name").asText(),
                            user.get("imageUrl").asText(),
                            user.get("level").asInt(),
                            user.get("registrationToken").asText()));
                }
                return null;
            }
            catch (IOException e)
            {
                // TODO
                return null;
            }
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (pDialog.isShowing())
                pDialog.dismiss();

            setAdapter(friendsList);
        }
    }
}
