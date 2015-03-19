package edu.csulb.android.fullcount;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TeamCreation extends ActionBarActivity {


    private HttpHelper httpHelp = new HttpHelper();


    //gather information from the user to be sent to the Server
    private EditText teamName;
    private EditText city;
    private Spinner league;
    private EditText leagueName;
    private EditText season;

    private SharedPreferences settings;

    private String auth_token_string = "";
    private String teamId;

    private Boolean isCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_creation);

        teamName = (EditText) findViewById(R.id.teamName);
        city = (EditText) findViewById(R.id.cityName);
        league = (Spinner) findViewById(R.id.LeagueChoice);
        leagueName = (EditText) findViewById(R.id.leagueName);
        season = (EditText) findViewById((R.id.seasonName));


        settings = PreferenceManager
                .getDefaultSharedPreferences(TeamCreation.this);
        teamId = settings.getString("teamId", "");
        auth_token_string = settings.getString("auth", "");

        Log.e("TeamId", teamId);

        if (!teamId.isEmpty()){
            isCreated = true;
            FullcountRestClient.get("/api/teams/" + teamId, null, auth_token_string, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    Toast.makeText(getBaseContext(), "Success: " + statusCode, Toast.LENGTH_SHORT).show();
                    Log.e("response", response.toString());

                    try{
                        teamName.setText(response.getString("name"));
                    } catch (JSONException je){
                        je.printStackTrace();
                    }
                    try{
                        city.setText(response.getString("city"));
                    } catch (JSONException je){
                        je.printStackTrace();
                    }
                    //League.setSelection(Integer.valueOf(response.getString("leagueCategory")));
                    try{
                        leagueName.setText(response.getString("leagueName"));
                    } catch (JSONException je){
                        je.printStackTrace();
                    }
                    try{
                        season.setText(response.getString("season"));
                    } catch (JSONException je){
                        je.printStackTrace();
                    }

                    try{
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("roster", response.getJSONArray("roster").toString());
                        editor.commit();
                    } catch (JSONException je){
                        je.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String error, Throwable throwable) {
                    Toast.makeText(getBaseContext(), "Error: " + statusCode + " " + error, Toast.LENGTH_SHORT).show();
                }
            });
        }

        Button doneButton = (Button) findViewById(R.id.Done_Button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> dataMap = new HashMap<String, String>();

                dataMap.put("name", teamName.getText().toString());
                dataMap.put("city", city.getText().toString());
                dataMap.put("leagueCategory", String.valueOf(league.getSelectedItemPosition()));
                dataMap.put("leagueName", leagueName.getText().toString());
                dataMap.put("season", season.getText().toString());

                Pattern p = Pattern.compile("\\w", Pattern.CASE_INSENSITIVE);
                Matcher m;
                for (Map.Entry<String, String> entry : dataMap.entrySet()) {
                    m = p.matcher(entry.getValue());
                    if (!m.find()) {
                        Toast.makeText(getBaseContext(), "Field: " + entry.getKey() + " missing.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                RequestParams params = new RequestParams();
                params.put("name", dataMap.get("name"));
                params.put("city", dataMap.get("city"));
                params.put("leagueCategory", dataMap.get("leagueCategory"));
                params.put("leagueName", dataMap.get("leagueName"));
                params.put("season", dataMap.get("season"));

                if (isCreated)
                    FullcountRestClient.put("/api/teams/" + teamId, params, auth_token_string, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                            Toast.makeText(getBaseContext(), "Success: " + statusCode, Toast.LENGTH_SHORT).show();
                            Log.e("response", response.toString());

                            Intent i = new Intent(getBaseContext(), TeamRoster.class);
                            startActivity(i);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String error, Throwable throwable) {
                            Toast.makeText(getBaseContext(), "Error: " + statusCode + " " + error, Toast.LENGTH_SHORT).show();
                        }
                    });
                else
                    FullcountRestClient.post("/api/teams", params, auth_token_string, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                            Toast.makeText(getBaseContext(), "Success: " + statusCode, Toast.LENGTH_SHORT).show();
                            Log.e("response", response.toString());

                            SharedPreferences.Editor editor = settings.edit();
                            try {
                                editor.putString("teamId", response.getString("_id"));
                            }catch(JSONException je){
                                je.printStackTrace();
                            }
                            editor.commit();
                            Intent i = new Intent(getBaseContext(), TeamRoster.class);
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
