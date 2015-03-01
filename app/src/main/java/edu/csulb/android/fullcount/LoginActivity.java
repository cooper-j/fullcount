package edu.csulb.android.fullcount;

import android.app.Activity;
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

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;

import javax.crypto.SecretKey;


public class LoginActivity extends Activity {

    private HttpHelper httpHelp = new HttpHelper();

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

               //create json object to send to the server with username and password
                JSONObject jsonobj = new JSONObject();
                try {
                    jsonobj.put("username", loginUsername.getText().toString());
                    jsonobj.put("password", loginPassword.getText().toString());
                } catch (JSONException je) {
                }

                //sends the json object to the server via our HttpHelper activity
                HttpResponse response = httpHelp.post("/api/users/login", jsonobj, "");
               if (response != null && response.getStatusLine().getStatusCode() == 200){
                   SharedPreferences settings = PreferenceManager
                           .getDefaultSharedPreferences(getBaseContext());
                   SharedPreferences.Editor editor = settings.edit();
                   try {
                       editor.putString("auth", Base64.encodeToString((loginUsername.getText().toString() + ":" + loginPassword.getText().toString()).getBytes("UTF-8"), Base64.URL_SAFE|Base64.NO_WRAP));
                       editor.putString("user", EntityUtils.toString(response.getEntity()));
                   } catch (UnsupportedEncodingException e) {
                       e.printStackTrace();
                   }catch (IOException e){
                       e.printStackTrace();
                   }
                   editor.commit();

                   Intent i = new Intent(getBaseContext(), HomeActivity.class);
                   startActivity(i);
               }else{
                   try{
                       Toast.makeText(getBaseContext(),EntityUtils.toString(response.getEntity()),Toast.LENGTH_SHORT).show();
                   }catch(IOException e){
                       e.printStackTrace();
                   }
               }
            }
        });
        //cancel login activity
        Button cancelButton = (Button)findViewById(R.id.cancel_button);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), IntroActivity.class);
                startActivity(i);
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
                }
                else if (aResponse.matches("201")) {
                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(i);
                }else
                    Toast.makeText(getBaseContext(), "Error: " + aResponse, Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(getBaseContext(), "Not Got Response From Server.", Toast.LENGTH_SHORT).show();
        }
    };
}
