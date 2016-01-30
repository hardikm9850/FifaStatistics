package com.example.kevin.fifastatistics;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kevin.fifastatistics.RestClient.RestClient;
import com.example.kevin.fifastatistics.User.Friend;
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

    private RestClient client = RestClient.getInstance();

    private OnListFragmentInteractionListener mListener;
    private ProgressDialog pDialog;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friends_list, container, false);

        new GetUsers().execute();

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
                            user.get("level").asInt()));
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

            // Set the adapter
            if (view instanceof RecyclerView)
            {
                Context context = view.getContext();
                RecyclerView recyclerView = (RecyclerView) view;

                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
                recyclerView.setAdapter(new MyFriendsRecyclerViewAdapter(friendsList, mListener));
            }
        }
    }
}
