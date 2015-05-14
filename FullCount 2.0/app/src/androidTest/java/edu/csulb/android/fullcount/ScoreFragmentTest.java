package edu.csulb.android.fullcount;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import edu.csulb.android.fullcount.io.models.RosterMember;
import edu.csulb.android.fullcount.io.models.Team;
import edu.csulb.android.fullcount.ui.activities.TestFragmentActivity;
import edu.csulb.android.fullcount.ui.fragments.ScoreFragment;

public class ScoreFragmentTest extends ActivityInstrumentationTestCase2<TestFragmentActivity> {
	private TestFragmentActivity mActivity;
	private ScoreFragment mFragment;

	private TextView mInning;
	private LinearLayout mPlayers;
	private ViewPager mPager;
	private View mSave;

	public ScoreFragmentTest() {
		super(TestFragmentActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
		mFragment = new ScoreFragment() {

			@Override
			public void onClick(View v) { }
		};
		mFragment.mTeam = new Team(null);
		mFragment.mTeam.setRoster(new ArrayList<RosterMember>());
		Fragment fragment = startFragment(mFragment);
		final View rootView = fragment.getView();
		mInning = (TextView) rootView.findViewById(R.id.score_inning);
		mPlayers = (LinearLayout) rootView.findViewById(R.id.score_players);
		mPager = (ViewPager) rootView.findViewById(R.id.score_pager);
		mSave = rootView.findViewById(R.id.score_save);	}

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
		assertNotNull(mInning);
		assertNotNull(mPlayers);
		assertNotNull(mPager);
		assertNotNull(mSave);
	}

	public void testSaveScore() {
		TouchUtils.clickView(this, mSave);
		getInstrumentation().waitForIdleSync();
		assertNotNull(mActivity);
	}
}