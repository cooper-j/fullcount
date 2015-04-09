package edu.csulb.android.fullcount.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import edu.csulb.android.fullcount.R;
import edu.csulb.android.fullcount.io.models.Player;
import edu.csulb.android.fullcount.ui.adapters.FavoritesListAdapter;

public class FavoritesFragment extends Fragment {
    private static final String ARGUMENT_FAVORITES = "FAVORITES";

    private ArrayList<Player> mFavoritesList;
    private FavoritesListAdapter mAdapter;

    private OnFragmentInteractionListener mListener;

    public static FavoritesFragment newInstance(ArrayList<Player> favorites) {
        FavoritesFragment fragment = new FavoritesFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARGUMENT_FAVORITES, favorites);
        fragment.setArguments(args);
        return fragment;
    }

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFavoritesList = (ArrayList<Player>)getArguments().getSerializable(ARGUMENT_FAVORITES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        mAdapter = new FavoritesListAdapter(getActivity(), mFavoritesList);
        ListView listView = (ListView)view.findViewById(R.id.favorites_list);
        listView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        //public void onSaveFavorites(ArrayList<Player> favorites);
    }

}
