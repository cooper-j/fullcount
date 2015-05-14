package edu.csulb.android.fullcount.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONObject;

import edu.csulb.android.fullcount.R;
import edu.csulb.android.fullcount.io.models.Player;
import edu.csulb.android.fullcount.tools.FullCountRestClient;

public class PlayerCardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARGUMENT_USER = "user";
    private static final String ARGUMENT_PLAYER = "player";
    private static final String ARGUMENT_AUTH = "auth";

    // TODO: Rename and change types of parameters
    public Player mUser;
    public Player mPlayer;
    private String mAuth;

    private CheckBox mFavoriteStar;
    private ImageView mPlayerImage;
    private ImageView mFrameImage;
    private TextView mPlayerName;

    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types and number of parameters
    public static PlayerCardFragment newInstance(Player user, Player player, String auth) {
        PlayerCardFragment fragment = new PlayerCardFragment();
        Bundle args = new Bundle();
        fragment.mUser = user;
        args.putSerializable(ARGUMENT_USER, user);
        args.putSerializable(ARGUMENT_PLAYER, player);
        args.putString(ARGUMENT_AUTH, auth);
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
            //mUser = (Player)getArguments().getSerializable(ARGUMENT_USER);
            mPlayer = (Player)getArguments().getSerializable(ARGUMENT_PLAYER);
            mAuth = getArguments().getString(ARGUMENT_AUTH);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_card, container, false);

        mFavoriteStar = (CheckBox)view.findViewById(R.id.favorite_star);
        mPlayerImage = (ImageView)view.findViewById(R.id.player_image_player_card_fragment);

	    if (mPlayer.getPictureUri() != null) {
		    ImageLoader.getInstance().displayImage(FullCountRestClient.getAbsoluteUrl(mPlayer.getPictureUri()), mPlayerImage);
	    } else {
		    mPlayerImage.setImageResource(R.drawable.ic_launcher);
	    }

        mFrameImage = (ImageView)view.findViewById(R.id.frame_player_card_fragment);
        mFrameImage.setImageResource(R.drawable.player_card_frame2);

        mPlayerName = (TextView)view.findViewById(R.id.name_player_card_fragment);
        mPlayerName.setText(mPlayer.getUsername());

        if (mUser == mPlayer)
            mFavoriteStar.setVisibility(View.GONE);
        else {
            mFavoriteStar.setChecked(mUser.getFavorites().contains(mPlayer) ? true : false);
            mFavoriteStar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        FullCountRestClient.post(getActivity(), "/api/users/" + mPlayer.getId() + "/follow", new JSONObject(), mAuth, true, new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        mUser.addFavorite(mPlayer);
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject error) {
                                        Log.e("Fav", "Error " + statusCode + ": " + error.toString() + " " + mPlayer.getId());
                                    }
                                }
                        );
                    else
                        FullCountRestClient.delete(getActivity(), "/api/users/" + mPlayer.getId() + "/follow", mAuth, true, new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        mUser.removeFavorite(mPlayer);
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject error) {
                                        Log.e("Fav", "Error " + statusCode + ": " + error.toString() + " " + mPlayer.getId());
                                    }
                                }
                        );
                }
            });
        }
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
