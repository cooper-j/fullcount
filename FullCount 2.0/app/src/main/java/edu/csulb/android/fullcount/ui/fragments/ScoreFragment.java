package edu.csulb.android.fullcount.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import edu.csulb.android.fullcount.FullCountApplication;
import edu.csulb.android.fullcount.R;
import edu.csulb.android.fullcount.io.models.Team;
import edu.csulb.android.fullcount.ui.activities.HomeActivity;
import edu.csulb.android.fullcount.ui.adapters.FieldsPagerAdapter;

public class ScoreFragment extends Fragment {

	static final String TAG = ScoreFragment.class.getSimpleName();
	static final boolean DEBUG_MODE = FullCountApplication.DEBUG_MODE;

	static final String ARGUMENT_AUTH = "AUTH";
	static final String ARGUMENT_AUTH_IS_BASIC = "AUTH_IS_BASIC";
	static final String ARGUMENT_TEAM = "TEAM";

	private String mAuthTokenString;
	private boolean mAuthIsBasic;

	private Team mTeam;

	private LinearLayout mPlayers;
	private ViewPager mPager;

	private OnFragmentInteractionListener mListener;

	public interface OnFragmentInteractionListener {
		public void onGameFinished();
	}

	public ScoreFragment() {
		// Required empty public constructor
	}

	public static ScoreFragment newInstance(String auth, boolean authIsBasic, Team team) {
		ScoreFragment fragment = new ScoreFragment();

		final Bundle args = new Bundle();
		args.putBoolean(ARGUMENT_AUTH_IS_BASIC, authIsBasic);
		args.putString(ARGUMENT_AUTH, auth);
		args.putSerializable(ARGUMENT_TEAM, team);
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle args = getArguments();
		if (args != null) {
			mAuthTokenString = args.getString(ARGUMENT_AUTH);
			mAuthIsBasic = args.getBoolean(ARGUMENT_AUTH_IS_BASIC);
			mTeam = (Team) args.getSerializable(ARGUMENT_TEAM);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View inflateView = inflater.inflate(R.layout.fragment_score, container, false);

		mPlayers = (LinearLayout) inflateView.findViewById(R.id.score_players);
		mPager = (ViewPager) inflateView.findViewById(R.id.score_pager);

		// TODO Add players to mPlayers LinearLayout
		TextView test = new TextView(getActivity());
		test.setText("FUCK YOU");

		mPlayers.addView(test);

		final FieldsPagerAdapter adapter = new FieldsPagerAdapter(getActivity(), 9); // TODO Fix
		mPager.setAdapter(adapter);

		return inflateView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		((HomeActivity) activity).onFragmentAttached(R.layout.fragment_score);

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
}
