package edu.csulb.android.fullcount.tools;

import android.text.TextUtils;

public final class WebUtils {

	public final static boolean isValidEmail(CharSequence target) {
		if (TextUtils.isEmpty(target)) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}
}