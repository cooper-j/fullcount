package edu.csulb.android.fullcount;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Victor Solevic on 17/02/2015.
 */
public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button cookieBtn = (Button) findViewById(R.id.button);
        cookieBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.webkit.CookieManager.getInstance().removeAllCookie();
                Intent i = new Intent(getApplicationContext(), IntroActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
