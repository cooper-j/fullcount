package edu.csulb.android.fullcount;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


public class TeamCreation extends ActionBarActivity {


    private HttpHelper httpHelp = new HttpHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_creation);


        Button doneButton = (Button) findViewById(R.id.Done_Button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //gather information from the user to be sent to the Server
                EditText TeamName = (EditText) findViewById(R.id.teamName);
                EditText City = (EditText) findViewById(R.id.cityName);
                Spinner League = (Spinner) findViewById(R.id.LeagueChoice);
                EditText LeagueName = (EditText) findViewById(R.id.leagueName);

                //create json object to send to the server with username and password
                JSONObject jsonobj = new JSONObject();
                try {
                    jsonobj.put("name", TeamName.getText().toString());
                    jsonobj.put("city", City.getText().toString());
                    jsonobj.put("league", League.getSelectedItemPosition());
                    jsonobj.put("lname", LeagueName.getText().toString());
                } catch (JSONException je) {
                    je.printStackTrace();
                }

                //sends the json object to the server via our HttpHelper activity
                HttpResponse response = httpHelp.post("/api/teams/", jsonobj, "", "");
                if (response != null && response.getStatusLine().getStatusCode() == 201){

                    Intent i = new Intent(getBaseContext(), HomeActivity.class);
                    startActivity(i);
                }else{
                    try{
                        Toast.makeText(getBaseContext(), EntityUtils.toString(response.getEntity()), Toast.LENGTH_SHORT).show();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_team_creation, menu);
        return true;
    }

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
}
