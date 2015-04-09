package edu.csulb.android.fullcount.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.facebook.Session;
import com.facebook.SessionState;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import edu.csulb.android.fullcount.FullCountApplication;
import edu.csulb.android.fullcount.R;
import edu.csulb.android.fullcount.io.models.Player;
import edu.csulb.android.fullcount.io.models.Team;
import edu.csulb.android.fullcount.tools.FullCountRestClient;


public class SplashActivity extends Activity implements Session.StatusCallback {

	static final String TAG = SplashActivity.class.getSimpleName();
	static final boolean DEBUG_MODE = FullCountApplication.DEBUG_MODE;

	static final int SPLASH_DELAY = 2000;

	private View mSplash;

	private boolean isAutoLoginChecked = false;
	private boolean isSplashTime = false;

	private Player mPlayer;

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

		checkAutoLogin(session);
	}

	private void checkAutoLogin(Session session) {
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

				session.closeAndClearTokenInformation();

				final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
				final String auth = settings.getString("auth", null);

				if (auth != null) {
					connect(auth);
				} else {
					isAutoLoginChecked = true;
					startHomeActivity();
				}

			}
		} else if (DEBUG_MODE) {
			Log.w(TAG, "Could not instantiate Facebook session");
		}
	}

	private void connectFacebook(String accessToken) {

		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("token", accessToken);

			final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			final SharedPreferences.Editor editor = settings.edit();

			editor.putString("auth", accessToken);
			editor.putBoolean("authIsBasic", false);
			editor.commit();

			FullCountRestClient.post(this, "/api/users/login/facebook", jsonObject, "", true, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					isAutoLoginChecked = true;

					if (response != null && (statusCode == 200 || statusCode == 201)) {

						if (DEBUG_MODE) {
							Log.i(TAG, "/api/users/login/facebook: " + response.toString());
						}

						try {
                            mPlayer = Player.parseFromJSON(response);

							startHomeActivity();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					} else if (response != null) {

						final Session session = Session.getActiveSession();
						if (session != null) {
							session.closeAndClearTokenInformation();
						}

						if (DEBUG_MODE) {
							Log.e(TAG, "Error " + statusCode + ": " + response.toString());
						}
					} else {

						final Session session = Session.getActiveSession();
						if (session != null) {
							session.closeAndClearTokenInformation();
						}

						if (DEBUG_MODE) {
							Log.e(TAG, "Error " + statusCode + ": " + "Response is null");
						}
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject error) {

					final Session session = Session.getActiveSession();
					if (session != null) {
						session.closeAndClearTokenInformation();
					}

					if (error != null) {
						if (DEBUG_MODE) {
							Log.e(TAG, "Error " + statusCode + ": " + error.toString());
						}
					} else {
						if (DEBUG_MODE) {
							Log.e(TAG, "Error " + statusCode + ": " + "Response is null");
						}
					}
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void connect(String auth) {
		FullCountRestClient.get("/api/users/current", null, auth, true, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				isAutoLoginChecked = true;

				if (response != null && statusCode == 200) {

					if (DEBUG_MODE) {
						Log.i(TAG, "/api/users/current: " + response.toString());
					}

					try {
						mPlayer = Player.parseFromJSON(response);

						startHomeActivity();
					} catch (JSONException e) {
						e.printStackTrace();
					}

				} else if (response != null) {

					if (DEBUG_MODE) {
						Log.e(TAG, "Error " + statusCode + ": " + response.toString());
					}

					PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().clear();

					startHomeActivity();
				} else {

					if (DEBUG_MODE) {
						Log.e(TAG, "Error " + statusCode + ": " + "Response is null");
					}
					PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().clear();

					startHomeActivity();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

				isAutoLoginChecked = true;

				if (DEBUG_MODE) {
					Log.e(TAG, "Error " + statusCode + ": " + responseString);
					throwable.printStackTrace();
				}

				startHomeActivity();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject error) {
				isAutoLoginChecked = true;

				PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().clear();

				if (error != null) {
					if (DEBUG_MODE) {
						Log.e(TAG, "Error " + statusCode + ": " + error.toString());
						throwable.printStackTrace();
					}
				} else {
					if (DEBUG_MODE) {
						Log.e(TAG, "Error " + statusCode + ": " + "Response is null");
						throwable.printStackTrace();
					}
				}

				startHomeActivity();
			}
		});
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


	private void startHomeActivity() {
		if (isFinishing()) {
			if (DEBUG_MODE) {
				Log.i(TAG, "User quit splash screen during timer");
			}
			return;
		}

		if (!isSplashTime || !isAutoLoginChecked) {
			if (DEBUG_MODE) {
				Log.w(TAG, "It's time !.. to wait some more..");
			}
			return;
		}


		Animation animation;

		if (mPlayer != null) {
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
				if (mPlayer != null) {
					Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					intent.putExtra(HomeActivity.EXTRA_PLAYER, mPlayer);
					startActivity(intent);
					finish();

					overridePendingTransition(R.anim.home_in, R.anim.splash_out);
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
}
