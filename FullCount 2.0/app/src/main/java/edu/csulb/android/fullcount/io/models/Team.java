package edu.csulb.android.fullcount.io.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.csulb.android.fullcount.FullCountApplication;

public class Team implements Serializable {

	static final String TAG = Team.class.getSimpleName();
	static final boolean DEBUG_MODE = FullCountApplication.DEBUG_MODE;

	// Mendatory JSON tags
	private static final String TAG_ID = "_id";

	// Optional JSON tags
	private static final String TAG_OWNER_ID = "owner";
	private static final String TAG_NAME = "name";
	private static final String TAG_CITY = "city";
	private static final String TAG_ROSTER = "roster";
	private static final String TAG_LEAGUE_CATEGORY = "leagueCategory";
	private static final String TAG_LEAGUE_NAME = "leagueName";
	private static final String TAG_SEASON = "season";
	private static final String TAG_GAMES = "games";

	// Model attributes
	private String mId;
	private String mOwnerId;
	private String mName;
	private String mCity;
	private List<Player> mRoster;
	private int mLeagueCategory;
	private String mLeagueName;
	private String mSeason;
	private List<Void> mGames; // TODO Create Game model

	public Team(String id) {
		mId = id;
	}

	public String getId() {
		return mId;
	}

	public String getOwnerId() {
		return mOwnerId;
	}

	public void setOwnerId(String ownerId) {
		mOwnerId = ownerId;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public String getCity() {
		return mCity;
	}

	public void setCity(String city) {
		mCity = city;
	}

	public List<Player> getRoster() {
		return mRoster;
	}

	public void setRoster(List<Player> roster) {
		mRoster = roster;
	}

	public int getLeagueCategory() {
		return mLeagueCategory;
	}

	public void setLeagueCategory(int leagueCategory) {
		mLeagueCategory = leagueCategory;
	}

	public String getLeagueName() {
		return mLeagueName;
	}

	public void setLeagueName(String leagueName) {
		mLeagueName = leagueName;
	}

	public List<Void> getGames() {
		return mGames;
	}

	public void setGames(List<Void> games) {
		mGames = games;
	}

	public String getSeason() {
		return mSeason;
	}

	public void setSeason(String season) {
		mSeason = season;
	}

	public JSONObject toJSON() {
		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put(TAG_ID, mId);
			jsonObject.put(TAG_OWNER_ID, mOwnerId);
			jsonObject.put(TAG_NAME, mName);
			jsonObject.put(TAG_CITY, mCity);
			// TODO Add TAG_ROSTER
			jsonObject.put(TAG_LEAGUE_CATEGORY, mLeagueCategory);
			jsonObject.put(TAG_LEAGUE_NAME, mLeagueName);
			jsonObject.put(TAG_SEASON, mSeason);
			// TODO Add TAG_GAMES
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
		return ("Team "             + mName + " (" +
				"#ID: "             + mId + "), " +
				"#OwnerID: "        + mOwnerId + ", " +
				"City: "            + mCity + ", " +
				"Roster: "          + ((mRoster != null) ? mRoster.size() : 0) + " players, " +
				"LeagueCategory: "   + mLeagueCategory + ", " +
				"LeagueName: "      + mLeagueName + ", " +
				"Season: "          + mSeason
		);
	}

	public static Team parseFromJSON(JSONObject jsonTeam) throws JSONException {
		final Team team = new Team(jsonTeam.getString(TAG_ID));

		team.setOwnerId(jsonTeam.optString(TAG_OWNER_ID));
		team.setName(jsonTeam.optString(TAG_NAME));
		team.setCity(jsonTeam.optString(TAG_CITY));

		final JSONArray jsonRoster = jsonTeam.optJSONArray(TAG_ROSTER);
		if (jsonRoster != null && jsonRoster.length() > 0) {
			final List<Player> roster = new ArrayList<>();

			for (int i = 0; i < jsonRoster.length(); i++) {
				roster.add(Player.parseFromJSON(jsonRoster.getJSONObject(i)));
			}

			team.setRoster(roster);
		}

		team.setLeagueCategory(jsonTeam.optInt(TAG_LEAGUE_CATEGORY));
		team.setLeagueName(jsonTeam.optString(TAG_LEAGUE_NAME));
		team.setSeason(jsonTeam.optString(TAG_SEASON));

		// TODO Add TAG_GAMES

		if (DEBUG_MODE) {
			Log.i(TAG, "Parsed JSON: " + team.toString());
		}

		return team;
	}

}