package edu.csulb.android.fullcount;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class IntroActivity extends Activity {

	static final boolean DEBUG_MODE = true;

	private HttpHelper mHttpHelp = new HttpHelper();

	private ProgressDialog mProgress;
	private LoginButton mFacebookConnect;
	private UiLifecycleHelper mUiHelper;

	private Session.StatusCallback mCallback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

	    mUiHelper = new UiLifecycleHelper(this, mCallback);
	    mUiHelper.onCreate(savedInstanceState);

	    // Facebook key hash
	    if (DEBUG_MODE) {
		    try {
			    PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
			    for (Signature signature : info.signatures) {
				    MessageDigest md = MessageDigest.getInstance("SHA");
				    md.update(signature.toByteArray());
				    Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
			    }
		    } catch (NameNotFoundException e) {

		    } catch (NoSuchAlgorithmException e) {

		    }
	    }

	    mFacebookConnect = (LoginButton) findViewById(R.id.authButton);
	    mFacebookConnect.setReadPermissions(Arrays.asList("user_location", "email"));

        Button logInBtn = (Button)findViewById(R.id.login_btn);
        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
        });

        TextView signUpTxt = (TextView)findViewById(R.id.signUpText);
        signUpTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ConnectionActivity.class);
                startActivity(i);
            }
        });
    }

	@Override
	protected void onResume() {
		super.onResume();
		mUiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mUiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mUiHelper.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mUiHelper.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mUiHelper.onSaveInstanceState(outState);
	}

	protected void startProgressDialog() {
		startProgressDialog(null, "Loading...");
	}

	protected void startProgressDialog(String title, String message) {
		mProgress = ProgressDialog.show(this, title, message);
	}

	protected void dismissProgressDialog() {
		if (mProgress != null) {

			if (mProgress.isShowing()) {
				mProgress.dismiss();
			}

			mProgress = null;
		}
	}

	private void connectFacebook(String accessToken) {
		startProgressDialog();

		JSONObject jsonobj = new JSONObject();
		try {
			jsonobj.put("token", accessToken);
		} catch (JSONException je) {
		}


		//sends the json object to the server via our HttpHelper activity
		HttpResponse response = mHttpHelp.post("/api/users/login/facebook", jsonobj, "", "");

		dismissProgressDialog();

		if (response != null && (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201)) {

			SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			SharedPreferences.Editor editor = settings.edit();
			try {
				editor.putString("user", EntityUtils.toString(response.getEntity()));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			editor.commit();

			Intent i = new Intent(getBaseContext(), HomeActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(i);
		} else {
			Session.getActiveSession().closeAndClearTokenInformation();

			try {
				Toast.makeText(this, EntityUtils.toString(response.getEntity()), Toast.LENGTH_SHORT).show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		mFacebookConnect.setEnabled(true);
	}

	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		if (state.isOpened()) {
			mFacebookConnect.setEnabled(false);
			connectFacebook(session.getAccessToken());
		} else if (state.isClosed()) {
			mFacebookConnect.setEnabled(true);
		}
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_intro, menu);
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