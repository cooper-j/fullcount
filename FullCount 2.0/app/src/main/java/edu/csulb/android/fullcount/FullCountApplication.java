package edu.csulb.android.fullcount;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class FullCountApplication extends Application {

	static final String TAG = FullCountApplication.class.getSimpleName();
	public static final boolean DEBUG_MODE = true;
	public static final boolean FORCE_OFFLINE = false;

	@Override
	public void onCreate() {
		super.onCreate();

		initImageLoader(this);
	}

	private void initImageLoader(Context context) {
		if (DEBUG_MODE) {
			Log.i(TAG, "Initializing UniversalImageLoader");
		}

		final DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true).build();
		final ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).defaultDisplayImageOptions(options).build();
		ImageLoader.getInstance().init(config);

	}
}