package edu.csulb.android.fullcount.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.facebook.Session;

import edu.csulb.android.fullcount.FullCountApplication;
import edu.csulb.android.fullcount.R;
import edu.csulb.android.fullcount.io.models.Player;
import edu.csulb.android.fullcount.ui.fragments.HomeFragment;
import edu.csulb.android.fullcount.ui.fragments.NavigationDrawerFragment;
import edu.csulb.android.fullcount.ui.fragments.TeamFragment;

public class HomeActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks, HomeFragment.OnFragmentInteractionListener, TeamFragment.OnFragmentInteractionListener {

	static final String TAG = HomeActivity.class.getSimpleName();
	static final boolean DEBUG_MODE = FullCountApplication.DEBUG_MODE;

	// Mandatory extras
	public static final String EXTRA_PLAYER = "EXTRA_PLAYER";

	// UI Elements
	private NavigationDrawerFragment mNavigationDrawerFragment;
	private CharSequence mTitle;

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

			case R.id.drawer_team:
				final String auth = settings.getString("auth", "");
				final String teamId = settings.getString("teamId", "");
				final boolean authIsBasic = settings.getBoolean("authIsBasic", true);
				fragment = TeamFragment.newInstance(auth, authIsBasic, teamId);
				transaction.addToBackStack(TeamFragment.class.getName());
				break;

			case R.id.drawer_create_game:
				Toast.makeText(this, "Create game", Toast.LENGTH_SHORT).show();
				// TODO fragmentManager.beginTransaction().replace(R.id.container, CreateGameFragment.newInstance()).commit();
				break;

			case R.id.drawer_favorites:
				Toast.makeText(this, "Favorites", Toast.LENGTH_SHORT).show();
				// TODO fragmentManager.beginTransaction().replace(R.id.container, FavoritesFragment.newInstance()).commit();
				break;

			case R.id.drawer_player_card:
				Toast.makeText(this, "Player card", Toast.LENGTH_SHORT).show();
				// TODO fragmentManager.beginTransaction().replace(R.id.container, PlayerCard.newInstance()).commit();
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

	@Override
	public void onFragmentInteraction(Uri uri) {

	}

	@Override
	public void onTeamCreation() {
		// TODO Add team roster fragment
	}

	@Override
	public void onTeamEdition() {
		// TODO Add team roster fragment
	}
}
