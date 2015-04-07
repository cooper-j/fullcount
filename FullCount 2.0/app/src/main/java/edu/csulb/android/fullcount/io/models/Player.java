package edu.csulb.android.fullcount.io.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import edu.csulb.android.fullcount.FullCountApplication;

public class Player implements Serializable {

	static final String TAG = Player.class.getSimpleName();
	static final boolean DEBUG_MODE = FullCountApplication.DEBUG_MODE;

	// Mendatory JSON tags
	private static final String TAG_ID = "_id";

	// Optional JSON tags
	private static final String TAG_USERNAME = "username";
	private static final String TAG_CITY = "city";
	private static final String TAG_TEAM = "team";
	private static final String TAG_PICTURE_URL = "picture";

	// Model attributes
	private String mId;
	private String mUsername;
	private String mCity;
	private Team mTeam; // TODO Create Team model
	private String mPictureUri;

	public Player(String id) {
		mId = id;
	}

	public String getId() {
		return mId;
	}

	public String getUsername() {
		return mUsername;
	}

	public void setUsername(String username) {
		mUsername = username;
	}

	public String getCity() {
		return mCity;
	}

	public void setCity(String city) {
		mCity = city;
	}

	public String getTeamName(String defaultValue) {
		return (mTeam == null) ? defaultValue : getTeam().getName();
	}
	public Team getTeam() {
		return mTeam;
	}

	public void setTeam(Team team) {
		mTeam = team;
	}

	public String getPictureUri() {
		return mPictureUri;
	}

	public void setPictureUri(String pictureUri) {
		mPictureUri = pictureUri;
	}

	public JSONObject toJSON() {
		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put(TAG_ID, mId);
			jsonObject.put(TAG_USERNAME, mUsername);
			jsonObject.put(TAG_CITY, mCity);
			jsonObject.put(TAG_TEAM, mTeam);
			jsonObject.put(TAG_PICTURE_URL, mPictureUri);
		} catch (JSONException e) {
			if (DEBUG_MODE) {
				e.printStackTrace();
			}
			return null;
		}

		if (DEBUG_MODE) {
			Log.i(TAG, "Generated Player JSON: " + jsonObject.toString());
		}

		return jsonObject;
	}

	@Override
	public String toString() {
		return ("Player "   + mUsername + " (" +
				"ID: "      + mId + "), " +
				"City: "    + mCity + ", " +
				((mTeam != null) ? (mTeam.toString() + ", ") : "") +
				"Picture: " + mPictureUri
		);
	}

	public static Player parseFromJSON(JSONObject jsonObject) throws JSONException {
		final Player player = new Player(jsonObject.getString(TAG_ID));

		player.setUsername(jsonObject.optString(TAG_USERNAME, "Unknown")); // TODO String
		player.setCity(jsonObject.optString(TAG_CITY));
		// TODO Replace player.setTeam(jsonObject.optString(TAG_TEAM));
		player.setPictureUri(jsonObject.optString(TAG_PICTURE_URL));

		if (DEBUG_MODE) {
			Log.i(TAG, "Parsed JSON: " + player.toString());
		}

		return player;
	}

}