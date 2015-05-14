package edu.csulb.android.fullcount;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.View;
import android.widget.EditText;

import edu.csulb.android.fullcount.io.models.Player;
import edu.csulb.android.fullcount.ui.activities.HomeActivity;
import edu.csulb.android.fullcount.ui.activities.LoginActivity;
import edu.csulb.android.fullcount.ui.activities.TestFragmentActivity;

public class SignUpFragmentTest extends ActivityInstrumentationTestCase2<TestFragmentActivity> {
	private TestFragmentActivity mActivity;
	private LoginActivity.SignUpFragment mFragment;

	private EditText mUsernameField;
	private EditText mEmailField;
	private EditText mPasswordField;
	private EditText mPasswordConfirmField;
	private View mSignUpButton;

	public SignUpFragmentTest() {
		super(TestFragmentActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
		mFragment = new LoginActivity.SignUpFragment() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
					case R.id.sign_up_sign_up: {
						Intent i = new Intent(mActivity.getBaseContext(), HomeActivity.class);
						i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						i.putExtra(HomeActivity.EXTRA_PLAYER, new Player("Billy"));
						mActivity.startActivityForResult(i, 42);
						break;
					}
				}
			}
		};
		Fragment fragment = startFragment(mFragment);
		final View rootView = fragment.getView();

		mUsernameField = (EditText) rootView.findViewById(R.id.sign_up_username);
		mEmailField = (EditText) rootView.findViewById(R.id.sign_up_email);
		mPasswordField = (EditText) rootView.findViewById(R.id.sign_up_password);
		mPasswordConfirmField = (EditText) rootView.findViewById(R.id.sign_up_password_confirm);
		mSignUpButton = rootView.findViewById(R.id.sign_up_sign_up);
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
		assertNotNull(mUsernameField);
		assertNotNull(mEmailField);
		assertNotNull(mPasswordField);
		assertNotNull(mPasswordConfirmField);
		assertNotNull(mSignUpButton);
	}

	public void testSignUp() {
		TouchUtils.clickView(this, mSignUpButton);
		getInstrumentation().waitForIdleSync();
		assertNotNull(mActivity);
		mActivity.finishActivity(42);
	}
}
