package edu.csulb.android.fullcount.tools;

import android.content.Context;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import edu.csulb.android.fullcount.FullCountApplication;

public class FullCountRestClient {
	static final String TAG = FullCountRestClient.class.getSimpleName();
	static final boolean DEBUG_MODE = FullCountApplication.DEBUG_MODE;

	private static final String BASE_URL = "http://fullcountserver.herokuapp.com";

	private static AsyncHttpClient client = new AsyncHttpClient();

	public static void get(String url, RequestParams params, String auth, boolean basic, AsyncHttpResponseHandler responseHandler) {
		if (auth != null && !auth.matches("")) client.addHeader("Authorization", (basic ? "Basic " : " Bearer ") + auth);
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}

	public static void post(String url, RequestParams params, String auth, boolean basic, AsyncHttpResponseHandler responseHandler) {
		if (auth != null && !auth.matches("")) client.addHeader("Authorization", (basic ? "Basic " : " Bearer ") + auth);
		client.post(getAbsoluteUrl(url), params, responseHandler);
	}

	public static void post(Context context, String url, JSONObject jsonObject, String auth, boolean basic, JsonHttpResponseHandler responseHandler) {
		if (auth != null && !auth.matches("")) client.addHeader("Authorization", (basic ? "Basic " : " Bearer ") + auth);
		try {
			final StringEntity entity = new StringEntity(jsonObject.toString());
			entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			client.post(context, getAbsoluteUrl(url), entity, "application/json", responseHandler);
		} catch (UnsupportedEncodingException e) {
			if (DEBUG_MODE) {
				e.printStackTrace();
				Toast.makeText(context, "Unexpected UnsupportedEncodingException", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public static void post(Context context, String url, JSONArray jsonArray, String auth, boolean basic, JsonHttpResponseHandler responseHandler) {
		if (auth != null && !auth.matches("")) client.addHeader("Authorization", (basic ? "Basic " : " Bearer ") + auth);
		try {
			final StringEntity entity = new StringEntity(jsonArray.toString());
			entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			client.post(context, getAbsoluteUrl(url), entity, "application/json", responseHandler);
		} catch (UnsupportedEncodingException e) {
			if (DEBUG_MODE) {
				e.printStackTrace();
				Toast.makeText(context, "Unexpected UnsupportedEncodingException", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public static void put(String url, RequestParams params, String auth, boolean basic, AsyncHttpResponseHandler responseHandler) {
		if (auth != null && !auth.matches("")) client.addHeader("Authorization", (basic ? "Basic " : " Bearer ") + auth);
		client.put(getAbsoluteUrl(url), params, responseHandler);
	}

	public static void put(Context context, String url, JSONObject jsonObject, String auth, boolean basic, JsonHttpResponseHandler responseHandler) {
		if (auth != null && !auth.matches("")) client.addHeader("Authorization", (basic ? "Basic " : " Bearer ") + auth);
		try {
			final StringEntity entity = new StringEntity(jsonObject.toString());
			entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			client.put(context, getAbsoluteUrl(url), entity, "application/json", responseHandler);
		} catch (UnsupportedEncodingException e) {
			if (DEBUG_MODE) {
				e.printStackTrace();
				Toast.makeText(context, "Unexpected UnsupportedEncodingException", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public static void put(Context context, String url, JSONArray jsonArray, String auth, boolean basic, JsonHttpResponseHandler responseHandler) {
		if (auth != null && !auth.matches("")) client.addHeader("Authorization", (basic ? "Basic " : " Bearer ") + auth);
		try {
			final StringEntity entity = new StringEntity(jsonArray.toString());
			entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			client.put(context, getAbsoluteUrl(url), entity, "application/json", responseHandler);
		} catch (UnsupportedEncodingException e) {
			if (DEBUG_MODE) {
				e.printStackTrace();
				Toast.makeText(context, "Unexpected UnsupportedEncodingException", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public static String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl;
	}
}
