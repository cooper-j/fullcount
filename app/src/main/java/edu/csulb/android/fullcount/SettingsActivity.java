package edu.csulb.android.fullcount;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


public class SettingsActivity extends Activity {

    private HttpHelper httpHelp = new HttpHelper();
    private String auth_token_string = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(this);
        auth_token_string = settings.getString("auth", "");

        TextView name = (TextView)findViewById(R.id.settings_name);
        EditText city = (EditText)findViewById(R.id.settings_city);
        EditText team = (EditText)findViewById(R.id.settings_team);
        EditText email = (EditText)findViewById(R.id.settings_email);

        try {
            JSONObject jsonobj = new JSONObject(settings.getString("user", auth_token_string));
            HttpResponse response = httpHelp.get("/api/users/"+jsonobj.get("_id").toString(), auth_token_string);
	        if (response != null && response.getStatusLine().getStatusCode() == 200) {
		        Log.e("Get", response.getStatusLine().toString());
		        Log.e("Get", EntityUtils.toString(response.getEntity()));
		        name.setText(jsonobj.get("username").toString());
		        city.setText(jsonobj.get("city").toString());
		        team.setText(jsonobj.get("team").toString());
		        email.setText(jsonobj.get("email").toString());
	        } else {
		        try {
			        Toast.makeText(this, EntityUtils.toString(response.getEntity()), Toast.LENGTH_SHORT).show();
		        } catch (IOException e) {
			        e.printStackTrace();
		        }
	        }
        }catch(JSONException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }




        Button saveBtn = (Button)findViewById(R.id.save);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            /*TODO*/
            public void onClick(View v) {
                RequestParams params = new RequestParams();
                EditText city = (EditText) findViewById(R.id.settings_city);
                EditText team = (EditText) findViewById(R.id.settings_team);
                EditText email = (EditText) findViewById(R.id.settings_email);
                //EditText oldPassword = (EditText)findViewById(R.id.settings_oldPassword);
                String newPassword1 = ((EditText) findViewById(R.id.settings_newPassword1)).getText().toString();
                String newPassword2 = ((EditText) findViewById(R.id.settings_newPassword2)).getText().toString();

                    if (!newPassword1.matches(newPassword2))
                        Toast.makeText(getBaseContext(), "Password missmatch", Toast.LENGTH_SHORT).show();
                    else if (newPassword1.matches("")){
                        params.put("city", city.getText().toString());
                        params.put("team", team.getText().toString());
                        params.put("email", email.getText().toString());
                        sendPutRequest(params);
                    }
                    else {
                        params.put("password", newPassword1);
                        params.put("city", city.getText().toString());
                        params.put("team", team.getText().toString());
                        params.put("email", email.getText().toString());
                        sendPutRequest(params);
                    }
            }
        });


        /*Button cancelBtn = (Button)findViewById(R.id.cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
            }
        });*/
    }

    private void sendPutRequest(RequestParams params){
        Log.e("Edit: ",auth_token_string);
            FullcountRestClient.put("/api/users/current", params, auth_token_string, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getBaseContext(), HomeActivity.class);
                    startActivity(i);
                    finish();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String error, Throwable throwable) {
                    Toast.makeText(getBaseContext(), "Error: " + statusCode + " " +error, Toast.LENGTH_SHORT).show();
                }
            });

    }
}
