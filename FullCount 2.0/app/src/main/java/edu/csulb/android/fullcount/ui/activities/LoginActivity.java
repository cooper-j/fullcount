package edu.csulb.android.fullcount.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import edu.csulb.android.fullcount.FullCountApplication;
import edu.csulb.android.fullcount.R;
import edu.csulb.android.fullcount.io.models.Player;
import edu.csulb.android.fullcount.tools.FullCountRestClient;
import edu.csulb.android.fullcount.tools.WebUtils;

public class LoginActivity extends FragmentActivity {

	static final String TAG = LoginActivity.class.getSimpleName();
	static final boolean DEBUG_MODE = FullCountApplication.DEBUG_MODE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		if (savedInstanceState == null) {
			displayHomeFragment();
		}
	}

	private void displayHomeFragment() {
		final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
		transaction.replace(R.id.login_container, new LoginHomeFragment());
		transaction.commit();
	}

	private void displayLoginFragment() {
		final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
		transaction.replace(R.id.login_container, new LoginFragment());
		transaction.addToBackStack(LoginFragment.class.getName());
		transaction.commit();
	}

	private void displaySignUpFragment() {
		final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
		transaction.replace(R.id.login_container, new SignUpFragment());
		transaction.addToBackStack(SignUpFragment.class.getName());
		transaction.commit();
	}

	private void startHomeScreen(Player player) {
		Intent i = new Intent(getBaseContext(), HomeActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// TODO More flags ? Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
		i.putExtra(HomeActivity.EXTRA_PLAYER, player);
		startActivity(i);
		finish();
	}

	public static class LoginHomeFragment extends Fragment implements View.OnClickListener {

		private ProgressDialog mProgress;
		private UiLifecycleHelper mUiHelper;

		private View mLoginButton;
		private LoginButton mFacebookButton;
		private View mSignUpButton;

		public LoginHomeFragment() {
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			mUiHelper = new UiLifecycleHelper(getActivity(), mCallback);
			mUiHelper.onCreate(savedInstanceState);
		}

		@Override
		public void onResume() {
			super.onResume();
			mUiHelper.onResume();
		}

		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			mUiHelper.onActivityResult(requestCode, resultCode, data);
		}

		@Override
		public void onPause() {
			super.onPause();
			mUiHelper.onPause();
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
			mUiHelper.onDestroy();
		}

		@Override
		public void onSaveInstanceState(Bundle outState) {
			super.onSaveInstanceState(outState);
			mUiHelper.onSaveInstanceState(outState);
		}

		// Called when session changes
		private Session.StatusCallback mCallback = new Session.StatusCallback() {
			@Override
			public void call(Session session, SessionState state, Exception exception) {
				onSessionStateChange(session, state, exception);
			}
		};

		private void onSessionStateChange(Session session, SessionState state, Exception exception) {
			if (state.isOpened()) {
				mFacebookButton.setEnabled(false);
				connectFacebook(session.getAccessToken());
			} else if (state.isClosed()) {
				mFacebookButton.setEnabled(true);
			}
		}

		protected void startProgressDialog() {
			startProgressDialog(null, "Loading...");
		}

		protected void startProgressDialog(String title, String message) {
			mProgress = ProgressDialog.show(getActivity(), title, message);
		}

		protected void dismissProgressDialog() {
			if (mProgress != null) {

				if (mProgress.isShowing()) {
					mProgress.dismiss();
				}

				mProgress = null;
			}
		}

		private void connectFacebook(String accessToken) {
			try {

				JSONObject jsonObject = new JSONObject();
				jsonObject.put("token", accessToken);

				final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
				final SharedPreferences.Editor editor = settings.edit();

				editor.putString("auth", accessToken);
				editor.putBoolean("authIsBasic", false);
				editor.commit();

				FullCountRestClient.post(getActivity(), "/api/users/login/facebook", jsonObject, "", true, new JsonHttpResponseHandler() {

					@Override
					public void onStart() {
						startProgressDialog();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
						if (response != null && (statusCode == 200 || statusCode == 201)) {
							if (DEBUG_MODE)	Log.i(TAG, "POST /api/users/login/facebook result" + '\n' + response.toString());
							try {
								final Player player = Player.parseFromJSON(response);
								((LoginActivity) getActivity()).startHomeScreen(player);
							} catch (JSONException e) {
								if (DEBUG_MODE) e.printStackTrace();
								Toast.makeText(getActivity(), "Internal error. Try again later", Toast.LENGTH_SHORT).show();
							}
						} else if (response != null) {
							final Session session = Session.getActiveSession();
							if (session != null) session.closeAndClearTokenInformation();
							if (DEBUG_MODE)	Log.e(TAG, "Error " + statusCode + ": " + response.toString());
							Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
						} else {
							final Session session = Session.getActiveSession();
							if (session != null) session.closeAndClearTokenInformation();
							if (DEBUG_MODE)	Log.e(TAG, "Error " + statusCode + ": " + "Response is null");
							Toast.makeText(getActivity(), "Could not log in. Try again later.", Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
						final Session session = Session.getActiveSession();
						if (session != null) session.closeAndClearTokenInformation();
						if (responseString != null) {
							if (DEBUG_MODE)	Log.e(TAG, "Error " + statusCode + ": " + responseString);
							Toast.makeText(getActivity(), responseString, Toast.LENGTH_SHORT).show();
						} else {
							if (DEBUG_MODE)	Log.e(TAG, "Error " + statusCode + ": " + "Response is null");
							Toast.makeText(getActivity(), "Could not log in. Try again later.", Toast.LENGTH_SHORT).show();
						}
						if (DEBUG_MODE && throwable != null) throwable.printStackTrace();
					}

					@Override
					public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject error) {
						final Session session = Session.getActiveSession();
						if (session != null) session.closeAndClearTokenInformation();
						if (error != null) {
							if (DEBUG_MODE)	Log.e(TAG, "Error " + statusCode + ": " + error.toString());
							try {
								Toast.makeText(getActivity(), error.getString("message"), Toast.LENGTH_SHORT).show();
							} catch (JSONException e) {
								if (DEBUG_MODE)	e.printStackTrace();
								Toast.makeText(getActivity(), "Unknown error. Try again later", Toast.LENGTH_SHORT).show();
							}
						} else {
							if (DEBUG_MODE)	Log.e(TAG, "Error " + statusCode + ": " + "Response is null");
							Toast.makeText(getActivity(), "Could not log in. Try again later.", Toast.LENGTH_SHORT).show();
						}
						if (DEBUG_MODE && throwable != null) throwable.printStackTrace();
					}

					@Override
					public void onFinish() {
						dismissProgressDialog();
					}
				});
			} catch (JSONException e) {
				if (DEBUG_MODE) e.printStackTrace();
				Toast.makeText(getActivity(), "Internal error. Try again later", Toast.LENGTH_SHORT).show();
			}

			mFacebookButton.setEnabled(true);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_login_home, container, false);

			mLoginButton = rootView.findViewById(R.id.login_home_login);
			mFacebookButton = (LoginButton) rootView.findViewById(R.id.login_home_facebook);
			mSignUpButton = rootView.findViewById(R.id.login_home_sign_up);

			mLoginButton.getBackground().setColorFilter(getResources().getColor(R.color.light_red), PorterDuff.Mode.MULTIPLY);
			mSignUpButton.getBackground().setColorFilter(getResources().getColor(R.color.green), PorterDuff.Mode.MULTIPLY);

			mFacebookButton.setFragment(this);
			mFacebookButton.setReadPermissions(Arrays.asList("user_location", "email"));

			mLoginButton.setOnClickListener(this);
			mSignUpButton.setOnClickListener(this);

			return rootView;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.login_home_login:
					((LoginActivity) getActivity()).displayLoginFragment();
					break;

				case R.id.login_home_sign_up:
					((LoginActivity) getActivity()).displaySignUpFragment();
					break;
			}
		}
	}

	public static class LoginFragment extends Fragment implements View.OnClickListener {

		private EditText mEmailField;
		private EditText mPasswordField;
		private View mLoginButton;
		private View mPasswordResetButton;

		public LoginFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_login, container, false);

			mEmailField = (EditText) rootView.findViewById(R.id.login_email);
			mPasswordField = (EditText) rootView.findViewById(R.id.login_password);
			mLoginButton = rootView.findViewById(R.id.login_login);
			mPasswordResetButton = rootView.findViewById(R.id.login_password_reset);

			mLoginButton.getBackground().setColorFilter(getResources().getColor(R.color.light_red), PorterDuff.Mode.MULTIPLY);

			mLoginButton.setOnClickListener(this);
			mPasswordResetButton.setOnClickListener(this);

			return rootView;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.login_login:
					executeLogin();
					break;

				case R.id.login_password_reset:
					// TODO Implement Reset Password view and task
					Toast.makeText(getActivity(), "Reset password", Toast.LENGTH_SHORT).show();
					break;
			}
		}

		private void executeLogin() {

			final String email = mEmailField.getText().toString();
			final String password = mPasswordField.getText().toString();

			try {

				JSONObject jsonObject = new JSONObject();
				jsonObject.put("email", email);
				jsonObject.put("password", password);

				final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
				final SharedPreferences.Editor editor = settings.edit();

				String auth = Base64.encodeToString((email + ":" + password).getBytes("UTF-8"), Base64.URL_SAFE | Base64.NO_WRAP);
				editor.putString("auth", auth);
				editor.commit();

				FullCountRestClient.post(getActivity(), "/api/users/login", jsonObject, auth, true, new JsonHttpResponseHandler() {

					@Override
					public void onStart() {
						// TODO Enable progress dialog
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

						if (response != null && statusCode == 200) {
							if (DEBUG_MODE)	Log.i(TAG, "POST /api/users/login result" + '\n' + response.toString());
							try {
								final Player player = Player.parseFromJSON(response);
								((LoginActivity) getActivity()).startHomeScreen(player);
							} catch (JSONException e) {
								if (DEBUG_MODE)	e.printStackTrace();
								Toast.makeText(getActivity(), "Internal error. Try again later", Toast.LENGTH_SHORT).show();
							}
						} else if (response != null) {
							if (DEBUG_MODE)	Log.e(TAG, "Error " + statusCode + ": " + response.toString());
							Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
						} else {
							if (DEBUG_MODE)	Log.e(TAG, "Error " + statusCode + ": " + "Response is null");
							Toast.makeText(getActivity(), "Could not log in. Try again later.", Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
						if (responseString != null) {
							if (DEBUG_MODE)	Log.e(TAG, "Error " + statusCode + ": " + responseString);
							Toast.makeText(getActivity(), responseString, Toast.LENGTH_SHORT).show();
						} else {
							if (DEBUG_MODE) Log.e(TAG, "Error " + statusCode + ": " + "Response is null");
							Toast.makeText(getActivity(), "Could not log in. Try again later.", Toast.LENGTH_SHORT).show();
						}
						if (DEBUG_MODE && throwable != null) throwable.printStackTrace();
					}

					@Override
					public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject error) {
						if (error != null) {
							if (DEBUG_MODE)	Log.e(TAG, "Error " + statusCode + ": " + error.toString());
							try {
								Toast.makeText(getActivity(), error.getString("message"), Toast.LENGTH_SHORT).show();
							} catch (JSONException e) {
								if (DEBUG_MODE) e.printStackTrace();
								Toast.makeText(getActivity(), "Internal error. Try again later.", Toast.LENGTH_SHORT).show();
							}
						} else {
							if (DEBUG_MODE)	Log.e(TAG, "Error " + statusCode + ": " + "Response is null");
							Toast.makeText(getActivity(), "Could not log in. Try again later.", Toast.LENGTH_SHORT).show();
						}
						if (DEBUG_MODE && throwable != null) throwable.printStackTrace();
					}

					@Override
					public void onFinish() {
						// TODO Disable progress dialog
					}
				});
			} catch (UnsupportedEncodingException e) {
				if (DEBUG_MODE) e.printStackTrace();
				Toast.makeText(getActivity(), "Internal error. Try again later.", Toast.LENGTH_SHORT).show();
			} catch (JSONException e) {
				if (DEBUG_MODE) e.printStackTrace();
				Toast.makeText(getActivity(), "Internal error. Try again later.", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public static class SignUpFragment extends Fragment implements View.OnClickListener {

		private EditText mUsernameField;
		private EditText mEmailField;
		private EditText mPasswordField;
		private EditText mPasswordConfirmField;
		private View mSignUpButton;

		public SignUpFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);

			mUsernameField = (EditText) rootView.findViewById(R.id.sign_up_username);
			mEmailField = (EditText) rootView.findViewById(R.id.sign_up_email);
			mPasswordField = (EditText) rootView.findViewById(R.id.sign_up_password);
			mPasswordConfirmField = (EditText) rootView.findViewById(R.id.sign_up_password_confirm);
			mSignUpButton = rootView.findViewById(R.id.sign_up_sign_up);

			mSignUpButton.getBackground().setColorFilter(getResources().getColor(R.color.green), PorterDuff.Mode.MULTIPLY);

			mSignUpButton.setOnClickListener(this);

			return rootView;
		}

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.sign_up_sign_up) {
				executeSignUp();
			}
		}

		private void executeSignUp() {

			final String username = mUsernameField.getText().toString();
			final String email = mEmailField.getText().toString();
			final String password = mPasswordField.getText().toString();
			final String passwordConfirm = mPasswordConfirmField.getText().toString();

			// Check sign-up form validity

			if (username.isEmpty() || username.length() < 4) {
				Toast.makeText(getActivity(), "Your username must be at least 4 characters long", Toast.LENGTH_SHORT).show();
				return;
			} else if (!WebUtils.isValidEmail(email)) {
				Toast.makeText(getActivity(), "Your e-mail is invalid", Toast.LENGTH_SHORT).show();
				return;
			} else if (password.isEmpty()) {
				Toast.makeText(getActivity(), "Please enter and confirm your password", Toast.LENGTH_SHORT).show();
				return;
			} else if (passwordConfirm.isEmpty()) {
				Toast.makeText(getActivity(), "Please confirm your password", Toast.LENGTH_SHORT).show();
				return;
			} else if (password.length() < 4) {
				Toast.makeText(getActivity(), "Your password must be at least 4 characters long", Toast.LENGTH_SHORT).show();
				return;
			} else if (!password.equals(passwordConfirm)) {
				Toast.makeText(getActivity(), "Your password confirmation doesn't match", Toast.LENGTH_SHORT).show();
				return;
			}

			try {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("username", username);
				jsonObject.put("email", email);
				jsonObject.put("password", password);

				final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
				final SharedPreferences.Editor editor = settings.edit();
				String auth = Base64.encodeToString((email + ":" + password).getBytes("UTF-8"), Base64.URL_SAFE | Base64.NO_WRAP);
				editor.putString("auth", auth);
				editor.apply();

				FullCountRestClient.post(getActivity(), "/api/users", jsonObject, "", true, new JsonHttpResponseHandler() {

					@Override
					public void onStart() {
						// TODO Enable progress dialog
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
						if (response != null && statusCode == 201) {
							if (DEBUG_MODE)	Log.i(TAG, "POST /api/users result" + '\n' + response.toString());
							try {
								final Player player = Player.parseFromJSON(response);
								((LoginActivity) getActivity()).startHomeScreen(player);
							} catch (JSONException e) {
								if (DEBUG_MODE)	e.printStackTrace();
								Toast.makeText(getActivity(), "Internal error. Try again later.", Toast.LENGTH_SHORT).show();
							}
						} else if (response != null) {
							if (DEBUG_MODE)	Log.e(TAG, "Error " + statusCode + ": " + response.toString());
							Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
						} else {
							if (DEBUG_MODE)	Log.e(TAG, "Error " + statusCode + ": " + "Response is null");
							Toast.makeText(getActivity(), "Could not sign up. Try again later.", Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
						if (responseString != null) {
							if (DEBUG_MODE)	Log.e(TAG, "Error " + statusCode + ": " + responseString);
							Toast.makeText(getActivity(), responseString, Toast.LENGTH_SHORT).show();
						} else {
							if (DEBUG_MODE)	Log.e(TAG, "Error " + statusCode + ": " + "Response is null");
							Toast.makeText(getActivity(), "Could not sign up. Try again later.", Toast.LENGTH_SHORT).show();
						}
						if (DEBUG_MODE && throwable != null) throwable.printStackTrace();
					}

					@Override
					public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject error) {
						if (error != null) {
							if (DEBUG_MODE)	Log.e(TAG, "Error " + statusCode + ": " + error.toString());
							try {
								Toast.makeText(getActivity(), error.getString("message"), Toast.LENGTH_SHORT).show();
							} catch (JSONException e) {
								if (DEBUG_MODE) e.printStackTrace();
								Toast.makeText(getActivity(), "Internal error. Try again later.", Toast.LENGTH_SHORT).show();
							}
						} else {
							if (DEBUG_MODE)	Log.e(TAG, "Error " + statusCode + ": " + "Response is null");
							Toast.makeText(getActivity(), "Could not sign up. Try again later.", Toast.LENGTH_SHORT).show();
						}
						if (DEBUG_MODE && throwable != null) throwable.printStackTrace();
					}

					@Override
					public void onFinish() {
						// TODO Disable progress dialog
					}
				});
			} catch (UnsupportedEncodingException e) {
				if (DEBUG_MODE) e.printStackTrace();
				Toast.makeText(getActivity(), "Internal error. Try again later.", Toast.LENGTH_SHORT).show();
			} catch (JSONException e) {
				if (DEBUG_MODE) e.printStackTrace();
				Toast.makeText(getActivity(), "Internal error. Try again later.", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
