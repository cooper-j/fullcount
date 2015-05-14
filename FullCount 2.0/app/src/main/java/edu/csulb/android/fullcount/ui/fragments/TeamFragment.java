package edu.csulb.android.fullcount.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import edu.csulb.android.fullcount.FullCountApplication;
import edu.csulb.android.fullcount.R;
import edu.csulb.android.fullcount.io.models.Team;
import edu.csulb.android.fullcount.tools.FullCountRestClient;
import edu.csulb.android.fullcount.ui.activities.HomeActivity;

public class TeamFragment extends Fragment implements View.OnClickListener {

	static final String TAG = TeamFragment.class.getSimpleName();
	static final boolean DEBUG_MODE = FullCountApplication.DEBUG_MODE;

	static final String ARGUMENT_AUTH = "AUTH";
	static final String ARGUMENT_AUTH_IS_BASIC = "AUTH_IS_BASIC";
	static final String ARGUMENT_TEAM_ID = "TEAM_ID";

	private String mAuthTokenString;
	private boolean mAuthIsBasic;
	private String mTeamId;
	public Team mTeam;

	private EditText mTeamName;
	private EditText mTeamCity;
	private Spinner mTeamLeague;
	private EditText mTeamLeagueName;
	private EditText mTeamSeason;
	private View mDoneButton;

	private OnFragmentInteractionListener mListener;

	public interface OnFragmentInteractionListener {
		public void onTeamEdition();
		public void onTeamCreation();
	}

	public TeamFragment() {
		// Required empty public constructor
	}

	public static TeamFragment newInstance(String auth, boolean authIsBasic, String teamId) {
		TeamFragment fragment = new TeamFragment();

		final Bundle args = new Bundle();
		args.putString(ARGUMENT_TEAM_ID, teamId);
		args.putBoolean(ARGUMENT_AUTH_IS_BASIC, authIsBasic);
		args.putString(ARGUMENT_AUTH, auth);
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
			mTeamId = args.getString(ARGUMENT_TEAM_ID);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View inflateView = inflater.inflate(R.layout.fragment_team, container, false);

		mTeamName = (EditText) inflateView.findViewById(R.id.team_name);
		mTeamCity = (EditText) inflateView.findViewById(R.id.team_city);
		mTeamLeague = (Spinner) inflateView.findViewById(R.id.team_league);
		mTeamLeagueName = (EditText) inflateView.findViewById(R.id.team_league_name);
		mTeamSeason = (EditText) inflateView.findViewById(R.id.team_season);
		mDoneButton = inflateView.findViewById(R.id.team_done);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.league_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mTeamLeague.setAdapter(adapter);

		mDoneButton.setOnClickListener(this);

		if (mTeamId != null) {
			FullCountRestClient.get("/api/teams/" + mTeamId, null, mAuthTokenString, mAuthIsBasic, new JsonHttpResponseHandler() {

				@Override
				public void onStart() {
					// TODO Start progress dialog
				}

				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					if (response != null && statusCode == 200) {
						if (DEBUG_MODE)	Log.i(TAG, "/api/teams: " + response.toString());
						try {
							final Team team = Team.parseFromJSON(response);
							fillTeamInformation(team);
							if (getActivity() != null) ((HomeActivity) getActivity()).player.setTeam(team);
						} catch (JSONException e) {
							if (DEBUG_MODE)	e.printStackTrace();
							Toast.makeText(getActivity(), "Internal error. Try again later.", Toast.LENGTH_SHORT).show();
						}
					} else if (response != null) {
						if (DEBUG_MODE)	Log.e(TAG, "Error " + statusCode + ": " + response.toString());
						Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
					} else {
						if (DEBUG_MODE)	Log.e(TAG, "Error " + statusCode + ": " + "Response is null");
						Toast.makeText(getActivity(), "Could not retrieve team. Try again later.", Toast.LENGTH_SHORT).show();
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
					if (responseString != null) {
						if (DEBUG_MODE) Log.e(TAG, "Error " + statusCode + ": " + responseString);
						Toast.makeText(getActivity(), responseString, Toast.LENGTH_SHORT).show();
					} else {
						if (DEBUG_MODE) Log.e(TAG, "Error " + statusCode + ": " + "Response is null");
						Toast.makeText(getActivity(), "Could not retrieve team. Try again later.", Toast.LENGTH_SHORT).show();
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
						if (DEBUG_MODE) Log.e(TAG, "Error " + statusCode + ": " + "Response is null");
						Toast.makeText(getActivity(), "Could not retrieve team. Try again later.", Toast.LENGTH_SHORT).show();
					}
					if (DEBUG_MODE && throwable != null) throwable.printStackTrace();
				}

				@Override
				public void onFinish() {
					// TODO Dismiss progress dialog
				}
			});
		}

		return inflateView;
	}


	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Team team = null;

		if (getActivity() instanceof HomeActivity) {
			team = ((HomeActivity) getActivity()).player.getTeam();
		}

		if (team != null) {
			fillTeamInformation(team);
		}
	}

	private void fillTeamInformation(Team team) {
		mTeamName.setText(team.getName());
		mTeamCity.setText(team.getCity());
		mTeamLeague.setSelection(team.getLeagueCategory());
		mTeamLeagueName.setText(team.getLeagueName());
		mTeamSeason.setText(team.getSeason());
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.team_done) {

			final String teamName = mTeamName.getText().toString();
			final String teamCity = mTeamCity.getText().toString();
			final int teamLeague = mTeamLeague.getSelectedItemPosition();
			final String teamLeagueName = mTeamLeagueName.getText().toString();
			final String teamSeason = mTeamSeason.getText().toString();

			// TODO Check data validity

			try {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("name", teamName);
				jsonObject.put("city", teamCity);
				jsonObject.put("leagueCategory", teamLeague);
				jsonObject.put("leagueName", teamLeagueName);
				jsonObject.put("season", teamSeason);

				if (mTeamId != null && !mTeamId.isEmpty()) {
					FullCountRestClient.put(getActivity(), "/api/teams/" + mTeamId, jsonObject, mAuthTokenString, mAuthIsBasic, new JsonHttpResponseHandler() {

						@Override
						public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
							if (response != null && statusCode == 200) {
								if (DEBUG_MODE)	Log.i(TAG, "PUT /api/teams/" + mTeamId + " result" + '\n' + response.toString());
								Toast.makeText(getActivity(), "Team successfully edited.", Toast.LENGTH_SHORT).show();
								try {
									final Team team = Team.parseFromJSON(response);
									if (getActivity() != null) ((HomeActivity) getActivity()).player.setTeam(team);
								} catch (JSONException e) {
									if (DEBUG_MODE)	e.printStackTrace();
									//Toast.makeText(getActivity(), "Internal error", Toast.LENGTH_SHORT).show();
								}
								if (mListener != null)	mListener.onTeamEdition();
							} else if (response != null) {
								if (DEBUG_MODE)	Log.e(TAG, "Error " + statusCode + ": " + response.toString());
								Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
							} else {
								if (DEBUG_MODE)	Log.e(TAG, "Error " + statusCode + ": " + "Response is null");
								Toast.makeText(getActivity(), "Could not edit team. Try again later.", Toast.LENGTH_SHORT).show();
							}
						}

						@Override
						public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
							if (responseString != null) {
								if (DEBUG_MODE) Log.e(TAG, "Error " + statusCode + ": " + responseString);
								Toast.makeText(getActivity(), responseString, Toast.LENGTH_SHORT).show();
							} else {
								if (DEBUG_MODE) Log.e(TAG, "Error " + statusCode + ": " + "Response is null");
								Toast.makeText(getActivity(), "Could not edit team. Try again later.", Toast.LENGTH_SHORT).show();
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
									if (DEBUG_MODE) e.printStackTrace();
									Toast.makeText(getActivity(), "Internal error. Try again later.", Toast.LENGTH_SHORT).show();
								}
							} else {
								if (DEBUG_MODE) Log.e(TAG, "Error " + statusCode + ": " + "Response is null");
								Toast.makeText(getActivity(), "Could not edit team. Try again later.", Toast.LENGTH_SHORT).show();
							}
							if (DEBUG_MODE && throwable != null) throwable.printStackTrace();
						}
					});
				} else {
					FullCountRestClient.post(getActivity(), "/api/teams", jsonObject, mAuthTokenString, mAuthIsBasic, new JsonHttpResponseHandler() {

						@Override
						public void onStart() {
							// TODO Start progress dialog
						}

						@Override
						public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
							if (response != null && statusCode == 201) {
								if (DEBUG_MODE)
									Log.i(TAG, "POST /api/teams result" + '\n' + response.toString());
								Toast.makeText(getActivity(), "Team successfully created.", Toast.LENGTH_SHORT).show();
								if (mListener != null) mListener.onTeamCreation();
							} else if (response != null) {
								if (DEBUG_MODE)
									Log.e(TAG, "Error " + statusCode + ": " + response.toString());
								Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
							} else {
								if (DEBUG_MODE)
									Log.e(TAG, "Error " + statusCode + ": " + "Response is null");
								Toast.makeText(getActivity(), "Could not create team. Try again later.", Toast.LENGTH_SHORT).show();
							}
						}

						@Override
						public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
							if (responseString != null) {
								if (DEBUG_MODE) Log.e(TAG, "Error " + statusCode + ": " + responseString);
								Toast.makeText(getActivity(), responseString, Toast.LENGTH_SHORT).show();
							} else {
								if (DEBUG_MODE) Log.e(TAG, "Error " + statusCode + ": " + "Response is null");
								Toast.makeText(getActivity(), "Could not create team. Try again later.", Toast.LENGTH_SHORT).show();
							}
							if (DEBUG_MODE && throwable != null) throwable.printStackTrace();
						}

						@Override
						public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject error) {
							if (error != null) {
								if (DEBUG_MODE)
									Log.e(TAG, "Error " + statusCode + ": " + error.toString());
								try {
									Toast.makeText(getActivity(), error.getString("message"), Toast.LENGTH_SHORT).show();
								} catch (JSONException e) {
									if (DEBUG_MODE) e.printStackTrace();
									Toast.makeText(getActivity(), "Internal error. Try again later.", Toast.LENGTH_SHORT).show();
								}
							} else {
								if (DEBUG_MODE)
									Log.e(TAG, "Error " + statusCode + ": " + "Response is null");
								Toast.makeText(getActivity(), "Could not create team. Try again later.", Toast.LENGTH_SHORT).show();
							}
							if (DEBUG_MODE && throwable != null) throwable.printStackTrace();
						}

						@Override
						public void onFinish() {
							// TODO Dismiss progress dialog
						}
					});
				}

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
			((HomeActivity) activity).onFragmentAttached(R.layout.fragment_team);
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
