package edu.csulb.android.fullcount;

import com.loopj.android.http.*;

public class FullcountRestClient {
    private static final String BASE_URL = "http://fullcount.azurewebsites.net";

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