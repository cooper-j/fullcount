package edu.csulb.android.fullcount;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


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

                //String[] data = { loginUsername.getText().toString(), loginPassword.getText().toString()};

               //create json object to send to the server with username and password
                JSONObject jsonobj = new JSONObject();
                try {
                    jsonobj.put("username", loginUsername.getText().toString());
                    jsonobj.put("password", loginPassword.getText().toString());
                } catch (JSONException je) {
                }

               //sends the json object to the server via our HttpHelper activity
                httpHelp.post(LoginActivity.this, "/api/users/login", jsonobj, null, handler);
            }
        });
        //cancel login activity
        Button cancelButton = (Button)findViewById(R.id.cancel_button);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), IntroActivity.class);
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
