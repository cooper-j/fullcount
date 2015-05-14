package edu.csulb.android.fullcount;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import edu.csulb.android.fullcount.io.models.Player;
import edu.csulb.android.fullcount.ui.activities.TestFragmentActivity;
import edu.csulb.android.fullcount.ui.fragments.PlayerCardFragment;

public class PlayerCardFragmentTest extends ActivityInstrumentationTestCase2<TestFragmentActivity> {
	private TestFragmentActivity mActivity;
	private PlayerCardFragment mFragment;

	private CheckBox mFavoriteStar;
	private ImageView mPlayerImage;
	private ImageView mFrameImage;
	private TextView mPlayerName;

	public PlayerCardFragmentTest() {
		super(TestFragmentActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
		mFragment = new PlayerCardFragment();
		mFragment.mUser = mFragment.mPlayer = new Player("Billy");

		Fragment fragment = startFragment(mFragment);
		final View rootView = fragment.getView();
		mFavoriteStar = (CheckBox) rootView.findViewById(R.id.favorite_star);
		mPlayerImage = (ImageView) rootView.findViewById(R.id.player_image_player_card_fragment);
		mFrameImage = (ImageView) rootView.findViewById(R.id.frame_player_card_fragment);
		mPlayerName = (TextView) rootView.findViewById(R.id.name_player_card_fragment);
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
		assertNotNull(mFavoriteStar);
		assertNotNull(mPlayerImage);
		assertNotNull(mFrameImage);
		assertNotNull(mPlayerName);
	}

	public void testFavorite() {
		TouchUtils.clickView(this, mPlayerImage);
		getInstrumentation().waitForIdleSync();
		assertNotNull(mActivity);
	}
}