package edu.csulb.android.fullcount;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import edu.csulb.android.fullcount.io.models.Player;
import edu.csulb.android.fullcount.ui.activities.HomeActivity;
import edu.csulb.android.fullcount.ui.activities.LoginActivity;
import edu.csulb.android.fullcount.ui.activities.TestFragmentActivity;

public class LoginFragmentTest extends ActivityInstrumentationTestCase2<TestFragmentActivity> {
	private TestFragmentActivity mActivity;
	private LoginActivity.LoginFragment mFragment;

	private EditText mEmailField;
	private EditText mPasswordField;
	private View mLoginButton;
	private View mPasswordResetButton;

	public LoginFragmentTest() {
		super(TestFragmentActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
		mFragment = new LoginActivity.LoginFragment() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
					case R.id.login_login:
						Intent i = new Intent(mActivity.getBaseContext(), HomeActivity.class);
						i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						i.putExtra(HomeActivity.EXTRA_PLAYER, new Player("Billy"));
						mActivity.startActivityForResult(i, 42);
					break;

					case R.id.login_password_reset:
						Toast.makeText(getActivity(), "Reset password", Toast.LENGTH_SHORT).show();
						break;
				}
			}
		};
		Fragment fragment = startFragment(mFragment);
		final View rootView = fragment.getView();

		mEmailField = (EditText) rootView.findViewById(R.id.login_email);
		mPasswordField = (EditText) rootView.findViewById(R.id.login_password);
		mLoginButton = rootView.findViewById(R.id.login_login);
		mPasswordResetButton = rootView.findViewById(R.id.login_password_reset);
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
		assertNotNull(mEmailField);
		assertNotNull(mPasswordField);
		assertNotNull(mLoginButton);
		assertNotNull(mPasswordResetButton);
	}

	public void testLogin() {
		TouchUtils.clickView(this, mLoginButton);
		getInstrumentation().waitForIdleSync();
		assertNotNull(mActivity);
		mActivity.finishActivity(42);
	}
}
