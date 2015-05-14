package edu.csulb.android.fullcount;

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import edu.csulb.android.fullcount.io.models.Player;
import edu.csulb.android.fullcount.ui.activities.HomeActivity;


public class HomeActivityTest extends ActivityInstrumentationTestCase2<HomeActivity> {

	private Activity mActivity;

	public HomeActivityTest() {
		super(HomeActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		final Intent i = new Intent();
		i.putExtra(HomeActivity.EXTRA_PLAYER, new Player("Billy"));
		setActivityIntent(i);
		mActivity = getActivity();
	}

	public void testPreconditions() {
		assertNotNull(mActivity);
	}
}
