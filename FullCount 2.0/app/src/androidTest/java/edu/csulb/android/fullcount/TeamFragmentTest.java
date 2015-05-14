package edu.csulb.android.fullcount;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import edu.csulb.android.fullcount.io.models.Team;
import edu.csulb.android.fullcount.ui.activities.TestFragmentActivity;
import edu.csulb.android.fullcount.ui.fragments.TeamFragment;

public class TeamFragmentTest extends ActivityInstrumentationTestCase2<TestFragmentActivity> {
	private TestFragmentActivity mActivity;
	private TeamFragment mFragment;

	private EditText mTeamName;
	private EditText mTeamCity;
	private Spinner mTeamLeague;
	private EditText mTeamLeagueName;
	private EditText mTeamSeason;
	private View mDoneButton;

	public TeamFragmentTest() {
		super(TestFragmentActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
		mFragment = new TeamFragment() {
			@Override
			public void onClick(View v) { }
		};
		mFragment.mTeam = new Team(null);

		Fragment fragment = startFragment(mFragment);
		final View rootView = fragment.getView();
		mTeamName = (EditText) rootView.findViewById(R.id.team_name);
		mTeamCity = (EditText) rootView.findViewById(R.id.team_city);
		mTeamLeague = (Spinner) rootView.findViewById(R.id.team_league);
		mTeamLeagueName = (EditText) rootView.findViewById(R.id.team_league_name);
		mTeamSeason = (EditText) rootView.findViewById(R.id.team_season);
		mDoneButton = rootView.findViewById(R.id.team_done);
	}

	private Fragment startFragment(Fragment fragment) {
		FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.activity_test_fragment, fragment, "tag");
		transaction.commit();
		getInstrumentation().waitForIdleSync();
		return mActivity.getSupportFragmentManager().findFragmentByTag("tag");
	}

	public void testPreconditions() {
		assertNotNull(mActivity);
		assertNotNull(mFragment);
		assertNotNull(mFragment.getView());
		assertNotNull(mTeamName);
		assertNotNull(mTeamCity);
		assertNotNull(mTeamLeague);
		assertNotNull(mTeamLeagueName);
		assertNotNull(mTeamSeason);
		assertNotNull(mDoneButton);
	}

	public void testSaveTeam() {
		TouchUtils.clickView(this, mDoneButton);
		getInstrumentation().waitForIdleSync();
		assertNotNull(mActivity);
	}
}