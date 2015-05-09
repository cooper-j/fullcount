package edu.csulb.android.fullcount.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.csulb.android.fullcount.R;
import edu.csulb.android.fullcount.io.models.Player;
import edu.csulb.android.fullcount.io.models.RosterMember;
import edu.csulb.android.fullcount.tools.FullCountRestClient;
import edu.csulb.android.fullcount.ui.adapters.TeamRosterListAdapter;

public class TeamRosterFragment extends Fragment implements View.OnClickListener {
    static final String ARGUMENT_PLAYER = "PLAYER";
    static final String ARGUMENT_AUTH = "AUTH";
    static final String ARGUMENT_AUTH_IS_BASIC = "AUTH_IS_BASIC";

    private Player mPlayer;
    private String mAuthTokenString;
    private boolean mAuthIsBasic;

    private TeamRosterListAdapter mAdapter;
    private Button mAddPlayerButton;
    private Button mSaveButton;

    private OnFragmentInteractionListener mListener;


    public static TeamRosterFragment newInstance(Player player, String auth, boolean authIsBasic) {
        TeamRosterFragment fragment = new TeamRosterFragment();

        final Bundle args = new Bundle();
        args.putSerializable(ARGUMENT_PLAYER, player);
        args.putBoolean(ARGUMENT_AUTH_IS_BASIC, authIsBasic);
        args.putString(ARGUMENT_AUTH, auth);
        fragment.setArguments(args);
        return fragment;
    }

    public TeamRosterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mPlayer = (Player)args.getSerializable(ARGUMENT_PLAYER);
            mAuthTokenString = args.getString(ARGUMENT_AUTH);
            mAuthIsBasic = args.getBoolean(ARGUMENT_AUTH_IS_BASIC);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_roster, container, false);

        mAddPlayerButton = (Button)view.findViewById(R.id.team_fragment_add_player);
        mSaveButton = (Button)view.findViewById(R.id.team_fragment_save);

        mAddPlayerButton.setOnClickListener(this);
        mSaveButton.setOnClickListener(this);

        mAdapter = new TeamRosterListAdapter(getActivity(), (ArrayList<RosterMember>)mPlayer.getTeam().getRoster());
        ListView listView = (ListView)view.findViewById(R.id.team_roster_list);
        listView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.team_fragment_add_player)
            mListener.onAddTeamPlayer();
        else if (v.getId() == R.id.team_fragment_save){
            JSONArray jsonArray = new JSONArray();
            for (RosterMember rosterMember : mPlayer.getTeam().getRoster()) {
                JSONObject jsonobj = new JSONObject();
                try {
                    jsonobj.put("name", rosterMember.getName());
                } catch (JSONException je) {
                    je.printStackTrace();
                }
                jsonArray.put(jsonobj);
            }
            JSONObject jsonParams = new JSONObject();
            try {
                jsonParams.put("members", jsonArray);
            } catch (JSONException je) {
                je.printStackTrace();
            }

                FullCountRestClient.put(getActivity(), "/api/teams/" + mPlayer.getTeam().getId() + "/members", jsonParams, mAuthTokenString, mAuthIsBasic, new JsonHttpResponseHandler() {

	                @Override
	                public void onStart() {
		                // TODO Start progress dialog
	                }

	                @Override
		            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
		                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();

		                ArrayList<RosterMember> rosterMembers = new ArrayList<>();

		                for (int i = 0; i < response.length(); i++){
		                    try {
		                        JSONObject obj = response.getJSONObject(i);
		                        RosterMember rosterMember = new RosterMember(obj.getString("_id"));
		                        rosterMember.setName(obj.getString("name"));
		                        rosterMembers.add(rosterMember);
		                    }catch (JSONException e){
		                        e.printStackTrace();
		                    }
		                }

		                mListener.onSaveTeamRoster(rosterMembers);
		            }

		            @Override
		            public void onFailure(int statusCode, Header[] headers, String error, Throwable throwable) {
		                Toast.makeText(getActivity(), "Error: " + statusCode + " " + error, Toast.LENGTH_SHORT).show();
		            }

	                @Override
	                public void onFinish() {
		                // TODO Dismiss progress dialog
	                }
                });
        }

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

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public interface OnFragmentInteractionListener {
        public void onAddTeamPlayer();
        public void onSaveTeamRoster(ArrayList<RosterMember> rosterMembers);
    }

}
