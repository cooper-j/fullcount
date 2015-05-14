package edu.csulb.android.fullcount;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import edu.csulb.android.fullcount.io.models.Player;
import edu.csulb.android.fullcount.ui.activities.TestFragmentActivity;
import edu.csulb.android.fullcount.ui.fragments.ProfileEditFragment;

public class ProfileEditFragmentTest extends ActivityInstrumentationTestCase2<TestFragmentActivity> {
	private TestFragmentActivity mActivity;
	private ProfileEditFragment mFragment;

	private ImageView mPicture;
	private EditText mUsername;
	private EditText mEmail;
	private EditText mCity;
	private View mSaveButton;

	public ProfileEditFragmentTest() {
		super(TestFragmentActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
		mFragment = new ProfileEditFragment() {

			@Override
			public void onClick(View v) { }
		};
		mFragment.mPlayer = new Player("Billy");

		Fragment fragment = startFragment(mFragment);
		final View rootView = fragment.getView();
		mPicture = (ImageView) rootView.findViewById(R.id.profile_edit_picture);
		mUsername = (EditText) rootView.findViewById(R.id.profile_edit_username);
		mEmail = (EditText) rootView.findViewById(R.id.profile_edit_email);
		mCity = (EditText) rootView.findViewById(R.id.profile_edit_city);
		mSaveButton = rootView.findViewById(R.id.profile_edit_save);
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
		assertNotNull(mPicture);
		assertNotNull(mUsername);
		assertNotNull(mEmail);
		assertNotNull(mCity);
		assertNotNull(mSaveButton);
	}

	public void testProfileEdit() {
		TouchUtils.clickView(this, mSaveButton);
		getInstrumentation().waitForIdleSync();
		assertNotNull(mActivity);
	}
}