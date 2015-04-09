package edu.csulb.android.fullcount.ui.adapters;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Iterator;

import edu.csulb.android.fullcount.R;

public abstract class BasePagerAdapter extends PagerAdapter {

	ArrayList<Object> instantiatedItems = new ArrayList<Object>();
	ArrayList<Object> destroyedItems = new ArrayList<Object>();

	@Override
	public final void startUpdate(ViewGroup container) {
		instantiatedItems.clear();
		destroyedItems.clear();
	}

	@Override
	public final Object instantiateItem(ViewGroup container, int position) {
		Object o = getItem(position);
		instantiatedItems.add(o);
		return o;
	}

	@Override
	public final void destroyItem(ViewGroup container, int position, Object object) {
		destroyedItems.add(object);
	}

	@Override
	public final void finishUpdate(ViewGroup container) {
		ArrayList<View> recycledViews = new ArrayList<View>();

		// Remove views backing destroyed items from the specified container,
		// and queue them for recycling.
		for (int i = 0; destroyedItems.size() > 0 && i < container.getChildCount(); i++) {
			View v = container.getChildAt(i);
			Iterator<Object> it = destroyedItems.iterator();
			while (it.hasNext()) {
				if (isViewFromObject(v, it.next())) {
					container.removeView(v);
					recycledViews.add(v);
					it.remove();
					break;
				}
			}
		}

		// Render views and attach them to the container. Page views are reused
		// whenever possible.
		for (Object instantiatedItem : instantiatedItems) {
			View convertView = null;
			if (recycledViews.size() > 0) convertView = recycledViews.remove(0);

			if (convertView != null) {
				// Re-add existing view before rendering so that we can make change inside getView()
				container.addView(convertView);
				convertView = getView(instantiatedItem, convertView, container);
			} else {
				convertView = getView(instantiatedItem, convertView, container);
				container.addView(convertView);
			}

			// Set another tag id to not break ViewHolder pattern
			convertView.setTag(R.id.view_data, instantiatedItem);
		}

		instantiatedItems.clear();
		recycledViews.clear();
	}

	@Override
	public final boolean isViewFromObject(View view, Object object) {
		return view.getTag(R.id.view_data) != null && view.getTag(R.id.view_data) == object;
	}

	/**
	 * Get the data item associated with the specified position in the data set.
	 *
	 * @param position Position of the item whose data we want within the adapter's
	 *                 data set.
	 * @return The data at the specified position
	 */
	protected abstract Object getItem(int position);

	/**
	 * Get a View that displays the data at the specified position in the data
	 * set.
	 *
	 * @param object      The data item whose view we want to render.
	 * @param convertView The view to be reused.
	 * @param parent      The parent that this view will eventually be attached to.
	 * @return A View corresponding to the data at the specified position.
	 */
	protected abstract View getView(Object object, View convertView, ViewGroup parent);

}