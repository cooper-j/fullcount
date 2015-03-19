package edu.csulb.android.fullcount;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import org.apache.http.HttpResponse;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Activity;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TeamRoster extends Activity {

    private Button DoneButton, AddPlayerButton;

    private ArrayList<String> players = new ArrayList<String>();
    private ArrayList<Integer> imgid = new ArrayList<Integer>();
    private Intent intent;

    private SharedPreferences settings;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.intent = this.getIntent();
        setContentView(R.layout.activity_team_roster);

        settings = PreferenceManager
                .getDefaultSharedPreferences(TeamRoster.this);

        AddPlayerButton = (Button)findViewById(R.id.AddPlayerButton);
        AddPlayerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Go Back to login page
                //Toast.makeText(getBaseContext(),"Player Fragment Opened",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getBaseContext(), AddPlayer.class);

                startActivityForResult(i, 1);

            }});

        if (!settings.getString("teamId", "").isEmpty()) {
            try {
                JSONArray roster = new JSONArray(settings.getString("roster", ""));
                for (int i = 0; i < roster.length(); i++){
                    players.add(roster.getJSONObject(i).getString("name"));
                    imgid.add(R.drawable.ham);
                }
            } catch (JSONException je) {
                je.printStackTrace();
            }
        }

        CustomListAdapter cLA = new CustomListAdapter(this, players, imgid);
        listView = (ListView) findViewById(R.id.team_roster_list);
        listView.setAdapter(cLA);


        DoneButton = (Button)findViewById(R.id.DoneButton);
        DoneButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Go Back to login page
                //Toast.makeText(getBaseContext(),"Back to team page",Toast.LENGTH_SHORT).show();

                String auth_token_string = settings.getString("auth", "");

                RequestParams params = new RequestParams();
                Map<String, String> map = new HashMap<String, String>();
                for (String name : players)
                    map.put("name", name);

                params.put("members", map);

                /*JSONObject jsonParams = new JSONObject();
                jsonParams.put("notes", "Test api support");
                StringEntity entity = new StringEntity(jsonParams.toString());
                FullcountRestClient.post(restApiUrl, entity, "application/json", responseHandler);*/

                Log.e("Params", params.toString());

                FullcountRestClient.post("/api/teams/" + intent.getStringExtra("teamId") + "/members", params, auth_token_string, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Toast.makeText(getBaseContext(), "Success: " + statusCode, Toast.LENGTH_SHORT).show();
                        Log.e("response", response.toString() );
                        if (statusCode == 201) {
                            Intent i = new Intent(getBaseContext(), HomeActivity.class);
                            startActivity(i);
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(getBaseContext(), HomeActivity.class);
                        startActivity(i);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String error, Throwable throwable) {
                        Toast.makeText(getBaseContext(), "Error: " + statusCode + " " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            }});
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (1) : {
                if (resultCode == Activity.RESULT_OK) {
                    String playerName = data.getStringExtra("playerName");
                    players.add(playerName);
                    String playerImage = data.getStringExtra("playerImage");
                    imgid.add(Integer.valueOf(playerImage));
                    CustomListAdapter cLA = new CustomListAdapter(this, players, imgid);
                    listView = (ListView) findViewById(R.id.team_roster_list);
                    listView.setAdapter(cLA);
                }
                break;
            }
        }
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
