package edu.csulb.android.fullcount.io.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import edu.csulb.android.fullcount.FullCountApplication;

public class RosterMember implements Serializable {

	static final String TAG = RosterMember.class.getSimpleName();
	static final boolean DEBUG_MODE = FullCountApplication.DEBUG_MODE;

	// Mendatory JSON tags
	private static final String TAG_ID = "_id";

	// Optional JSON tags
	private static final String TAG_USERNAME = "name";
	private static final String TAG_TEAM = "team";
	private static final String TAG_PICTURE_URL = "picture";

	// Model attributes
	private String mId;
	private String mName;
	private Team mTeam; // TODO Create Team model
	private String mPictureUri;

	public RosterMember() {}
    public RosterMember(String id) {
        mId = id;
    }

	public String getId() {
		return mId;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
        mName = name;
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
			jsonObject.put(TAG_USERNAME, mName);
			jsonObject.put(TAG_TEAM, mTeam);
			jsonObject.put(TAG_PICTURE_URL, mPictureUri);
		} catch (JSONException e) {
			if (DEBUG_MODE) {
				e.printStackTrace();
			}
			return null;
		}

		if (DEBUG_MODE) {
			Log.i(TAG, "Generated RosterMember JSON: " + jsonObject.toString());
		}

		return jsonObject;
	}

	@Override
	public String toString() {
		return ("RosterMember "   + mName + " (" +
				"ID: "      + mId + "), " +
				((mTeam != null) ? (mTeam.toString() + ", ") : "") +
				"Picture: " + mPictureUri
		);
	}

	public static RosterMember parseFromJSON(JSONObject jsonObject) throws JSONException {
		final RosterMember rosterMember = new RosterMember(jsonObject.getString(TAG_ID));

        rosterMember.setName(jsonObject.optString(TAG_USERNAME, "Unknown")); // TODO String
		// TODO Replace rosterMember.setTeam(jsonObject.optString(TAG_TEAM));
        rosterMember.setPictureUri(jsonObject.optString(TAG_PICTURE_URL));

		if (DEBUG_MODE) {
			Log.i(TAG, "Parsed JSON: " + rosterMember.toString());
		}

		return rosterMember;
	}

}
