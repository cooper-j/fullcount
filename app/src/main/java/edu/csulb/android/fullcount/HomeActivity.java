package edu.csulb.android.fullcount;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.Session;

/**
 * Created by Victor Solevic on 17/02/2015.
 */
public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button editBtn = (Button)findViewById(R.id.edit_profile_button);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(i);
            }
        });

        Button cookieBtn = (Button)findViewById(R.id.logout_button);
        cookieBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
	            Session.getActiveSession().closeAndClearTokenInformation();
	            Intent i = new Intent(getApplicationContext(), IntroActivity.class);
	            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(i);
	            finish();
            }
        });
    }


    public void fbClearToken(Context context) {
        Session session = Session.getActiveSession();
        if (session != null) {

            if (!session.isClosed()) {
                session.closeAndClearTokenInformation();
                //clear your preferences if saved
            }
        } else {
            session = new Session(context);
            Session.setActiveSession(session);

            session.closeAndClearTokenInformation();
            //clear your preferences if saved
        }

    }
}
