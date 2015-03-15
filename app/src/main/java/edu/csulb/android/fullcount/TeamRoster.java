package edu.csulb.android.fullcount;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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

public class TeamRoster extends Activity {

    Button DoneButton, AddPlayerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_roster);

        AddPlayerButton = (Button)findViewById(R.id.AddPlayerButton);
        AddPlayerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Go Back to login page
                Toast.makeText(getBaseContext(),"Player Fragment Opened",Toast.LENGTH_SHORT).show();
                //Intent i = new Intent(getBaseContext(), FragmentAddPlayer.class);
                //startActivity(i);

            }});

        String[] players = new String[] {"1", "2", "3"};


        Integer[] imgid = new Integer[players.length];
        for (int i = 0; i < players.length; i++) {
            imgid[i] = R.drawable.avatar_icon;
        }

        CustomListAdapter cLA = new CustomListAdapter(this, players, imgid);
        ListView listView = (ListView) findViewById(R.id.team_roster_list);
        listView.setAdapter(cLA);


        DoneButton = (Button)findViewById(R.id.DoneButton);
        DoneButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Go Back to login page
                //Toast.makeText(getBaseContext(),"Back to team page",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getBaseContext(), TeamCreation.class);
                startActivity(i);
            }});

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_team_roster, menu);
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
