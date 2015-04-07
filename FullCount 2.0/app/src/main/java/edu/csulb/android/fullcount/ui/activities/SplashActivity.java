package edu.csulb.android.fullcount.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import edu.csulb.android.fullcount.FullCountApplication;
import edu.csulb.android.fullcount.R;


public class SplashActivity extends Activity {

	static final String TAG = SplashActivity.class.getSimpleName();
	static final boolean DEBUG_MODE = FullCountApplication.DEBUG_MODE;

	static final int SPLASH_DELAY = 2000;

	private View mSplash;

	private boolean isSplashTime = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (DEBUG_MODE) {
			if (savedInstanceState == null) {
				Log.i(TAG, "SplashActivity started");
			} else {
				Log.i(TAG, "SplashActivity restarted");
			}
		}

		// Set layout
		setContentView(R.layout.activity_splash);

		mSplash = findViewById(R.id.splash);

		if (DEBUG_MODE) {
			try {
				PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
				for (Signature signature : info.signatures) {
					MessageDigest md = MessageDigest.getInstance("SHA");
					md.update(signature.toByteArray());
					Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
				}
			} catch (NameNotFoundException e) {

			} catch (NoSuchAlgorithmException e) {

			}
		}

		// Start timeout
		if (DEBUG_MODE) {
			Log.i(TAG, "Splash screen duration : " + SPLASH_DELAY + " ms");
		}

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (DEBUG_MODE) {
					Log.i(TAG, "Splash delay ran out");
				}
				isSplashTime = true;
				startHomeActivity();
			}
		}, SPLASH_DELAY);

		/* TODO Facebook autologin

		Session session = Session.getActiveSession();

		if (session == null) {

			if (DEBUG_MODE) {
				Log.i(TAG, "No active session found");
			}

			if (savedInstanceState != null) {
				session = Session.restoreSession(this, null, this, savedInstanceState);

				if (DEBUG_MODE) {
					Log.i(TAG, "Active session restored");
				}
			}

			if (session == null) {
				session = new Session(this);

				if (DEBUG_MODE) {
					Log.i(TAG, "New session created");
				}
			}

			Session.setActiveSession(session);
			if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
				session.openForRead(new Session.OpenRequest(this).setCallback(this));

				if (DEBUG_MODE) {
					Log.i(TAG, "Session opened for read");
				}
			}
		} else if (DEBUG_MODE) {
			Log.i(TAG, "Active session found");
		}

		checkFacebookSession(session);
		*/

	}

	/*
	private void checkFacebookSession(Session session) {
		if (session != null) {
			if (session.isOpened()) {

				if (DEBUG_MODE) {
					Log.i(TAG, "Session is opened !");
				}

				connectFacebook(session.getAccessToken());
			} else {

				if (DEBUG_MODE) {
					Log.i(TAG, "Session not opened !");
				}

				isAutoLoginChecked = true;
				session.closeAndClearTokenInformation();
			}
		} else if (DEBUG_MODE) {
			Log.w(TAG, "Could not instantiate Facebook session");
		}
	}

	private void connectFacebook(String accessToken) {
		LoginTask task = new LoginTask(this);
		task.setListener(this);
		task.setAccessToken(accessToken);
		task.execute();
	}

	@Override
	public void call(Session session, SessionState state, Exception exception) {
		if (DEBUG_MODE) {
			Log.i(TAG, "Facebook session loaded with state: " + state.toString(), exception);
		}

		if (session != null && session.isOpened()) {
			connectFacebook(session.getAccessToken());
			session.removeCallback(this);
		}
	}
	*/


	private void startHomeActivity() {
		if (isFinishing()) {
			if (DEBUG_MODE) {
				Log.i(TAG, "User quit splash screen during timer");
			}
			return;
		}

		if (!isSplashTime) { // TODO Add auto login check
			if (DEBUG_MODE) {
				Log.w(TAG, "It's time !.. to wait some more..");
			}
			return;
		}


		Animation animation;

		if (false) { // TODO Check autologin
			// TODO Change animation to fade out
			animation = AnimationUtils.loadAnimation(this, R.anim.splash_out);
		} else {
			animation = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
		}

		animation.setFillAfter(true);
		animation.setInterpolator(this, android.R.anim.anticipate_interpolator);
		animation.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) { }

			@Override
			public void onAnimationRepeat(Animation animation) { }

			@Override
			public void onAnimationEnd(Animation animation) {
				if (false) { // TODO Add autologin check
					/*
					final Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					startActivity(intent);

					finish();
					overridePendingTransition(R.anim.home_in, R.anim.splash_out);
					*/
				} else {
					final Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					startActivity(intent);

					finish();
					overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				}

			}
		});

		mSplash.startAnimation(animation);
	}

	/*
	@Override
	public void onLoginComplete() {
		isAutoLoginChecked = true;
		startHomeActivity();
	}

	@Override
	public void onLoginIncomplete() {

		final Session session = Session.getActiveSession();
		if (session != null) {
			session.closeAndClearTokenInformation();
		}

		isAutoLoginChecked = true;
		startHomeActivity();
	}
	*/

}
