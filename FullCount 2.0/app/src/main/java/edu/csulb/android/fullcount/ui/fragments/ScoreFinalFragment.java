package edu.csulb.android.fullcount.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.csulb.android.fullcount.FullCountApplication;
import edu.csulb.android.fullcount.R;
import edu.csulb.android.fullcount.tools.FullCountRestClient;
import edu.csulb.android.fullcount.ui.activities.HomeActivity;

public class ScoreFinalFragment extends Fragment implements View.OnClickListener {

	static final String TAG = ScoreFinalFragment.class.getSimpleName();
	static final boolean DEBUG_MODE = FullCountApplication.DEBUG_MODE;

	static final String ARGUMENT_AUTH = "AUTH";
	static final String ARGUMENT_AUTH_IS_BASIC = "AUTH_IS_BASIC";
	static final String ARGUMENT_SCORE = "SCORE";
	static final String ARGUMENT_JSON_SCORE_SHEET = "JSON_SCORE_SHEET";

	private String mAuthTokenString;
	private boolean mAuthIsBasic;
	private int mScore;
	private JSONArray mJsonScoreSheet;

	private EditText mOpponentName;
	private EditText mOpponentScore;
	private View mSave;

	private OnFragmentInteractionListener mListener;

	public interface OnFragmentInteractionListener {
		public void onGameFinished();
	}

	public ScoreFinalFragment() {
		// Required empty public constructor
	}

	public static ScoreFinalFragment newInstance(String auth, boolean authIsBasic, int score, JSONArray jsonScoreSheet) {
		ScoreFinalFragment fragment = new ScoreFinalFragment();

		final Bundle args = new Bundle();
		args.putBoolean(ARGUMENT_AUTH_IS_BASIC, authIsBasic);
		args.putString(ARGUMENT_AUTH, auth);
		args.putInt(ARGUMENT_SCORE, score);
		args.putString(ARGUMENT_JSON_SCORE_SHEET, jsonScoreSheet.toString());
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
			mScore = args.getInt(ARGUMENT_SCORE);
			try {
				mJsonScoreSheet = new JSONArray(args.getString(ARGUMENT_JSON_SCORE_SHEET));
			} catch (JSONException e) {
				if (DEBUG_MODE) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View inflateView = inflater.inflate(R.layout.fragment_score_final, container, false);

		mOpponentName = (EditText) inflateView.findViewById(R.id.score_final_name);
		mOpponentScore = (EditText) inflateView.findViewById(R.id.score_final_score);
		mSave = inflateView.findViewById(R.id.score_final_save);

		mSave.setOnClickListener(this);

		return inflateView;
	}
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.score_final_save) {

			final String opponentName = mOpponentName.getText().toString();
			final String opponentScore = mOpponentScore.getText().toString();

			// TODO Check data validity

			try {
				JSONObject jsonObject = new JSONObject();

				jsonObject.put("opponent", opponentName);
				jsonObject.put("opponentScore", opponentScore);
				jsonObject.put("teamScore", mScore);
				jsonObject.put("scoresheet", mJsonScoreSheet);

				Log.e(TAG, jsonObject.toString());

				FullCountRestClient.post(getActivity(), "/api/games", jsonObject, mAuthTokenString, mAuthIsBasic, new JsonHttpResponseHandler() {

					@Override
					public void onStart() {
						// TODO Start progress dialog
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
						if (response != null && (statusCode == 200 || statusCode == 201)) {
							if (DEBUG_MODE)	Log.i(TAG, "POST /api/games result" + '\n' + response.toString());
							Toast.makeText(getActivity(), "Game saved.", Toast.LENGTH_SHORT).show();
							if (mListener != null)	mListener.onGameFinished();
						} else if (response != null) {
							if (DEBUG_MODE)	Log.e(TAG, "Error " + statusCode + ": " + response.toString());
							Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
						} else {
							if (DEBUG_MODE)	Log.e(TAG, "Error " + statusCode + ": " + "Response is null");
							Toast.makeText(getActivity(), "Could not save game. Try again later.", Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
						if (responseString != null) {
							if (DEBUG_MODE) Log.e(TAG, "Error " + statusCode + ": " + responseString);
							Toast.makeText(getActivity(), responseString, Toast.LENGTH_SHORT).show();
						} else {
							if (DEBUG_MODE) Log.e(TAG, "Error " + statusCode + ": " + "Response is null");
							Toast.makeText(getActivity(), "Could not save game. Try again later.", Toast.LENGTH_SHORT).show();
						}
						if (DEBUG_MODE && throwable != null) throwable.printStackTrace();
					}

					@Override
					public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject error) {
						if (error != null) {
							if (DEBUG_MODE)	Log.e(TAG, "Error " + statusCode + ": " + error.toString());
							try {
								Toast.makeText(getActivity(), error.getString("message"), Toast.LENGTH_SHORT).show();
							} catch (JSONException e) {
								if (DEBUG_MODE)	e.printStackTrace();
								Toast.makeText(getActivity(), "Internal error. Try again later.", Toast.LENGTH_SHORT).show();
							}
						} else {
							if (DEBUG_MODE)	Log.e(TAG, "Error " + statusCode + ": " + "Response is null");
							Toast.makeText(getActivity(), "Could not save game. Try again later.", Toast.LENGTH_SHORT).show();
						}
						if (DEBUG_MODE && throwable != null) throwable.printStackTrace();
					}

					@Override
					public void onFinish() {
						// TODO Stop progress dialog
					}
				});
			} catch (JSONException e) {
				if (DEBUG_MODE)	e.printStackTrace();
				Toast.makeText(getActivity(), "Unexpected error occurred. Try again later.", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (activity instanceof HomeActivity) {
			((HomeActivity) activity).onFragmentAttached(R.layout.fragment_score_final);
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
