package edu.csulb.android.fullcount.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import edu.csulb.android.fullcount.R;
import edu.csulb.android.fullcount.io.models.RosterMember;

public class AddPlayerTeamRosterFragment extends Fragment implements View.OnClickListener {


    // TODO: Rename and change types of parameters
    private Button mAddButton;
    private EditText mName;

    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types and number of parameters
    public static AddPlayerTeamRosterFragment newInstance() {
        AddPlayerTeamRosterFragment fragment = new AddPlayerTeamRosterFragment();
        return fragment;
    }

    public AddPlayerTeamRosterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_player_team_roster, container, false);

        mName = (EditText) view.findViewById(R.id.add_team_player_name);
        mAddButton = (Button) view.findViewById(R.id.team_player_add);

        mAddButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        RosterMember rosterMember = new RosterMember();
        rosterMember.setName(mName.getText().toString());
        mListener.onAddRosterMember(rosterMember);
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
        public void onAddRosterMember(RosterMember rosterMember);
    }
}
