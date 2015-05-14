package edu.csulb.android.fullcount.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import edu.csulb.android.fullcount.FullCountApplication;
import edu.csulb.android.fullcount.R;
import edu.csulb.android.fullcount.io.models.Player;
import edu.csulb.android.fullcount.tools.FullCountRestClient;
import edu.csulb.android.fullcount.tools.ImageFilePath;
import edu.csulb.android.fullcount.ui.activities.HomeActivity;

public class ProfileEditFragment extends Fragment implements View.OnClickListener {

	static final String TAG = ProfileEditFragment.class.getSimpleName();
	static final boolean DEBUG_MODE = FullCountApplication.DEBUG_MODE;

	static final String ARGUMENT_AUTH = "AUTH";
	static final String ARGUMENT_AUTH_IS_BASIC = "AUTH_IS_BASIC";

	private static final int REQUEST_SELECT_PICTURE = 1;

	private String mAuthTokenString;
	private boolean mAuthIsBasic;

	private ImageView mPicture;
	private EditText mUsername;
	private EditText mEmail;
	private EditText mCity;
	private View mSaveButton;

	public Player mPlayer;

	private OnFragmentInteractionListener mListener;

	public interface OnFragmentInteractionListener {
		public void onProfileSaved();
	}

	public ProfileEditFragment() {
		// Required empty public constructor
	}

	public static ProfileEditFragment newInstance(String auth, boolean authIsBasic) {
		ProfileEditFragment fragment = new ProfileEditFragment();

		final Bundle args = new Bundle();
		args.putBoolean(ARGUMENT_AUTH_IS_BASIC, authIsBasic);
		args.putString(ARGUMENT_AUTH, auth);
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle args = getArguments();
		if (args != null) {
			mAuthTokenString = args.getString(ARGUMENT_AUTH);
			mAuthIsBasic = args.getBoolean(ARGUMENT_AUTH_IS_BASIC);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View inflateView = inflater.inflate(R.layout.fragment_profile_edit, container, false);

		mPicture = (ImageView) inflateView.findViewById(R.id.profile_edit_picture);
		mUsername = (EditText) inflateView.findViewById(R.id.profile_edit_username);
		mEmail = (EditText) inflateView.findViewById(R.id.profile_edit_email);
		mCity = (EditText) inflateView.findViewById(R.id.profile_edit_city);
		mSaveButton = inflateView.findViewById(R.id.profile_edit_save);

		mPicture.setOnClickListener(this);
		mSaveButton.setOnClickListener(this);

		return inflateView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (getActivity() instanceof HomeActivity) {
			mPlayer = ((HomeActivity) getActivity()).player;
		}

		if (mPlayer != null) {
			fillPlayerInformation(mPlayer);
		}
	}

	private void fillPlayerInformation(Player player) {
		if (player.getPictureUri() != null) {
			ImageLoader.getInstance().displayImage(FullCountRestClient.getAbsoluteUrl(player.getPictureUri()), mPicture);
		}
		mUsername.setText(player.getUsername());
		mCity.setText(player.getCity());
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (isDetached()) {
			return;
		}

		switch (requestCode) {

			case REQUEST_SELECT_PICTURE:
				if (resultCode == Activity.RESULT_OK) {
					final Uri pictureUri = data.getData();
					final String picturePath = ImageFilePath.getPath(getActivity(), pictureUri);
					final File picture = new File(picturePath); // TODO Reduce picture size
					ImageLoader.getInstance().displayImage("file://" + picturePath, mPicture);
					final RequestParams params = new RequestParams();
					try {
						params.put("picture", picture);
						FullCountRestClient.put("/api/users/current", params, mAuthTokenString, mAuthIsBasic, new JsonHttpResponseHandler() {

							@Override
							public void onStart() {
								// TODO Enable upload progress
								// TODO Disable picture click
							}

							@Override
							public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
								if (response != null && statusCode == 200) {
									if (DEBUG_MODE)	Log.i(TAG, "PUT /api/users/current result" + '\n' + response.toString());
									Toast.makeText(getActivity(), "Picture successfully edited.", Toast.LENGTH_SHORT).show();
									try {
										final Player player = Player.parseFromJSON(response);
										if (getActivity() != null) ((HomeActivity) getActivity()).player = player;
									} catch (JSONException e) {
										if (DEBUG_MODE)	e.printStackTrace();
										Toast.makeText(getActivity(), "Internal error. Try again later", Toast.LENGTH_SHORT).show();
									}
								} else if (response != null) {
									if (DEBUG_MODE)	Log.e(TAG, "Error " + statusCode + ": " + response.toString());
									Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
								} else {
									if (DEBUG_MODE)	Log.e(TAG, "Error " + statusCode + ": " + "Response is null");
									Toast.makeText(getActivity(), "Could not save picture. Try again later.", Toast.LENGTH_SHORT).show();
								}
							}

							@Override
							public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
								if (responseString != null) {
									if (DEBUG_MODE) Log.e(TAG, "Error " + statusCode + ": " + responseString);
									Toast.makeText(getActivity(), responseString, Toast.LENGTH_SHORT).show();
								} else {
									if (DEBUG_MODE) Log.e(TAG, "Error " + statusCode + ": " + "Response is null");
									Toast.makeText(getActivity(), "Could not save picture. Try again later.", Toast.LENGTH_SHORT).show();
								}
							}

							@Override
							public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject error) {
								if (error != null) {
									if (DEBUG_MODE)	Log.e(TAG, "Error " + statusCode + ": " + error.toString());
									try {
										Toast.makeText(getActivity(), error.getString("message"), Toast.LENGTH_SHORT).show();
									} catch (JSONException e) {
										if (DEBUG_MODE) e.printStackTrace();
										Toast.makeText(getActivity(), "Internal error. Try again later", Toast.LENGTH_SHORT).show();
									}
								} else {
									if (DEBUG_MODE) Log.e(TAG, "Error " + statusCode + ": " + "Response is null");
									Toast.makeText(getActivity(), "Could not save picture. Try again later.", Toast.LENGTH_SHORT).show();
								}
							}

							@Override
							public void onFinish() {
								// TODO Disable upload progress
								// TODO Enable picture click
							}
						});


					} catch (FileNotFoundException e) {
						if (DEBUG_MODE) {
							e.printStackTrace();
						}
					}
				}
				break;

		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.profile_edit_picture)  {
			Intent intent = new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(intent, REQUEST_SELECT_PICTURE);
		} else if (v.getId() == R.id.profile_edit_save) {

			final String username = mUsername.getText().toString();
			final String city = mCity.getText().toString();

			// TODO Check data validity

			try {
				JSONObject jsonObject = new JSONObject();

				jsonObject.put("username", username);
				jsonObject.put("city", city);

				Log.e(TAG, jsonObject.toString());

				FullCountRestClient.put(getActivity(), "/api/users/current", jsonObject, mAuthTokenString, mAuthIsBasic, new JsonHttpResponseHandler() {

					@Override
					public void onStart() {
						// TODO Start progress dialog
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
						if (response != null && statusCode == 200) {
							if (DEBUG_MODE)	Log.i(TAG, "PUT /api/users/current result" + '\n' + response.toString());
							Toast.makeText(getActivity(), "Profile saved.", Toast.LENGTH_SHORT).show();
							try {
								final Player player = Player.parseFromJSON(response);
								if (getActivity() != null) ((HomeActivity) getActivity()).player = player;
							} catch (JSONException e) {
								if (DEBUG_MODE)	e.printStackTrace();
								Toast.makeText(getActivity(), "Internal error. Try again later.", Toast.LENGTH_SHORT).show();
							}
							if (mListener != null) mListener.onProfileSaved();
						} else if (response != null) {
							if (DEBUG_MODE)	Log.e(TAG, "Error " + statusCode + ": " + response.toString());
							Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
						} else {
							if (DEBUG_MODE)	Log.e(TAG, "Error " + statusCode + ": " + "Response is null");
							Toast.makeText(getActivity(), "Could not save profile. Try again later.", Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
						if (responseString != null) {
							if (DEBUG_MODE) Log.e(TAG, "Error " + statusCode + ": " + responseString);
							Toast.makeText(getActivity(), responseString, Toast.LENGTH_SHORT).show();
						} else {
							if (DEBUG_MODE) Log.e(TAG, "Error " + statusCode + ": " + "Response is null");
							Toast.makeText(getActivity(), "Could not save profile. Try again later.", Toast.LENGTH_SHORT).show();
						}
						if (DEBUG_MODE && throwable != null) throwable.printStackTrace();
					}

					@Override
					public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject error) {
						if (error != null) {
							if (DEBUG_MODE)	Log.e(TAG, "Error " + statusCode + ": " + error.toString());
							try {
								Toast.makeText(getActivity(), error.getString("message"), Toast.LENGTH_SHORT).show();
							} catch (JSONException e) {
								if (DEBUG_MODE)	e.printStackTrace();
								Toast.makeText(getActivity(), "Internal error. Try again later.", Toast.LENGTH_SHORT).show();
							}
						} else {
							if (DEBUG_MODE)	Log.e(TAG, "Error " + statusCode + ": " + "Response is null");
							Toast.makeText(getActivity(), "Could not save profile. Try again later.", Toast.LENGTH_SHORT).show();
						}
						if (DEBUG_MODE && throwable != null) throwable.printStackTrace();
					}

					@Override
					public void onFinish() {
						// TODO Disable progress dialog
					}
				});
			} catch (JSONException e) {
				if (DEBUG_MODE) e.printStackTrace();
				Toast.makeText(getActivity(), "Unexpected error occurred. Try again later.", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (activity instanceof  HomeActivity) {
			((HomeActivity) activity).onFragmentAttached(R.layout.fragment_profile_edit);
		}

		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			// throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}
}
