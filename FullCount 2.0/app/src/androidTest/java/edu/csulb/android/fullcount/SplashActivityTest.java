package edu.csulb.android.fullcount;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import edu.csulb.android.fullcount.ui.activities.SplashActivity;


public class SplashActivityTest extends ActivityInstrumentationTestCase2<SplashActivity> {

	private Activity mActivity;

	public SplashActivityTest () {
		super(SplashActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		mActivity = getActivity();
	}

	public void testPreconditions() {
		assertNotNull(mActivity);
	}
}
