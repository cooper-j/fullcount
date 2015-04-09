package edu.csulb.android.fullcount.io.models;

import org.json.JSONObject;

import java.io.Serializable;

public class Run implements Serializable {

	private int mStrikes;
	private int mBalls;
	private int mOut;
	private String mFirstBase;
	private String mSecondBase;
	private String mThirdBase;
	private String mFourthBase;

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

	public static Run parseFromJSON(JSONObject jsonObject) {
		// TODO Implement
		return new Run();
	}

}
