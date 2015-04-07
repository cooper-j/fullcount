package edu.csulb.android.fullcount.ui.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import edu.csulb.android.fullcount.R;
import edu.csulb.android.fullcount.io.models.Player;
import edu.csulb.android.fullcount.ui.activities.HomeActivity;

public class HomeFragment extends Fragment implements View.OnClickListener {

	private OnFragmentInteractionListener mListener;

	private Player mPlayer;

	public static HomeFragment newInstance() {
		HomeFragment fragment = new HomeFragment();
		return fragment;
	}

	public HomeFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private ImageView mPlayerPicture;
	private TextView mPlayerUsername;
	private TextView mPlayerTeam;
	private TextView mPlayerCity;
	private View mProfileEdit;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View inflateView = inflater.inflate(R.layout.fragment_home, container, false);

		mPlayerPicture = (ImageView) inflateView.findViewById(R.id.home_picture);
		mPlayerUsername = (TextView) inflateView.findViewById(R.id.home_username);
		mPlayerTeam = (TextView) inflateView.findViewById(R.id.home_team);
		mPlayerCity = (TextView) inflateView.findViewById(R.id.home_city);
		mProfileEdit = inflateView.findViewById(R.id.home_profile_edit);

		mProfileEdit.setOnClickListener(this);

		return inflateView;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mPlayer = ((HomeActivity) getActivity()).player;

		// TODO Add picture
		mPlayerUsername.setText(mPlayer.getUsername());
		mPlayerTeam.setText(mPlayer.getTeam("No team")); // TODO String
		mPlayerCity.setText(mPlayer.getCity());
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.home_profile_edit) {
			// TODO Add profile edit view
			Toast.makeText(getActivity(), "Profile edit", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		((HomeActivity) activity).onFragmentAttached(R.layout.fragment_home);

		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	public interface OnFragmentInteractionListener {
		public void onFragmentInteraction(Uri uri);
	}

}
