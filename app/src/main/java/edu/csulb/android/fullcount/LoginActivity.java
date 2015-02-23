package edu.csulb.android.fullcount;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class LoginActivity extends Activity implements View.OnClickListener {

    private static final String Query_URL = "http://fullcount.azurewebsites.net";
	//@Override
    //TextView loginTextView;
    Button loginButton;
    EditText loginUsername;
    EditText loginPassword;
    JSONObject jsonobj;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);
	}

    @Override
    //This represents what will happen when the button is pushed
    public void onClick(View v) {

        loginUsername = (EditText) findViewById(R.id.login_username);
        loginPassword = (EditText) findViewById(R.id.login_password);
        new Thread(new Runnable() {
            public void run() {
                try {
                    JSONObject jsonobj = new JSONObject();
                    jsonobj.put("username", loginUsername.getText());
                    jsonobj.put("password", loginPassword.getText());
                    postData(jsonobj, "/login");
                } catch (JSONException je) {
                }
            }
        }).start();
    }

    public void postData(JSONObject data, String url) {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://fullcount.azurewebsites.net" + url);
        try {
            StringEntity dataStringEntity = new StringEntity(data.toString());
            dataStringEntity.setContentType("application/json;charset=UTF-8");
            dataStringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
            post.setEntity(dataStringEntity);

            //Execute HTTP Post Request
            HttpResponse response = client.execute(post);
            Log.d("Status Code: ", String.valueOf(response.getStatusLine().getStatusCode()));
        } catch (ClientProtocolException e) {
            //TODO Auto-generated catch block
        } catch (IOException e) {
            //TODO Auto-generated catch block
        }
    }
}
