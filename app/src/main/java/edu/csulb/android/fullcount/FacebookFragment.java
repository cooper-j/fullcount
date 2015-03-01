package edu.csulb.android.fullcount;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

public class FacebookFragment extends Fragment {

    private static final String TAG = FacebookFragment.class.getSimpleName();

    private UiLifecycleHelper uiHelper;

    private final List<String> permissions;

    public FacebookFragment() {
        permissions = Arrays.asList("email");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_intro, container, false);

        LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
        authButton.setFragment(this);
        authButton.setReadPermissions(permissions);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // For scenarios where the main activity is launched and user
        // session is not null, the session state change notification
        // may not be triggered. Trigger it if it's open/closed.
        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);
        }

        uiHelper.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    private void onSessionStateChange(Session session, SessionState state,
                                      Exception exception) {
        if (state.isOpened()) {
            Log.i(TAG, "Logged in...");
        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out...");
        }
    }

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state,
                         Exception exception) {
            //onSessionStateChange(session, state, exception);
            if (session.isOpened()){
                HttpHelper httpHelper = new HttpHelper();
                JSONObject jsonobj = new JSONObject();
                try {
                    jsonobj.put("token", session.getAccessToken());
                } catch (JSONException je) {
                }
                httpHelper.post("/api/users/login/facebook", jsonobj, "");
            }
        }
    };

    private final Handler handler = new Handler() {

        public void handleMessage(Message msg) {

            String aResponse = msg.getData().getString("message");

            if ((null != aResponse)) {
                if (aResponse.matches("200")){
                    Intent i = new Intent(getActivity(), HomeActivity.class);
                    startActivity(i);
                }
                else
                {
                    fbClearToken(getActivity());
                    // ALERT MESSAGE
                    Toast.makeText(
                            getActivity(),
                            "Error: " + aResponse,
                            Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                // ALERT MESSAGE
                Toast.makeText(
                        getActivity(),
                        "Not Got Response From Server.",
                        Toast.LENGTH_SHORT).show();
            }

        }
    };

    private void fbClearToken(Context context) {
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
