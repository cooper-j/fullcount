package edu.csulb.android.fullcount.ui.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import edu.csulb.android.fullcount.io.models.Run;
import edu.csulb.android.fullcount.ui.widgets.Field;

public class FieldsPagerAdapter extends BasePagerAdapter {

	private List<Run> mRuns;
	private int mBattersCount;

	private Context mContext;

	private class FieldsViewHolder {
		List<Field> fieldList;
	}

	public FieldsPagerAdapter(Context context, int battersCount) {
		this(context, battersCount, null);
	}

	public FieldsPagerAdapter(Context context, int battersCount, List<Run> runs) {

		mContext = context;
		mBattersCount = battersCount;

		if (runs != null) {
			mRuns = runs;
		} else {
			mRuns = new ArrayList<>();
		}
	}

	@Override
	protected View getView(Object object, View convertView, ViewGroup parent) {

		FieldsViewHolder  viewHolder;

		if (convertView == null) {
			LinearLayout layout = new LinearLayout(mContext);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
			layout.setLayoutParams(params);

			viewHolder = new FieldsViewHolder();
			viewHolder.fieldList = new ArrayList<>();

			for (int i = 0; i < mBattersCount; i++) {
				Field field = new Field(mContext);
				viewHolder.fieldList.add(field);
				layout.addView(field);
			}

			convertView = layout;
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (FieldsViewHolder) convertView.getTag();
		}

		// TODO Fill data

		return convertView;
	}

	@Override
	protected Object getItem(int position) {
		return position;
	}

	@Override
	public int getCount() {
		return 9;
	}
}
