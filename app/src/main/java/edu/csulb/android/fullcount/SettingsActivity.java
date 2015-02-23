package edu.csulb.android.fullcount;

import android.app.Activity;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

        Button btn = (Button) findViewById(R.id.save);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            JSONObject jsonobj = new JSONObject();
                            jsonobj.put("username", "bob");
                            jsonobj.put("password", "bob");
                            postData(jsonobj, "/login");
                        } catch (JSONException je) {
                        }
                            try {
                                JSONObject jsonobj2 = new JSONObject();
                                jsonobj2.put("password", "12345");
                                jsonobj2.put("email", "Hi@gmail.com");
                                jsonobj2.put("city", "L/A");
                                jsonobj2.put("team", "N/A");
                                postData(jsonobj2, "/editAccount");
                            } catch (JSONException je) {

                            }
                        }
                    }).start();
            }
        });
	}

    public void postData(JSONObject data, String url) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://fullcount.azurewebsites.net" + url);

        try {
            StringEntity dataStringEntity = new StringEntity(data.toString());
            dataStringEntity.setContentType("application/json;charset=UTF-8");
            dataStringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
            httppost.setEntity(dataStringEntity);

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
             Log.e("test", EntityUtils.toString(response.getEntity()));
            Log.d("test", "NEXT");
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
    }

}
