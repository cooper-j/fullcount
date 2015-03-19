package edu.csulb.android.fullcount;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


public class AddPlayer extends Activity {

    private ImageButton photoButton;
    private EditText playerName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);

        //initialize the widgets
        photoButton = (ImageButton) findViewById(R.id.player_image_button);
        photoButton.setTag(R.drawable.ham);
        playerName = (EditText) findViewById(R.id.player_name_edit_text);
        Button saveButton = (Button)findViewById(R.id.add_player_done_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("playerName", playerName.getText().toString());
                resultIntent.putExtra("playerImage", photoButton.getTag().toString());
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_player, menu);
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

    public void setPlayerPhotoClick(View view) {
    }
}
