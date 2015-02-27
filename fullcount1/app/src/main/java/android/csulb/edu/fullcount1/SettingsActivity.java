package android.csulb.edu.fullcount1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

    private HttpHelper httpHelp = new HttpHelper();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

        Button saveBtn = (Button)findViewById(R.id.save);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            /*TODO*/
            public void onClick(View v) {
                JSONObject jsonobj = new JSONObject();
                try {
                    jsonobj.put("password", "12345");
                    jsonobj.put("email", "Hi@gmail.com");
                    jsonobj.put("city", "L/A");
                    jsonobj.put("team", "N/A");
                } catch (JSONException je) {
                }
                httpHelp.put("/api/users/current", jsonobj, null);
            }
        });


        Button cancelBtn = (Button)findViewById(R.id.cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private final Handler handler = new Handler() {

        public void handleMessage(Message msg) {

            String aResponse = msg.getData().getString("message");

            //once the user data has been confirmed, the application moves on to the home page
            if ((null != aResponse)) {
                if (aResponse.matches("200")) {
                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(i);
                    finish();
                } else
                    Toast.makeText(getBaseContext(), "Error: " + aResponse, Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(getBaseContext(), "Not Got Response From Server.", Toast.LENGTH_SHORT).show();
        }
    };


}
