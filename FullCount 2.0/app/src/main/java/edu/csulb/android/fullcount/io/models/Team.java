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
	private List<RosterMember> mRoster;
	private int mLeagueCategory;
	private String mLeagueName;
	private String mSeason;
	private List<Game> mGames; // TODO Create Game model

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

	public List<RosterMember> getRoster() {
		return mRoster;
	}

	public void setRoster(List<RosterMember> roster) {
		mRoster = roster;
	}

    public void removeMemberFromRoster(RosterMember rosterMember){ this.mRoster.remove(rosterMember); }

    public void addMemberToRoster(RosterMember rosterMember) { this.mRoster.add(rosterMember); };

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

	public List<Game> getGames() {
		return mGames;
	}

	public void setGames(List<Game> games) {
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
			final List<RosterMember> roster = new ArrayList<>();

			for (int i = 0; i < jsonRoster.length(); i++) {
				roster.add(RosterMember.parseFromJSON(jsonRoster.getJSONObject(i)));
			}
			team.setRoster(roster);
		}
        else
            team.setRoster(new ArrayList<RosterMember>());
		team.setLeagueCategory(jsonTeam.optInt(TAG_LEAGUE_CATEGORY));
		team.setLeagueName(jsonTeam.optString(TAG_LEAGUE_NAME));
		team.setSeason(jsonTeam.optString(TAG_SEASON));

		final JSONArray jsonGames = jsonTeam.optJSONArray(TAG_GAMES);
		if (jsonGames != null && jsonGames.length() > 0) {
			final List<Game> games = new ArrayList<>();

			for (int i = 0; i < jsonGames.length(); i++) {
				games.add(Game.parseFromJSON(jsonGames.getJSONObject(i)));
			}
			team.setGames(games);
		} else team.setGames(new ArrayList<Game>());

		if (DEBUG_MODE) {
			Log.i(TAG, "Parsed JSON: " + team.toString());
		}

		return team;
	}

}
