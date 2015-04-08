package edu.csulb.android.fullcount.ui.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import edu.csulb.android.fullcount.R;
import edu.csulb.android.fullcount.io.models.RosterMember;
import edu.csulb.android.fullcount.ui.adapters.RosterMemberListAdapter;
import edu.csulb.android.fullcount.ui.widgets.DynamicRosterMemberListView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BattingRosterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BattingRosterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BattingRosterFragment extends Fragment implements View.OnClickListener {

    static final String ARGUMENT_PLAYER_LIST = "PLAYER_LIST";

    private ArrayList<RosterMember> mRosterMemberList;

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

        RosterMemberListAdapter adapter = new RosterMemberListAdapter(getActivity(), mRosterMemberList);
        DynamicRosterMemberListView listView  = (DynamicRosterMemberListView)view.findViewById(R.id.listview);

        listView.setRosterMemberList(mRosterMemberList);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        // Inflate the layout for this fragment
        return view;
    }

    public void onClick(View v) {

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
