package edu.csulb.android.fullcount;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import edu.csulb.android.fullcount.ui.activities.LoginActivity;


public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {

	private Activity mActivity;

	public LoginActivityTest() {
		super(LoginActivity.class);
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
