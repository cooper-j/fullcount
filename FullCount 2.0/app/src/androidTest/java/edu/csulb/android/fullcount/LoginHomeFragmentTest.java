package edu.csulb.android.fullcount;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.View;

import com.facebook.widget.LoginButton;

import edu.csulb.android.fullcount.ui.activities.LoginActivity;
import edu.csulb.android.fullcount.ui.activities.TestFragmentActivity;

public class LoginHomeFragmentTest extends ActivityInstrumentationTestCase2<TestFragmentActivity> {
	private TestFragmentActivity mActivity;
	private LoginActivity.LoginHomeFragment mFragment;

	private View mLoginButton;
	private LoginButton mFacebookButton;
	private View mSignUpButton;

	public LoginHomeFragmentTest() {
		super(TestFragmentActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
		mFragment = new LoginActivity.LoginHomeFragment() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
					case R.id.login_home_login: {
						final FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
						transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
						transaction.replace(R.id.activity_test_fragment, new LoginActivity.LoginFragment());
						transaction.commit();
						break;
					}

					case R.id.login_home_sign_up: {
						final FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
						transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
						transaction.replace(R.id.activity_test_fragment, new LoginActivity.SignUpFragment());
						transaction.commit();
						break;
					}
				}
			}
		};
		Fragment fragment = startFragment(mFragment);
		final View rootView = fragment.getView();
		mLoginButton = rootView.findViewById(R.id.login_home_login);
		mFacebookButton = (LoginButton) rootView.findViewById(R.id.login_home_facebook);
		mSignUpButton = rootView.findViewById(R.id.login_home_sign_up);
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
		assertNotNull(mLoginButton);
		assertNotNull(mFacebookButton);
		assertNotNull(mSignUpButton);
	}

	public void testLogin() {
		TouchUtils.clickView(this, mLoginButton);
		getInstrumentation().waitForIdleSync();
		assertNotNull(mActivity);
	}

	public void testSignUp() {
		TouchUtils.clickView(this, mSignUpButton);
		getInstrumentation().waitForIdleSync();
		assertNotNull(mActivity);
	}
}