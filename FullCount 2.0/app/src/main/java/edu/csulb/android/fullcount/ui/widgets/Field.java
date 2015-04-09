package edu.csulb.android.fullcount.ui.widgets;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import edu.csulb.android.fullcount.R;
import edu.csulb.android.fullcount.io.models.Run;

public class Field extends RelativeLayout implements View.OnClickListener {

	private View mField;

	private ImageView mBackground;

	private TextView m1B;
	private TextView m2B;
	private TextView m3B;
	private TextView mHR;
	private TextView mBB;

	private View mFirstBase;
	private View mSecondBase;
	private View mThirdBase;
	private View mHomeBase;

	private View mFirstBaseNote;
	private View mSecondBaseNote;
	private View mThirdBaseNote;
	private View mHomeBaseNote;

	private CheckBox mFirstBall;
	private CheckBox mSecondBall;
	private CheckBox mThirdBall;
	private CheckBox mFirstStrike;
	private CheckBox mSecondStrike;

	private CheckBox mOut;

	private LayoutInflater mInflater;

	private Run mRun;

	public Field(Context context) {
		super(context);
		mInflater = LayoutInflater.from(context);
		init();

	}

	public Field(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mInflater = LayoutInflater.from(context);
		init();
	}

	public Field(Context context, AttributeSet attrs) {
		super(context, attrs);
		mInflater = LayoutInflater.from(context);
		init();
	}

	public Run getRun() {
		return mRun;
	}

	public void setRun(Run run) {
		mRun = run;

		switch (run.getBases()) {
			case 1:
				mBackground.setImageResource(R.drawable.field_first);
				break;
			case 2:
				mBackground.setImageResource(R.drawable.field_second);
				break;
			case 3:
				mBackground.setImageResource(R.drawable.field_third);
				break;
			case 4:
				mBackground.setImageResource(R.drawable.field_home);
				break;
			default:
				mBackground.setImageResource(R.drawable.field);
				break;
		}

		switch (run.getBalls()) {
			case 3:
				mThirdBall.setChecked(true);
			case 2:
				mSecondBall.setChecked(true);
			case 1:
				mFirstBall.setChecked(true);
		}

		switch (run.getStrikes()) {
			case 2:
				mSecondStrike.setChecked(true);
			case 1:
				mFirstStrike.setChecked(true);
		}

		if (run.getOut() > 0) {
			mOut.setChecked(true);
		}
	}
	public void init() {
		mField = mInflater.inflate(R.layout.view_field, this, true);

		mRun = new Run();

		mBackground = (ImageView) mField.findViewById(R.id.field_background);

		m1B = (TextView) mField.findViewById(R.id.field_1b);
		m2B = (TextView) mField.findViewById(R.id.field_2b);
		m3B = (TextView) mField.findViewById(R.id.field_3b);
		mHR = (TextView) mField.findViewById(R.id.field_hr);
		mBB = (TextView) mField.findViewById(R.id.field_bb);

		m1B.setOnClickListener(this);
		m2B.setOnClickListener(this);
		m3B.setOnClickListener(this);
		mHR.setOnClickListener(this);
		mBB.setOnClickListener(this);

		m1B.setTag(false);
		m2B.setTag(false);
		m3B.setTag(false);
		mHR.setTag(false);
		mBB.setTag(false);

		mFirstBase = mField.findViewById(R.id.field_base_first);
		mSecondBase = mField.findViewById(R.id.field_base_second);
		mThirdBase = mField.findViewById(R.id.field_base_third);
		mHomeBase = mField.findViewById(R.id.field_base_home);

		mFirstBase.setOnClickListener(this);
		mSecondBase.setOnClickListener(this);
		mThirdBase.setOnClickListener(this);
		mHomeBase.setOnClickListener(this);

		mFirstBase.setTag(false);
		mSecondBase.setTag(false);
		mThirdBase.setTag(false);
		mHomeBase.setTag(false);

		mFirstBaseNote = mField.findViewById(R.id.field_note_base_first);
		mSecondBaseNote = mField.findViewById(R.id.field_note_base_second);
		mThirdBaseNote = mField.findViewById(R.id.field_note_base_third);
		mHomeBaseNote = mField.findViewById(R.id.field_note_base_home);

		mFirstBall = (CheckBox) mField.findViewById(R.id.field_ball_first);
		mSecondBall = (CheckBox) mField.findViewById(R.id.field_ball_second);
		mThirdBall = (CheckBox) mField.findViewById(R.id.field_ball_third);
		mFirstStrike = (CheckBox) mField.findViewById(R.id.field_strike_first);
		mSecondStrike = (CheckBox) mField.findViewById(R.id.field_strike_second);

		mFirstBall.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.DST_IN);
		mSecondBall.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.DST_IN);
		mThirdBall.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.DST_IN);
		mFirstStrike.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.DST_IN);
		mSecondStrike.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.DST_IN);

		mFirstBall.setOnClickListener(this);
		mSecondBall.setOnClickListener(this);
		mThirdBall.setOnClickListener(this);
		mFirstStrike.setOnClickListener(this);
		mSecondStrike.setOnClickListener(this);

		mOut = (CheckBox) mField.findViewById(R.id.field_out);

		mOut.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.field_base_first: {
				if (mRun.getBases() < 1) {
					mBackground.setImageResource(R.drawable.field_first);
					mRun.setBases(1);
				} else {
					mBackground.setImageResource(R.drawable.field);
					mRun.setBases(0);
				}
				break;
			}

			case R.id.field_base_second: {
				if (mRun.getBases() < 2) {
					mBackground.setImageResource(R.drawable.field_second);
					mRun.setBases(2);
				} else {
					mBackground.setImageResource(R.drawable.field_first);
					mRun.setBases(1);
				}
				break;
			}

			case R.id.field_base_third: {
				if (mRun.getBases() < 3) {
					mBackground.setImageResource(R.drawable.field_third);
					mRun.setBases(3);
				} else {
					mBackground.setImageResource(R.drawable.field_second);
					mRun.setBases(2);
				}
				break;
			}

			case R.id.field_base_home: {
				if (mRun.getBases() < 4) {
					mBackground.setImageResource(R.drawable.field_home);
					mRun.setBases(4);
				} else {
					mBackground.setImageResource(R.drawable.field_third);
					mRun.setBases(3);
				}
				break;
			}

			case R.id.field_1b:
			case R.id.field_2b:
			case R.id.field_3b:
			case R.id.field_hr:
			case R.id.field_bb:
				final ViewGroup parent = ((ViewGroup) v.getParent());
				for (int i = 0; i < parent.getChildCount(); i++) {
					if (parent.getChildAt(i).getId() == v.getId() && v.getTag() != null && !((Boolean) v.getTag())) {
						((TextView) v).setTextColor(getResources().getColor(R.color.red));
						v.setTag(true);
					} else {
						((TextView) v).setTextColor(getResources().getColor(R.color.translucent_dark));
						v.setTag(false);
					}
				}
				break;

			case R.id.field_ball_first: {
				mRun.setBalls(mFirstBall.isChecked() ? 1 : 0);
				mSecondBall.setChecked(false);
				mThirdBall.setChecked(false);
				break;
			}

			case R.id.field_ball_second: {
				if (mSecondBall.isChecked()) {
					mRun.setBalls(2);
					mFirstBall.setChecked(true);
					mThirdBall.setChecked(false);
				} else {
					mRun.setBalls(1);
					mThirdBall.setChecked(false);
				}
				break;
			}

			case R.id.field_ball_third: {
				if (mThirdBall.isChecked()) {
					mRun.setBalls(3);
					mFirstBall.setChecked(true);
					mSecondBall.setChecked(true);
				} else {
					mRun.setBalls(2);
				}
				break;
			}

			case R.id.field_strike_first: {
				mRun.setStrikes(mFirstStrike.isChecked() ? 1 : 0);
				mSecondStrike.setChecked(false);
				break;
			}

			case R.id.field_strike_second: {
				if (mSecondStrike.isChecked()) {
					mRun.setStrikes(2);
					mFirstStrike.setChecked(true);
				} else {
					mRun.setStrikes(1);
				}
				break;
			}

			case R.id.field_out: {
				mRun.setOut(mOut.isChecked() ? 1 : 0);
			}
		}
	}
}
