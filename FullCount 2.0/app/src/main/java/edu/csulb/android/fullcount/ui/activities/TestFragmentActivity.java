package edu.csulb.android.fullcount.ui.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import org.json.JSONArray;

import java.util.ArrayList;

import edu.csulb.android.fullcount.R;
import edu.csulb.android.fullcount.io.models.RosterMember;
import edu.csulb.android.fullcount.ui.fragments.AddPlayerTeamRosterFragment;
import edu.csulb.android.fullcount.ui.fragments.BattingRosterFragment;
import edu.csulb.android.fullcount.ui.fragments.FavoritesFragment;
import edu.csulb.android.fullcount.ui.fragments.HomeFragment;
import edu.csulb.android.fullcount.ui.fragments.PlayerCardFragment;
import edu.csulb.android.fullcount.ui.fragments.ProfileEditFragment;
import edu.csulb.android.fullcount.ui.fragments.ScoreFinalFragment;
import edu.csulb.android.fullcount.ui.fragments.ScoreFragment;
import edu.csulb.android.fullcount.ui.fragments.TeamFragment;
import edu.csulb.android.fullcount.ui.fragments.TeamRosterFragment;

public class TestFragmentActivity extends FragmentActivity implements HomeFragment.OnFragmentInteractionListener, TeamFragment.OnFragmentInteractionListener, ProfileEditFragment.OnFragmentInteractionListener, BattingRosterFragment.OnFragmentInteractionListener, TeamRosterFragment.OnFragmentInteractionListener, AddPlayerTeamRosterFragment.OnFragmentInteractionListener, ScoreFragment.OnFragmentInteractionListener, ScoreFinalFragment.OnFragmentInteractionListener, PlayerCardFragment.OnFragmentInteractionListener, FavoritesFragment.OnFragmentInteractionListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
	}

	@Override
	public void onTeamCreation() { }

	@Override
	public void onTeamEdition() { }

	@Override
	public void onProfileEditionClick() { }

	@Override
	public void onProfileSaved() { }

	@Override
	public void onBattingRosterNext(ArrayList<RosterMember> rosterMember) { }

	@Override
	public void onGameFinished() { }

	@Override
	public void onGameFinished(int score, JSONArray jsonScoreSheet) { }

	@Override
	public void onAddRosterMember(RosterMember rosterMember) { }

	@Override
	public void onAddTeamPlayer() { }

	@Override
	public void onSaveTeamRoster(ArrayList<RosterMember> rosterMembers) { }

	@Override
	public void onSelectFavorite(int position) { }
}