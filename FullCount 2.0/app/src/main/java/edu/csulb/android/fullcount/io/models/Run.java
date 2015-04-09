package edu.csulb.android.fullcount.io.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import edu.csulb.android.fullcount.FullCountApplication;

public class Run implements Serializable {

	static final String TAG = Run.class.getSimpleName();
	static final boolean DEBUG_MODE = FullCountApplication.DEBUG_MODE;

	private int mBases;
	private int mStrikes;
	private int mBalls;
	private int mOut;
	private String mFirstBase;
	private String mSecondBase;
	private String mThirdBase;
	private String mFourthBase;

	public int getBases() {
		return mBases;
	}

	public void setBases(int bases) {
		mBases = bases;
	}

	public int getStrikes() {
		return mStrikes;
	}

	public void setStrikes(int strikes) {
		mStrikes = strikes;
	}

	public int getBalls() {
		return mBalls;
	}

	public void setBalls(int balls) {
		mBalls = balls;
	}

	public int getOut() {
		return mOut;
	}

	public void setOut(int out) {
		mOut = out;
	}

	public String getFirstBase() {
		return mFirstBase;
	}

	public void setFirstBase(String firstBase) {
		mFirstBase = firstBase;
	}

	public String getSecondBase() {
		return mSecondBase;
	}

	public void setSecondBase(String secondBase) {
		mSecondBase = secondBase;
	}

	public String getThirdBase() {
		return mThirdBase;
	}

	public void setThirdBase(String thirdBase) {
		mThirdBase = thirdBase;
	}

	public String getFourthBase() {
		return mFourthBase;
	}

	public void setFourthBase(String fourthBase) {
		mFourthBase = fourthBase;
	}

	public JSONObject toJSON() {
		try {
			final JSONObject jsonRun = new JSONObject();

			jsonRun.put("strikes", mStrikes);
			jsonRun.put("balls", mBalls);
			jsonRun.put("out", mOut);
			jsonRun.put("firstBase", mFirstBase);
			jsonRun.put("secondBase", mSecondBase);
			jsonRun.put("ThirdBase", mThirdBase);
			jsonRun.put("FourthBase", mFourthBase);

			/* TODO Statistics
			final JSONObject jsonStatistics = new JSONObject();
			jsonStatistics.put("");
			*/

			return jsonRun;
		} catch (JSONException e) {
			if (DEBUG_MODE) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static Run parseFromJSON(JSONObject jsonObject) {
		// TODO Implement
		return new Run();
	}

}
