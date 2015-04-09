package edu.csulb.android.fullcount.io.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import edu.csulb.android.fullcount.FullCountApplication;

public class Game implements Serializable {

	static final String TAG = Game.class.getSimpleName();
	static final boolean DEBUG_MODE = FullCountApplication.DEBUG_MODE;

	// Mendatory JSON tags
	private static final String TAG_ID = "_id";

	private static final String TAG_OPPONENT_NAME = "opponent";
	private static final String TAG_OPPONENT_SCORE = "opponentScore";
	private static final String TAG_TEAM_SCORE = "teamScore";
	private static final String TAG_SCORESHEET = "scoresheet";

	// Model attributes
	private String mId;
	private String mOpponentName;
	private int mOpponentScore;
	private int mTeamScore;

	public Game(String id) {
		mId = id;
	}

	public String getId() {
		return mId;
	}

	public String getOpponentName() {
		return mOpponentName;
	}

	public void setOpponentName(String opponentName) {
		mOpponentName = opponentName;
	}

	public int getOpponentScore() {
		return mOpponentScore;
	}

	public void setOpponentScore(int opponentScore) {
		mOpponentScore = opponentScore;
	}

	public int getTeamScore() {
		return mTeamScore;
	}

	public void setTeamScore(int teamScore) {
		mTeamScore = teamScore;
	}

	public JSONObject toJSON() {
		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put(TAG_ID, mId);

			// TODO Implement
		} catch (JSONException e) {
			if (DEBUG_MODE) {
				e.printStackTrace();
			}
			return null;
		}

		if (DEBUG_MODE) {
			Log.i(TAG, "Generated Team JSON: " + jsonObject.toString());
		}

		return jsonObject;
	}

	@Override
	public String toString() {
		return ""; // TODO Implement
	}

	public static Game parseFromJSON(JSONObject jsonGame) throws JSONException {
		final Game game = new Game(jsonGame.getString(TAG_ID));

		game.setOpponentName(jsonGame.optString(TAG_OPPONENT_NAME));
		game.setOpponentScore(jsonGame.optInt(TAG_OPPONENT_SCORE));
		game.setTeamScore(jsonGame.optInt(TAG_TEAM_SCORE));

		// TODO Add TAG_SCORESHEET

		if (DEBUG_MODE) {
			Log.i(TAG, "Parsed JSON: " + game.toString());
		}

		return game;
	}

}
