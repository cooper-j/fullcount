package edu.csulb.android.fullcount;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BattingRoster extends Activity {


    private ArrayList<String> players = new ArrayList<>();
    private String[] player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batting_roster);

        //final DynamicListView dynLv = (DynamicListView) findViewById(R.id.);

        //String[] players = {"Test1", "Test2", "Test3", "Test4", "Test5", "Test6", "Test7", "Test8", "Test9"};

        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(this);
        String teamId = settings.getString("teamId", "");
        try {
            JSONArray playerJsonArray = new JSONArray(settings.getString("roster", ""));

            player = new String[playerJsonArray.length()];
            for (int i = 0; i < playerJsonArray.length(); i++) {
                players.add(playerJsonArray.getJSONObject(i).getString("name"));
                //player[i] = playerJsonArray.getJSONObject(i).getString("name");
            }
        } catch (JSONException je){
            je.printStackTrace();
            finish();
        }

        for(int i = 0; i < players.size(); i++){
            Log.e("Player", players.get(i));
        }

        /*player = new String[5];
        player[0] = "Boby";
        player[1] = "Jean";
        player[2] = "Nick";
        player[3] = "Benoit";
        player[4] = "Victor";

        players.add(player[0]);
        players.add(player[1]);
        players.add(player[2]);
        players.add(player[3]);
        players.add(player[4]);*/

        Integer[] imgid = new Integer[player.length];
        for (int i = 0; i < players.size(); i++) {
            imgid[i] = R.drawable.ham;
        }

        //adapter = new StableArrayAdapter(this, R.layout.batting_roster_list, players, imgid);
        //listView = (DynamicListView) findViewById(R.id.listview);

        /*listView.setCheeseList(players);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);*/


        if (player.length > 0){
            StableArrayAdapter adapter = new StableArrayAdapter(this, R.layout.batting_roster_list, players, imgid);
            DynamicListView listView  = (DynamicListView)findViewById(R.id.listview);

            listView.setCheeseList(players);
            listView.setAdapter(adapter);
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            /*FullcountRestClient.get("/api/teams/"+teamId+"/members", null, settings.getString("auth", ""), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Toast.makeText(getBaseContext(), "Success: " + statusCode, Toast.LENGTH_SHORT).show();
                    Log.e("responseObject", response.toString());
                    if (statusCode == 200) {
                        //players
                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_SHORT).show();
                    Log.e("responseArray", response.toString());
                    if (statusCode == 200) {
                        players.clear();
                        for (int i = 0; i < response.length(); i++)
                        try {
                            players.add(response.getJSONObject(i).getString("name"));
                            //Log.e("Player", response.getJSONObject(i).toString());
                        } catch (JSONException je){
                            je.printStackTrace();
                        }

                        runOnUiThread(new Runnable(){
                            public void run() {

                                Integer[] imgid = new Integer[players.size()];
                                for (int i = 0; i < players.size(); i++) {
                                    imgid[i] = R.drawable.avatar_icon;
                                }
                                adapter = new StableArrayAdapter(battingRoster, R.layout.batting_roster_list, players, imgid);
                                listView = (DynamicListView)findViewById(R.id.listview);
                                listView.setCheeseList(players);
                                listView.setAdapter(adapter);
                                listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                            }
                        });
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String error, Throwable throwable) {
                    Toast.makeText(getBaseContext(), "Error: " + statusCode + " " + error, Toast.LENGTH_SHORT).show();
                }
            });*/
        }
    }
}
