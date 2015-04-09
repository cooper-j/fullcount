package edu.csulb.android.fullcount.ui.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import edu.csulb.android.fullcount.io.models.Run;
import edu.csulb.android.fullcount.ui.widgets.Field;

public class FieldPagerAdapter extends PagerAdapter {

	private int mBattersCount;
	private Context mContext;

	private List<Run> mRuns;

	public FieldPagerAdapter(Context context, List<Run> runs, int battersCount) {
		mContext = context;
		mBattersCount = battersCount;

		if (runs == null) {
			mRuns = new ArrayList<>();
		} else {
			mRuns = runs;
		}
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public final Object instantiateItem(ViewGroup container, int position) {

		LinearLayout layout = new LinearLayout(mContext);
		layout.setOrientation(LinearLayout.VERTICAL);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		layout.setLayoutParams(params);

		for (int i = 0; i < mBattersCount; i++) {
			Field field = new Field(mContext);

			if (mRuns.size() >= (position + 1) * mBattersCount) {
				field.setRun(mRuns.get(position * mBattersCount + i));
			}

			layout.addView(field);
		}

		container.addView(layout);
		return layout;
	}

	@Override
	public final void destroyItem(ViewGroup container, int position, Object object) {
		final ViewGroup parent = ((ViewGroup) object);
		if (mRuns.size() >= (position + 1) * mBattersCount) {
			for (int i = 0; i < parent.getChildCount(); i++) {
				mRuns.set(position * mBattersCount + i, ((Field) parent.getChildAt(i)).getRun());
			}
		} else {
			int size = mRuns.size();
			for (int i = 0; i < position * mBattersCount -  size; i++) {
				mRuns.add(new Run());
			}
			for (int i = 0; i < parent.getChildCount(); i++) {
				mRuns.add(position * mBattersCount + i, ((Field) parent.getChildAt(i)).getRun());
			}
		}

		container.removeView((View) object);
	}

	@Override
	public final void finishUpdate(ViewGroup container) {
	}

	@Override
	public int getCount() {
		return 9;
	}

	public List<Run> getRuns() {
		return mRuns;
	}
}
