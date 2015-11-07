package edu.csulb.android.fullcount;

import android.content.Context;

import com.loopj.android.http.*;

import org.apache.http.entity.StringEntity;

public class FullcountRestClient {
    // private static final String BASE_URL = "http://fullcount.azurewebsites.net";
	private static final String BASE_URL = "http://192.168.1.3:3000";


	private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, String auth, AsyncHttpResponseHandler responseHandler) {
        if (!auth.matches(""))
            client.addHeader("Authorization", "Basic " + auth);
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, String auth, AsyncHttpResponseHandler responseHandler) {
        if (!auth.matches(""))
            client.addHeader("Authorization", "Basic " + auth);
        client.post(getAbsoluteUrl(url), params, responseHandler);
        //For Arrays
    }

    public static void post(Context context, String url, StringEntity params, String auth, AsyncHttpResponseHandler responseHandler) {
        if (!auth.matches(""))
            client.addHeader("Authorization", "Basic " + auth);
        client.post(context, getAbsoluteUrl(url), params, "application/json", responseHandler);
        //NOT For Arrays
    }


    public static void put(Context context, String url, StringEntity params, String auth, AsyncHttpResponseHandler responseHandler) {
        if (!auth.matches(""))
            client.addHeader("Authorization", "Basic " + auth);
        client.put(context, getAbsoluteUrl(url), params, "application/json", responseHandler);
    }

    public static void put(String url, RequestParams params, String auth, AsyncHttpResponseHandler responseHandler) {
        if (!auth.matches(""))
            client.addHeader("Authorization", "Basic " + auth);
        client.put(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}