package edu.csulb.android.fullcount.ui.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import edu.csulb.android.fullcount.R;
import edu.csulb.android.fullcount.io.models.Player;

public class PlayerCardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARGUMENT_PLAYER = "PLAYER";

    // TODO: Rename and change types of parameters
    private Player mPlayer;
    private ImageView mPlayerImage;
    private ImageView mFrameImage;
    private TextView mPlayerName;

    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types and number of parameters
    public static PlayerCardFragment newInstance(Player player) {
        PlayerCardFragment fragment = new PlayerCardFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARGUMENT_PLAYER, player);
        fragment.setArguments(args);
        return fragment;
    }

    public PlayerCardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPlayer = (Player)getArguments().getSerializable(ARGUMENT_PLAYER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_card, container, false);

        mPlayerImage = (ImageView)view.findViewById(R.id.player_image_player_card_fragment);
        mPlayerImage.setImageResource(R.drawable.ic_launcher);

        mFrameImage = (ImageView)view.findViewById(R.id.frame_player_card_fragment);
        //mFrameImage.setImageResource(R.drawable.ic_launcher);

        mPlayerName = (TextView)view.findViewById(R.id.name_player_card_fragment);
        mPlayerName.setText(mPlayer.getUsername());

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
        //public void onFragmentInteraction(Uri uri);
    }

}
