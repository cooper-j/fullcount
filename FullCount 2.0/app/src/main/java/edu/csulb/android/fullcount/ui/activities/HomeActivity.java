package edu.csulb.android.fullcount.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.Session;

import org.json.JSONArray;

import java.util.ArrayList;

import edu.csulb.android.fullcount.FullCountApplication;
import edu.csulb.android.fullcount.R;
import edu.csulb.android.fullcount.io.models.Player;
import edu.csulb.android.fullcount.io.models.RosterMember;
import edu.csulb.android.fullcount.ui.fragments.AddPlayerTeamRosterFragment;
import edu.csulb.android.fullcount.ui.fragments.BattingRosterFragment;
import edu.csulb.android.fullcount.ui.fragments.FavoritesFragment;
import edu.csulb.android.fullcount.ui.fragments.HomeFragment;
import edu.csulb.android.fullcount.ui.fragments.NavigationDrawerFragment;
import edu.csulb.android.fullcount.ui.fragments.PlayerCardFragment;
import edu.csulb.android.fullcount.ui.fragments.ProfileEditFragment;
import edu.csulb.android.fullcount.ui.fragments.ScoreFinalFragment;
import edu.csulb.android.fullcount.ui.fragments.ScoreFragment;
import edu.csulb.android.fullcount.ui.fragments.TeamFragment;
import edu.csulb.android.fullcount.ui.fragments.TeamRosterFragment;

public class HomeActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        HomeFragment.OnFragmentInteractionListener,
        TeamFragment.OnFragmentInteractionListener,
        ProfileEditFragment.OnFragmentInteractionListener,
        BattingRosterFragment.OnFragmentInteractionListener,
        TeamRosterFragment.OnFragmentInteractionListener,
        AddPlayerTeamRosterFragment.OnFragmentInteractionListener,
		ScoreFragment.OnFragmentInteractionListener,
		ScoreFinalFragment.OnFragmentInteractionListener,
        PlayerCardFragment.OnFragmentInteractionListener,
        FavoritesFragment.OnFragmentInteractionListener {

	static final String TAG = HomeActivity.class.getSimpleName();
	static final boolean DEBUG_MODE = FullCountApplication.DEBUG_MODE;

	// Mandatory extras
	public static final String EXTRA_PLAYER = "EXTRA_PLAYER";

	// UI Elements
	private NavigationDrawerFragment mNavigationDrawerFragment;
	private CharSequence mTitle;
	private DrawerLayout nNavDrawerLayout;

	// Public attributes
	public Toolbar toolbar;
	public Player player;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		final Bundle extras = getIntent().getExtras();

		if (extras != null) {
			this.player = (Player) extras.getSerializable(EXTRA_PLAYER);
		} else {
			Toast.makeText(this, "Internal error occurred. Please try again.", Toast.LENGTH_SHORT).show();
			onNavigationDrawerItemSelected(R.id.drawer_logout);
		}

		nNavDrawerLayout = (DrawerLayout) this.findViewById(R.id.drawer_layout);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

		toolbar = (Toolbar) findViewById(R.id.home_toolbar);

		setSupportActionBar(toolbar);
	}

	@Override
	public void onNavigationDrawerItemSelected(int viewId) {
		FragmentManager fragmentManager = getSupportFragmentManager();

		final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

		final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);

		Fragment fragment = null;

		switch (viewId) {
			case R.id.drawer_picture:
			case R.id.drawer_username:
				fragment = new HomeFragment();
				break;

			case R.id.drawer_team: {
				final String auth = settings.getString("auth", "");
                String teamId = "";
                if (player.getTeam() != null && player.getTeam().getId() != null)
                    teamId = player.getTeam().getId();
				final boolean authIsBasic = settings.getBoolean("authIsBasic", true);
				fragment = TeamFragment.newInstance(auth, authIsBasic, teamId);
				break;
			}

			case R.id.drawer_create_game:
				Toast.makeText(this, "Create game", Toast.LENGTH_SHORT).show();
                if (player.getTeam() != null) {
                    fragment = BattingRosterFragment.newInstance(player.getTeam().getRoster());
                }
				break;

			case R.id.drawer_favorites: {
				final String auth = settings.getString("auth", "");
				final boolean authIsBasic = settings.getBoolean("authIsBasic", true);
				fragment = FavoritesFragment.newInstance(player);

				// Toast.makeText(this, "Favorites", Toast.LENGTH_SHORT).show();
				// TODO fragmentManager.beginTransaction().replace(R.id.container, FavoritesFragment.newInstance()).commit();
				break;
			}

			case R.id.drawer_player_card:
				Toast.makeText(this, "Player card", Toast.LENGTH_SHORT).show();

                final String auth = settings.getString("auth", "");
                fragment = PlayerCardFragment.newInstance(player, player, auth);
                break;

			case R.id.drawer_logout:
				// Clear session information
				Session.getActiveSession().closeAndClearTokenInformation();
				settings.edit().clear().apply();

				startActivity(new Intent(this, LoginActivity.class));
				finish();
				return;
		}

		if (fragment != null) {
			transaction.replace(R.id.container, fragment);
			transaction.commit();
		} else if (DEBUG_MODE) {
			Log.e(TAG, "Unexpected null fragment");
		}
	}

	public void onFragmentAttached(int layoutId) {
		switch (layoutId) {
			case R.layout.fragment_home:
				mTitle = "Home"; // TODO String
				break;

			case R.layout.fragment_team:
				mTitle = "Team"; // TODO String

			// TODO Add other fragments
		}

		getSupportActionBar().setTitle(mTitle);
	}

	public void restoreActionBar() {
		getSupportActionBar().setTitle(mTitle);
	}

    public void removeRosterMemberOnClickHandler(View v) {
        RosterMember itemToRemove = (RosterMember)v.getTag();
        this.player.getTeam().removeMemberFromRoster(itemToRemove);
    }

	@Override
	public void onTeamCreation() {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        final String auth = settings.getString("auth", "");
        final boolean authIsBasic = settings.getBoolean("authIsBasic", true);

        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.addToBackStack(TeamRosterFragment.class.getName());
        transaction.replace(R.id.container, TeamRosterFragment.newInstance(player, auth, authIsBasic));
        transaction.commit();
	}

	@Override
	public void onTeamEdition() {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        final String auth = settings.getString("auth", "");
        final boolean authIsBasic = settings.getBoolean("authIsBasic", true);

        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.addToBackStack(TeamRosterFragment.class.getName());
        transaction.replace(R.id.container, TeamRosterFragment.newInstance(player, auth, authIsBasic));
        transaction.commit();
	}

    @Override
	public void onProfileEditionClick() {
		final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		final String auth = settings.getString("auth", "");
        final boolean authIsBasic = settings.getBoolean("authIsBasic", true);

		final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
		transaction.addToBackStack(ProfileEditFragment.class.getName());
		transaction.replace(R.id.container, ProfileEditFragment.newInstance(auth, authIsBasic));
		transaction.commit();
	}

	@Override
	public void onProfileSaved() {
		getSupportFragmentManager().popBackStack();
		// TODO Pop back stack
	}

	@Override
	public void onBattingRosterNext(ArrayList<RosterMember> rosterMember) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        final String auth = settings.getString("auth", "");
        final boolean authIsBasic = settings.getBoolean("authIsBasic", true);

        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.addToBackStack(ScoreFragment.class.getName());
        transaction.replace(R.id.container, ScoreFragment.newInstance(auth, authIsBasic, player.getTeam()));
        transaction.commit();
	}


	@Override
	public void onGameFinished() {
		getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
		final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
		transaction.addToBackStack(HomeFragment.class.getName());
		transaction.replace(R.id.container, HomeFragment.newInstance());
		transaction.commit();
	}

	@Override
	public void onGameFinished(int score, JSONArray jsonScoreSheet) {
		final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		final String auth = settings.getString("auth", "");
		final boolean authIsBasic = settings.getBoolean("authIsBasic", true);

		final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
		transaction.addToBackStack(ScoreFinalFragment.class.getName());
		transaction.replace(R.id.container, ScoreFinalFragment.newInstance(auth, authIsBasic, score, jsonScoreSheet));
		transaction.commit();
	}

    @Override
    public void onAddRosterMember(RosterMember rosterMember) {
        player.getTeam().addMemberToRoster(rosterMember);
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onAddTeamPlayer() {
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.addToBackStack(AddPlayerTeamRosterFragment.class.getName());
        transaction.replace(R.id.container, AddPlayerTeamRosterFragment.newInstance());

        transaction.commit();
    }

    @Override
    public void onSaveTeamRoster(ArrayList<RosterMember> rosterMembers) {
        player.getTeam().setRoster(rosterMembers);

        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.addToBackStack(HomeFragment.class.getName());
        transaction.replace(R.id.container, HomeFragment.newInstance());
        transaction.commit();
    }

    @Override
    public void onSelectFavorite(int position) {
		final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		final String auth = settings.getString("auth", "");

        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.addToBackStack(PlayerCardFragment.class.getName());
        transaction.replace(R.id.container, PlayerCardFragment.newInstance(player, player.getFavorites().get(position), auth));
        transaction.commit();
    }

	@Override
	public void onSelectSearchedPlayer(Player p) {
		final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		final String auth = settings.getString("auth", "");

		final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
		transaction.addToBackStack(PlayerCardFragment.class.getName());
		transaction.replace(R.id.container, PlayerCardFragment.newInstance(player, p, auth));
		transaction.commit();
		nNavDrawerLayout.closeDrawers();
	}

	public void onToggleStar(View view){}

}
