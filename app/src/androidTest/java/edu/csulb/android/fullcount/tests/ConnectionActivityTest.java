package edu.csulb.android.fullcount.tests;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.View;

import edu.csulb.android.fullcount.ConnectionActivity;
import edu.csulb.android.fullcount.HomeActivity;
import edu.csulb.android.fullcount.R;

public class ConnectionActivityTest extends ActivityInstrumentationTestCase2<ConnectionActivity> {

	private static final int ACTIVITY_LAUNCH_TIMEOUT = 200;
	private static final int NETWORK_TIMEOUT = 50000;

	private static final String FIELD_USERNAME = "toto";
	private static final String FIELD_EMAIL_DOMAIN = "toto.com";
	private static final String FIELD_PASSWORD = "toto42";

	private Activity mConnectionActivity;
	private View mUsername;
	private View mEmail;
	private View mPassword;
	private View mPasswordConfirm;
	private View mConfirm;

	public ConnectionActivityTest() {
		super(ConnectionActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		// Sets the initial touch mode for the Activity under test
		// setActivityInitialTouchMode(true);

		// Get a reference to the Activity under test, starting it if necessary
		mConnectionActivity = getActivity();

		// Get references to all views
		mUsername = mConnectionActivity.findViewById(R.id.UserField);
		mEmail = mConnectionActivity.findViewById(R.id.EmailField);
		mPassword = mConnectionActivity.findViewById(R.id.Password1Field);
		mPasswordConfirm = mConnectionActivity.findViewById(R.id.Password2Field);
		mConfirm = mConnectionActivity.findViewById(R.id.AccountCreateButton);
	}

	/**
	 * Tests the preconditions of this test fixture
	 */
	@MediumTest
	public void testPreConditions() {
		assertNotNull("mConnectionActivity is null", mConnectionActivity);
		assertNotNull("mUsername is null", mUsername);
		assertNotNull("mEmail is null", mEmail);
		assertNotNull("mPassword is null", mPassword);
		assertNotNull("mPasswordConfirm is null", mPasswordConfirm);
		assertNotNull("mConfirm is null", mConfirm);
	}

	public void fillConnectionData() {

		//GORE Generate username
		String username = FIELD_USERNAME + (Math.random() % 100000);

		// Request focus on the username EditText field
		getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				mUsername.requestFocus();
			}
		});

		// Wait until all events from the MainHandler's queue are processed
		getInstrumentation().waitForIdleSync();

		// Input the text
		getInstrumentation().sendStringSync(username);
		getInstrumentation().waitForIdleSync();

		// Do the same for the email
		getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				mEmail.requestFocus();
			}
		});
		getInstrumentation().waitForIdleSync();
		getInstrumentation().sendStringSync(username + '@' + FIELD_EMAIL_DOMAIN);
		getInstrumentation().waitForIdleSync();

		// Do the same for the password
		getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				mPassword.requestFocus();
			}
		});
		getInstrumentation().waitForIdleSync();
		getInstrumentation().sendStringSync(FIELD_PASSWORD);
		getInstrumentation().waitForIdleSync();

		// Do the same for the password confirmation
		getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				mPasswordConfirm.requestFocus();
			}
		});
		getInstrumentation().waitForIdleSync();
		getInstrumentation().sendStringSync(FIELD_PASSWORD);
		getInstrumentation().waitForIdleSync();
	}

	@LargeTest
	public void testConnection() {
		// Register monitor for HomeActivity
		Instrumentation.ActivityMonitor homeActivityMonitor = getInstrumentation().addMonitor(HomeActivity.class.getName(), null, false);

		fillConnectionData();

		/* TODO Spoof server response
		// Generate fake server response
		JSONObject response = new JSONObject();

		try {
			JSONObject token = new JSONObject();
			token.put("value", "qCBtfH6MVUzFpWwS4Dsaq3bSKKqtJdU0fvWONPT4HGPAmocU37OLVnDNp8ymQ5fITci45fjECxcbwmhzi17R746yiAouEzTJv9cfhULxdjPLbem2ZbmYNxkmgKoxiop0fJHXocNHNywnUToxty9mFiY7CegEk02JAyRrZNO9OW1a2vMUrAcz7qauBLZnqZ4nkG2D3HgfKctWFRHukTWN9Y4uw5xyM1agclhzTfZmmJnfFeX8rRGv2TDq3D8k7hOu");
			token.put("expirationDate", "2015-04-05T23:21:44.020Z");

			JSONObject user = new JSONObject();
			user.put("username", FIELD_USERNAME);
			user.put("email", FIELD_EMAIL);
			user.put("registrationDate", "2015-03-07T00:21:44.069Z");
			user.put("lastConnection", "2015-03-07T01:34:43.037Z");
			user.put("_id", "54fa44980f8f95824259583f");

			response.put("token", token);
			response.put("user", user);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		stubFor(put(urlMatching("/api/users")).willReturn(aResponse().withStatus(201).withBody(response.toString())));
		*/

		// Perform Confirm click
		getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				mConfirm.requestFocus();
				mConfirm.performClick();
			}
		});

		getInstrumentation().waitForIdleSync();

		// Wait until HomeActivity was launched and get a reference to it
		HomeActivity homeActivity = (HomeActivity) homeActivityMonitor.waitForActivityWithTimeout(NETWORK_TIMEOUT);

		// Verify that HomeActivity was started
		assertNotNull("homeActivity is null", homeActivity);
		assertEquals("Monitor for homeActivity has not been called", 1, homeActivityMonitor.getHits());
		assertEquals("Activity is of wrong type", HomeActivity.class, homeActivity.getClass());

		// Finish HomeActivity
		// TODO Should not be done this way
		homeActivity.finish();

		// Unregister monitor for HomeActivity
		getInstrumentation().removeMonitor(homeActivityMonitor);
	}
}
