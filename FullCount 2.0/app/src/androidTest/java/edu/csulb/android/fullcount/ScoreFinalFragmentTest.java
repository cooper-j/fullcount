package edu.csulb.android.fullcount;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.View;
import android.widget.EditText;

import edu.csulb.android.fullcount.ui.activities.TestFragmentActivity;
import edu.csulb.android.fullcount.ui.fragments.ScoreFinalFragment;

public class ScoreFinalFragmentTest extends ActivityInstrumentationTestCase2<TestFragmentActivity> {
	private TestFragmentActivity mActivity;
	private ScoreFinalFragment mFragment;

	private EditText mOpponentName;
	private EditText mOpponentScore;
	private View mSave;

	public ScoreFinalFragmentTest() {
		super(TestFragmentActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
		mFragment = new ScoreFinalFragment() {

			@Override
			public void onClick(View v) { }
		};

		Fragment fragment = startFragment(mFragment);
		final View rootView = fragment.getView();
		mOpponentName = (EditText) rootView.findViewById(R.id.score_final_name);
		mOpponentScore = (EditText) rootView.findViewById(R.id.score_final_score);
		mSave = rootView.findViewById(R.id.score_final_save);
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
		assertNotNull(mOpponentName);
		assertNotNull(mOpponentScore);
		assertNotNull(mSave);
	}

	public void testSaveFinalScore() {
		TouchUtils.clickView(this, mSave);
		getInstrumentation().waitForIdleSync();
		assertNotNull(mActivity);
	}
}