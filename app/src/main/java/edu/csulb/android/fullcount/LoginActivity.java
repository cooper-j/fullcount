package edu.csulb.android.fullcount;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends Activity {

    private HttpHelper httpHelp = new HttpHelper();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {

                EditText loginUsername = (EditText) findViewById(R.id.login_username);
                EditText loginPassword = (EditText) findViewById(R.id.login_password);

                //String[] data = { loginUsername.getText().toString(), loginPassword.getText().toString()};

                JSONObject jsonobj = new JSONObject();
                try {
                    jsonobj.put("username", loginUsername.getText().toString());
                    jsonobj.put("password", loginPassword.getText().toString());
                } catch (JSONException je) {
                }
                httpHelp.post(LoginActivity.this, "/api/users/login", jsonobj, null);
           }
	    });
        Button cancelButton = (Button) findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), IntroActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
