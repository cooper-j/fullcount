package edu.csulb.android.fullcount.tests;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.View;

import edu.csulb.android.fullcount.ConnectionActivity;
import edu.csulb.android.fullcount.IntroActivity;
import edu.csulb.android.fullcount.LoginActivity;
import edu.csulb.android.fullcount.R;

public class IntroActivityTest extends ActivityInstrumentationTestCase2<IntroActivity> {

	private static final int ACTIVITY_LAUNCH_TIMEOUT = 200;

	private Activity mIntroActivity;
	private View mLogin;
	private View mLoginFacebook;
	private View mSignUp;

	public IntroActivityTest() {
		super(IntroActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		// Sets the initial touch mode for the Activity under test
		setActivityInitialTouchMode(true);

		// Get a reference to the Activity under test, starting it if necessary
		mIntroActivity = getActivity();

		// Get references to all views
		mLogin = mIntroActivity.findViewById(R.id.login_btn);
		mLoginFacebook = mIntroActivity.findViewById(R.id.authButton);
		mSignUp = mIntroActivity.findViewById(R.id.signUpText);
	}

	/**
	 * Tests the preconditions of this test fixture
	 */
	@MediumTest
	public void testPreConditions() {
		assertNotNull("mIntroActivity is null", mIntroActivity);
		assertNotNull("mLogin is null", mLogin);
		assertNotNull("mLoginFacebook is null", mLoginFacebook);
		assertNotNull("mSignUp is null", mSignUp);
	}

	@MediumTest
	public void testLogin() {
		// Register monitor for LoginActivity
		Instrumentation.ActivityMonitor loginActivityMonitor = getInstrumentation().addMonitor(LoginActivity.class.getName(), null, false);

		// Perform Login click
		TouchUtils.clickView(this, mLogin);

		// Wait until LoginActivity was launched and get a reference to it
		LoginActivity loginActivity = (LoginActivity) loginActivityMonitor.waitForActivityWithTimeout(ACTIVITY_LAUNCH_TIMEOUT);

		// Verify that LoginActivity was started
		assertNotNull("loginActivity is null", loginActivity);
		assertEquals("Monitor for loginActivity has not been called", 1, loginActivityMonitor.getHits());
		assertEquals("Activity is of wrong type", LoginActivity.class, loginActivity.getClass());

		// Finish LoginActivity
		// TODO Should not be done this way
		loginActivity.finish();

		// Unregister monitor for LoginActivity
		getInstrumentation().removeMonitor(loginActivityMonitor);
	}

	@MediumTest
	public void testFacebookLogin() {
		// TODO Implement
	}

	@MediumTest
	public void testSignUp() {
		// Register monitor for ConnectionActivity
		Instrumentation.ActivityMonitor connectionActivityMonitor = getInstrumentation().addMonitor(ConnectionActivity.class.getName(), null, false);

		// Perform Login click
		TouchUtils.clickView(this, mSignUp);

		// Wait until ConnectionActivity was launched and get a reference to it
		ConnectionActivity connectionActivity = (ConnectionActivity) connectionActivityMonitor.waitForActivityWithTimeout(ACTIVITY_LAUNCH_TIMEOUT);

		// Verify that ConnectionActivity was started
		assertNotNull("connectionActivity is null", connectionActivity);
		assertEquals("Monitor for connectionActivity has not been called", 1, connectionActivityMonitor.getHits());
		assertEquals("Activity is of wrong type", ConnectionActivity.class, connectionActivity.getClass());

		// Finish ConnectionActivity
		// TODO Should not be done this way
		connectionActivity.finish();

		// Unregister monitor for ConnectionActivity
		getInstrumentation().removeMonitor(connectionActivityMonitor);
	}
}
