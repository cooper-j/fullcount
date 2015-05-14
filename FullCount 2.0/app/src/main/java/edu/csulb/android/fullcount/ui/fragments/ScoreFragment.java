package edu.csulb.android.fullcount.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import edu.csulb.android.fullcount.FullCountApplication;
import edu.csulb.android.fullcount.R;
import edu.csulb.android.fullcount.io.models.Run;
import edu.csulb.android.fullcount.io.models.Team;
import edu.csulb.android.fullcount.ui.activities.HomeActivity;
import edu.csulb.android.fullcount.ui.adapters.FieldPagerAdapter;

public class ScoreFragment extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener {

	static final String TAG = ScoreFragment.class.getSimpleName();
	static final boolean DEBUG_MODE = FullCountApplication.DEBUG_MODE;

	static final String ARGUMENT_AUTH = "AUTH";
	static final String ARGUMENT_AUTH_IS_BASIC = "AUTH_IS_BASIC";
	static final String ARGUMENT_TEAM = "TEAM";

	private String mAuthTokenString;
	private boolean mAuthIsBasic;

	private FieldPagerAdapter mAdapter;
	public Team mTeam;

	private TextView mInning;
	private LinearLayout mPlayers;
	private ViewPager mPager;
	private View mSave;

	private OnFragmentInteractionListener mListener;

	public interface OnFragmentInteractionListener {
		public void onGameFinished(int score, JSONArray jsonScoreSheet);
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

		mInning = (TextView) inflateView.findViewById(R.id.score_inning);
		mPlayers = (LinearLayout) inflateView.findViewById(R.id.score_players);
		mPager = (ViewPager) inflateView.findViewById(R.id.score_pager);
		mSave = inflateView.findViewById(R.id.score_save);

		for (int i = 0; i < mTeam.getRoster().size() && i < 9; i++) {
			final View view = inflater.inflate(R.layout.item_score_player, mPlayers, false);
			final TextView playerName = (TextView) view.findViewById(R.id.item_score_player_name);
			playerName.setText(mTeam.getRoster().get(i).getName());
			mPlayers.addView(view);
		}

		mAdapter = new FieldPagerAdapter(getActivity(), null, mTeam.getRoster().size());
		mPager.setAdapter(mAdapter);
		mPager.setOnPageChangeListener(this);

		mSave.setOnClickListener(this);

		return inflateView;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.score_save) {
			try {
				mAdapter.saveData();

				final List<Run> runs = mAdapter.getRuns();
				final JSONArray jsonScoreSheet = new JSONArray();
				int score = 0;

				for (int i = 0; i < mTeam.getRoster().size() && i < runs.size(); i++) {
					final JSONObject jsonObject = new JSONObject();
					final JSONArray jsonRuns = new JSONArray();
					final Run run = runs.get(i);

					jsonObject.put("member", mTeam.getRoster().get(i).getId());

					for (int j = i; j < runs.size(); j = j + mTeam.getRoster().size()) {
						Run currentRun = runs.get(j);
						if (currentRun.getBases() == 4) {
							score++;
						}
						jsonRuns.put(currentRun.toJSON());
					}

					jsonObject.put("run", jsonRuns);
					jsonScoreSheet.put(jsonObject);
				}

				if (DEBUG_MODE) {
					Log.i(TAG, "Score: " + score + ", scoresheet: " + jsonScoreSheet.toString());
				}
				mListener.onGameFinished(score, jsonScoreSheet);

			} catch (JSONException e) {
				if (DEBUG_MODE) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onPageSelected(int position) {
		mInning.setText(String.valueOf(position + 1));
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (activity instanceof HomeActivity) {
			((HomeActivity) activity).onFragmentAttached(R.layout.fragment_score);
		}

		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			// throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}
}
