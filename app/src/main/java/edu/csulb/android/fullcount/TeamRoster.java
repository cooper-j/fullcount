package edu.csulb.android.fullcount;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Activity;

import com.loopj.android.http.JsonHttpResponseHandler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class TeamRoster extends Activity {

    private Button DoneButton, AddPlayerButton;

    private ArrayList<Player> players = new ArrayList<>();

    private SharedPreferences settings;

    private ListView listView;

    private CustomListAdapter _adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_roster);

        settings = PreferenceManager
                .getDefaultSharedPreferences(TeamRoster.this);

        AddPlayerButton = (Button) findViewById(R.id.AddPlayerButton);
        AddPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go Back to login page
                //Toast.makeText(getBaseContext(),"Player Fragment Opened",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getBaseContext(), AddPlayer.class);

                startActivityForResult(i, 1);

            }
        });

        if (!settings.getString("teamId", "").isEmpty()) {
            try {
                JSONArray roster = new JSONArray(settings.getString("roster", ""));
                for (int i = 0; i < roster.length(); i++) {
                    players.add(new Player(roster.getJSONObject(i).getString("name"), R.drawable.ham));
                }
            } catch (JSONException je) {
                je.printStackTrace();
            }
        }

        _adapter = new CustomListAdapter(TeamRoster.this, players);
        listView = (ListView) findViewById(R.id.team_roster_list);
        listView.setAdapter(_adapter);


        DoneButton = (Button) findViewById(R.id.DoneButton);
        DoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go Back to login page
                //Toast.makeText(getBaseContext(),"Back to team page",Toast.LENGTH_SHORT).show();

                String auth_token_string = settings.getString("auth", "");

                JSONArray jsonArray = new JSONArray();
                for (Player player : players) {
                    JSONObject jsonobj = new JSONObject();
                    try {
                        jsonobj.put("name", player.getName());
                    } catch (JSONException je) {
                        je.printStackTrace();
                    }
                    jsonArray.put(jsonobj);
                }
                JSONObject jsonParams = new JSONObject();
                try {
                    jsonParams.put("members", jsonArray);
                } catch (JSONException je) {
                    je.printStackTrace();
                }

                StringEntity entity = null;
                try {
                    entity = new StringEntity(jsonParams.toString());
                    //Log.e("Params", entity.getContent().);
                } catch (UnsupportedEncodingException uee) {
                    uee.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.e("Data", jsonArray.toString());

                SharedPreferences.Editor editor = settings.edit();
                editor.putString("roster", jsonArray.toString());
                editor.commit();

                entity.setContentType("application/json");

                FullcountRestClient.put(getApplicationContext(), "/api/teams/" + settings.getString("teamId", "") + "/members", entity, auth_token_string, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Toast.makeText(getBaseContext(), "Success: " + statusCode, Toast.LENGTH_SHORT).show();
                        Log.e("response", response.toString());
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
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (1) : {
                if (resultCode == Activity.RESULT_OK) {
                    //String playerName = data.getStringExtra("playerName");
                    players.add(new Player(data.getStringExtra("playerName"), Integer.valueOf(data.getStringExtra("playerImage"))));
                    //players.add(playerName);
                    //String playerImage = data.getStringExtra("playerImage");
                    //imgid.add(Integer.valueOf(playerImage));
                    CustomListAdapter cLA = new CustomListAdapter(this, players);
                    listView = (ListView) findViewById(R.id.team_roster_list);
                    listView.setAdapter(cLA);
                }
                break;
            }
        }
    }

    public void removePlayerOnClickHandler(View v) {
        Player itemToRemove = (Player)v.getTag();
        _adapter.remove(itemToRemove);
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
