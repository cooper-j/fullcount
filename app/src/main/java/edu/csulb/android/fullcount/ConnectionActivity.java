package edu.csulb.android.fullcount;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.HttpResponse;


import org.apache.http.entity.StringEntity;
import java.lang.Thread;
import java.io.IOException;
import org.apache.http.client.ClientProtocolException;

import org.json.JSONObject;
import org.json.JSONException;
import org.apache.http.util.EntityUtils;
import org.apache.http.protocol.HTTP;
import android.util.Log;
import org.apache.http.message.BasicHeader;

import android.content.Intent;

public class ConnectionActivity extends ActionBarActivity {

    private Button createAccButton, cancelButton;
    private EditText userName, email, passwordOne, passwordTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        userName = (EditText) findViewById(R.id.UserField);
        email = (EditText) findViewById(R.id.EmailField);
        passwordOne = (EditText) findViewById(R.id.Password1Field);
        passwordTwo = (EditText) findViewById(R.id.Password2Field);



        createAccButton = (Button)findViewById(R.id.AccountCreateButton);
        createAccButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                userName = (EditText) findViewById(R.id.UserField);
                email = (EditText) findViewById(R.id.EmailField);
                passwordOne = (EditText) findViewById(R.id.Password1Field);
                passwordTwo = (EditText) findViewById(R.id.Password2Field);

                ConnectionActivity abc = new ConnectionActivity();
                String field1 = userName.getText().toString();
                String field2 = email.getText().toString();
                String field3 = passwordOne.getText().toString();
                String field4 = passwordTwo.getText().toString();

                if (!field1.equals("")|| !field2.equals("")
                        || !field3.equals("") || !field4.equals(""))
                {
                    if(field3.equals(field4))
                    {
                        //Account Registered
                        //abc.register(field1, field2, field3);
                        new Thread(new Runnable() {
                            public void run()
                            {
                                try {
                                    JSONObject jsonobj = new JSONObject();
                                    jsonobj.put("username", userName.getText().toString());
                                    jsonobj.put("password", passwordOne.getText().toString());
                                    jsonobj.put("email", email.getText().toString());
                                    jsonobj.put("city", "test city");
                                    jsonobj.put("team", "test team");
                                    register(jsonobj, "/register");
                                } catch (JSONException je) {
                                }
                            }
                        }).start();

                        Toast.makeText(getBaseContext(),"Account Info Sent",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        //Warning: Passwords do not match
                        Toast.makeText(getBaseContext(),"Passwords do not match",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    //A required field was not completed
                    Toast.makeText(getBaseContext(),"Fields must be completed",Toast.LENGTH_SHORT).show();
                }

            }});

        cancelButton = (Button)findViewById(R.id.CancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Go Back to login page
                //Toast.makeText(getBaseContext(),"Back to Login",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }});
    }


    //Compiler is having a problem with R.menu.menu_connection
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_connection, menu);
        return true;
    }

    //Compiler is having a problem with R.id.action_settings
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void register(JSONObject data, String url)
    {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://fullcount.azurewebsites.net" + url);
        try {
            StringEntity dataStringEntity = new StringEntity(data.toString());
            dataStringEntity.setContentType("application/json;charset=UTF-8");
            dataStringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
            httppost.setEntity(dataStringEntity);

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            Log.d("Status Code: ", String.valueOf(response.getStatusLine().getStatusCode()));
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
    }

}
