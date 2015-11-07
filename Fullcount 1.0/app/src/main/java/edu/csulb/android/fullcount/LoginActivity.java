package edu.csulb.android.fullcount;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpRequest;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


public class LoginActivity extends Activity {

    //private HttpHelper httpHelp = new HttpHelper();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

                Button loginButton = (Button) findViewById(R.id.login_button);
                loginButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //set the text boxes for gathering username and password
                        EditText loginUsername = (EditText) findViewById(R.id.login_username);
                        EditText loginPassword = (EditText) findViewById(R.id.login_password);

                        RequestParams params = new RequestParams();
                        params.put("email", loginUsername.getText().toString());
                        params.put("password", loginPassword.getText().toString());

                        SharedPreferences settings = PreferenceManager
                                .getDefaultSharedPreferences(getBaseContext());
                        SharedPreferences.Editor editor = settings.edit();
                        try {
                            String auth = Base64.encodeToString((loginUsername.getText().toString() + ":" + loginPassword.getText().toString()).getBytes("UTF-8"), Base64.URL_SAFE | Base64.NO_WRAP);
                            editor.putString("auth", auth);
                            editor.commit();
                            FullcountRestClient.post("/api/users/login", params, auth, new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    if (statusCode == 200) {
                                        Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_SHORT).show();

                                        SharedPreferences settings = PreferenceManager
                                                .getDefaultSharedPreferences(getBaseContext());
                                        SharedPreferences.Editor editor = settings.edit();

                                        try {
                                            editor.putString("teamId", response.getJSONObject("user").getString("team"));
                                            editor.putString("roster", response.getJSONObject("team").getJSONArray("roster").toString());
                                        } catch (JSONException je) {
                                            je.printStackTrace();
                                        }
                                        editor.commit();

                                        Log.e("Login response", response.toString());

                                        Intent i = new Intent(getBaseContext(), HomeActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(i);
                                    }
                                    //Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, String error, Throwable throwable) {
                                    Toast.makeText(getBaseContext(), "Error: " + statusCode + " " + error, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                });
        //cancel login activity
        Button cancelButton = (Button)findViewById(R.id.cancel_button);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
	            finish();
            }
        });
    }
}
