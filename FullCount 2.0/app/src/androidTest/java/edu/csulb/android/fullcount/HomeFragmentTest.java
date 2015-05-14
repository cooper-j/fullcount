package edu.csulb.android.fullcount;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import edu.csulb.android.fullcount.io.models.Player;
import edu.csulb.android.fullcount.ui.activities.LoginActivity;
import edu.csulb.android.fullcount.ui.activities.TestFragmentActivity;
import edu.csulb.android.fullcount.ui.fragments.HomeFragment;

public class HomeFragmentTest extends ActivityInstrumentationTestCase2<TestFragmentActivity> {
	private TestFragmentActivity mActivity;
	private HomeFragment mFragment;

	private ImageView mPlayerPicture;
	private TextView mPlayerUsername;
	private TextView mPlayerTeam;
	private TextView mPlayerCity;
	private View mProfileEdit;
	private ListView mListView;
	private View mPlaceHolder;

	public HomeFragmentTest() {
		super(TestFragmentActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
		mFragment = new HomeFragment() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
					case R.id.home_profile_edit:{
						final FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
						transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
						transaction.replace(R.id.activity_test_fragment, new LoginActivity.LoginFragment());
						transaction.commit();
						break;
					}
				}
			}
		};
		mFragment.mPlayer = new Player("Billy");

		Fragment fragment = startFragment(mFragment);
		final View rootView = fragment.getView();
		mPlayerPicture = (ImageView) rootView.findViewById(R.id.home_picture);
		mPlayerUsername = (TextView) rootView.findViewById(R.id.home_username);
		mPlayerTeam = (TextView) rootView.findViewById(R.id.home_team);
		mPlayerCity = (TextView) rootView.findViewById(R.id.home_city);
		mProfileEdit = rootView.findViewById(R.id.home_profile_edit);
		mListView = (ListView) rootView.findViewById(R.id.home_history);
		mPlaceHolder = rootView.findViewById(R.id.home_place_holder);
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
		assertNotNull(mPlayerPicture);
		assertNotNull(mPlayerUsername);
		assertNotNull(mPlayerTeam);
		assertNotNull(mPlayerCity);
		assertNotNull(mProfileEdit);
		assertNotNull(mListView);
		assertNotNull(mPlaceHolder);
	}

	public void testProfileEdit() {
		TouchUtils.clickView(this, mProfileEdit);
		getInstrumentation().waitForIdleSync();
		assertNotNull(mActivity);
	}
}