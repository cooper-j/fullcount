package edu.csulb.android.fullcount.ui.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.csulb.android.fullcount.FullCountApplication;


import com.nostra13.universalimageloader.core.ImageLoader;


import com.nostra13.universalimageloader.core.ImageLoader;

import edu.csulb.android.fullcount.R;
import edu.csulb.android.fullcount.io.models.Player;
import edu.csulb.android.fullcount.tools.FullCountRestClient;
import edu.csulb.android.fullcount.ui.activities.HomeActivity;
import edu.csulb.android.fullcount.ui.adapters.FavoritesListAdapter;
import edu.csulb.android.fullcount.ui.adapters.PlayerSearchAdapter;

public class NavigationDrawerFragment extends Fragment implements View.OnClickListener {

    private static final boolean DEBUG_MODE = FullCountApplication.DEBUG_MODE;
	private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
	private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    private static final String ARGUMENT_AUTH = "AUTH";
    private static final String ARGUMENT_AUTH_IS_BASIC = "AUTH_IS_BASIC";
    private static String[] columns = new String[]{"_id", "FEED_ICON", "FEED_TITLE"};


    /**
	 * A pointer to the current callbacks instance (the Activity).
	 */
	private NavigationDrawerCallbacks mCallbacks;

	/**
	 * Helper component that ties the action bar to the navigation drawer.
	 */
	private ActionBarDrawerToggle mDrawerToggle;

	private DrawerLayout mDrawerLayout;
	private View mFragmentContainerView;
    private FavoritesListAdapter mPlayerAdapter;
	/**
	 * UI Elements
	 */
	private SearchView mSearchView;
	private ListView mlistView;
	private ImageView mUserPicture;
	private TextView mUsername;
	private ScrollView mMenuLayout;

	private View mTeamMenu;
	private View mCreateGameMenu;
	private View mFavoritesMenu;
	private View mPlayerCardMenu;
	private View mLogoutMenu;


	private ArrayList<Player> mSearchPlayers = new ArrayList<Player>();

    private String mAuthTokenString;
    private boolean mAuthIsBasic;

	private int mCurrentSelectedViewId = R.id.drawer_picture;
	private boolean mFromSavedInstanceState;
	private boolean mUserLearnedDrawer;

	public NavigationDrawerFragment() {
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Read in the flag indicating whether or not the user has demonstrated awareness of the
		// drawer. See PREF_USER_LEARNED_DRAWER for details.
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
		mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

		if (savedInstanceState != null) {
			mCurrentSelectedViewId = savedInstanceState.getInt(STATE_SELECTED_POSITION);
			mFromSavedInstanceState = true;
		}

        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this.getActivity());

        mAuthTokenString = settings.getString("auth", "");
        mAuthIsBasic = settings.getBoolean("authIsBasic", true);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Indicate that this fragment would like to influence the set of actions in the action bar.
		setHasOptionsMenu(true);
	}


	@Override
	public void onResume() {
		super.onResume();

		final Player player = ((HomeActivity) getActivity()).player;
		mUsername.setText(player.getUsername());

		if (player.getPictureUri() != null) {
			ImageLoader.getInstance().displayImage(FullCountRestClient.getAbsoluteUrl(player.getPictureUri()), mUserPicture);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View inflateView = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

		mSearchView = (SearchView) inflateView.findViewById(R.id.drawer_search);
		mlistView = (ListView) inflateView.findViewById(R.id.drawer_search_list);
		mUserPicture = (ImageView) inflateView.findViewById(R.id.drawer_picture);
		mUsername = (TextView) inflateView.findViewById(R.id.drawer_username);
		mMenuLayout = (ScrollView) inflateView.findViewById(R.id.drawer_menu_layout);

		mTeamMenu = inflateView.findViewById(R.id.drawer_team);
		mCreateGameMenu = inflateView.findViewById(R.id.drawer_create_game);
		mFavoritesMenu = inflateView.findViewById(R.id.drawer_favorites);
		mPlayerCardMenu = inflateView.findViewById(R.id.drawer_player_card);
		mLogoutMenu = inflateView.findViewById(R.id.drawer_logout);

		mUserPicture.setOnClickListener(this);
		mUsername.setOnClickListener(this);
		mTeamMenu.setOnClickListener(this);
		mCreateGameMenu.setOnClickListener(this);
		mFavoritesMenu.setOnClickListener(this);
		mPlayerCardMenu.setOnClickListener(this);
		mLogoutMenu.setOnClickListener(this);

        mPlayerAdapter = new FavoritesListAdapter(getActivity(), mSearchPlayers);
		mlistView.setAdapter(mPlayerAdapter);

		mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mSearchView.setQuery("", false);
				mCallbacks.onSelectSearchedPlayer(mSearchPlayers.get(position));
			}
		});

		mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

			@Override
			public boolean onQueryTextChange(String query) {

				if (query.isEmpty()) {
					mMenuLayout.setVisibility(View.VISIBLE);
					mlistView.setVisibility(View.GONE);
					return true;
				}

				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject.put("needle", query);
					jsonObject.put("offset", 0);
					jsonObject.put("limit", 5);

					FullCountRestClient.post(getActivity(), "/api/users/search", jsonObject, "", false, new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
							if (statusCode == 200) {
								try {
									mSearchPlayers.clear();
									for (int i = 0; i < response.length(); i++) {
										mSearchPlayers.add(Player.parseFromJSON(response.getJSONObject(i)));
									}
								} catch (JSONException e) {
									if (DEBUG_MODE) {
										e.printStackTrace();
									}
									Toast.makeText(getActivity(), "Unexpected error occurred. Try again later.", Toast.LENGTH_SHORT).show();
								}
								mPlayerAdapter.notifyDataSetChanged();
								/*Animation fadeOut = new AlphaAnimation(1, 0);
								fadeOut.setInterpolator(new AccelerateInterpolator());
								fadeOut.setStartOffset(1000);
								fadeOut.setDuration(1000);
								mMenuLayout.setAnimation(fadeOut);*/
								mMenuLayout.setVisibility(View.GONE);
								mlistView.setVisibility(View.VISIBLE);
							}
						}

						@Override
						public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
							super.onFailure(statusCode, headers, throwable, errorResponse);
						}
					});

				} catch (JSONException e) {
					if (DEBUG_MODE) {
						e.printStackTrace();
					}
					Toast.makeText(getActivity(), "Unexpected error occurred. Try again later.", Toast.LENGTH_SHORT).show();
				}
				return false;
			}

			@Override
			public boolean onQueryTextSubmit(String query) {
				return false;
            }
        });

		selectItem(mCurrentSelectedViewId);

		return inflateView;
	}

	@Override
	public void onClick(View v) {
		if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView)) {
			mDrawerLayout.closeDrawer(mFragmentContainerView);
		}

		selectItem(v.getId());
	}

	public boolean isDrawerOpen() {
		return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
	}

	/**
	 * Users of this fragment must call this method to set up the navigation drawer interactions.
	 *
	 * @param fragmentId   The android:id of this fragment in its activity's layout.
	 * @param drawerLayout The DrawerLayout containing this fragment's UI.
	 */
	public void setUp(int fragmentId, DrawerLayout drawerLayout) {
		mFragmentContainerView = getActivity().findViewById(fragmentId);
		mDrawerLayout = drawerLayout;

		// set a custom shadow that overlays the main content when the drawer opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		// set up the drawer's list view with items and click listener

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the navigation drawer and the action bar app icon.
		mDrawerToggle = new ActionBarDrawerToggle(getActivity(),                    /* host Activity */
				mDrawerLayout,                    /* DrawerLayout object */
				((HomeActivity) getActivity()).toolbar,             /* nav drawer image to replace 'Up' caret */
				R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
				R.string.navigation_drawer_close  /* "close drawer" description for accessibility */) {
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				if (!isAdded()) {
					return;
				}

				getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				if (!isAdded()) {
					return;
				}

				if (!mUserLearnedDrawer) {
					// The user manually opened the drawer; store this flag to prevent auto-showing
					// the navigation drawer automatically in the future.
					mUserLearnedDrawer = true;
					SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
					sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
				}

				getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
			}
		};

		// If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
		// per the navigation drawer design guidelines.
		if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
			mDrawerLayout.openDrawer(mFragmentContainerView);
		}

		// Defer code dependent on restoration of previous instance state.
		mDrawerLayout.post(new Runnable() {
			@Override
			public void run() {
				mDrawerToggle.syncState();
			}
		});

		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	private void selectItem(int viewId) {
		mCurrentSelectedViewId = viewId;

		if (mCallbacks != null) {
			mCallbacks.onNavigationDrawerItemSelected(viewId);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallbacks = (NavigationDrawerCallbacks) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedViewId);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Forward the new configuration the drawer toggle component.
		mDrawerToggle.onConfigurationChanged(newConfig);
	}


	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// If the drawer is open, show the global app actions in the action bar. See also
		// showGlobalContextActionBar, which controls the top-left area of the action bar.
		if (mDrawerLayout != null && isDrawerOpen()) {
			// inflater.inflate(R.menu.global, menu);
			showGlobalContextActionBar();
		}
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		/*
		if (item.getItemId() == R.id.action_example) {
			Toast.makeText(getActivity(), "Example action.", Toast.LENGTH_SHORT).show();
			return true;
		}
		*/

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Per the navigation drawer design guidelines, updates the action bar to show the global app
	 * 'context', rather than just what's in the current screen.
	 */
	private void showGlobalContextActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		// actionBar.setTitle(R.string.app_name);
	}

	private ActionBar getActionBar() {
		return ((ActionBarActivity) getActivity()).getSupportActionBar();
	}

	/**
	 * Callbacks interface that all activities using this fragment must implement.
	 */
	public static interface NavigationDrawerCallbacks {
		/**
		 * Called when an item in the navigation drawer is selected.
		 */
		void onNavigationDrawerItemSelected(int viewId);
		void onSelectSearchedPlayer(Player player);
	}
}
