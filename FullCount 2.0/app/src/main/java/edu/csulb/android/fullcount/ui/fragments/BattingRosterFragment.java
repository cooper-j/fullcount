package edu.csulb.android.fullcount.ui.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import edu.csulb.android.fullcount.R;
import edu.csulb.android.fullcount.io.models.RosterMember;
import edu.csulb.android.fullcount.ui.adapters.RosterMemberListAdapter;
import edu.csulb.android.fullcount.ui.widgets.DynamicRosterMemberListView;

public class BattingRosterFragment extends Fragment implements View.OnClickListener {

    static final String ARGUMENT_PLAYER_LIST = "ROSTER_MEMBER_LIST";

    private Button mNextButton;
    private ArrayList<RosterMember> mRosterMemberList;

    private RosterMemberListAdapter mAdapter;
    private OnFragmentInteractionListener mListener;

    /**
     *
     * @param param1
     * @return
     */

    public static BattingRosterFragment newInstance(List<RosterMember> param1) {
        BattingRosterFragment fragment = new BattingRosterFragment();

        final Bundle args = new Bundle();
        args.putSerializable(ARGUMENT_PLAYER_LIST, (ArrayList<RosterMember>)param1);

        fragment.setArguments(args);
        return fragment;
    }

    public BattingRosterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRosterMemberList = (ArrayList<RosterMember>)getArguments().getSerializable(ARGUMENT_PLAYER_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_batting_roster, container, false);

        mNextButton = (Button)view.findViewById(R.id.batting_roster_fragment_next);
        mNextButton.setOnClickListener(this);

        mAdapter = new RosterMemberListAdapter(getActivity(), mRosterMemberList);
        DynamicRosterMemberListView listView  = (DynamicRosterMemberListView)view.findViewById(R.id.listview);

        listView.setRosterMemberList(mRosterMemberList);
        listView.setAdapter(mAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        // Inflate the layout for this fragment
        return view;
    }

    public void onClick(View v) {
        mListener.onBattingRosterNext(mAdapter.getRosterMemberList());
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
	    public void onBattingRosterNext(ArrayList<RosterMember> rosterMember);
    }

}
