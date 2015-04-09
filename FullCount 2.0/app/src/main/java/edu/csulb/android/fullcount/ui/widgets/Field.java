package edu.csulb.android.fullcount.ui.widgets;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import edu.csulb.android.fullcount.R;

public class Field extends RelativeLayout implements View.OnClickListener {

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

	private RadioButton mOut;

	private LayoutInflater mInflater;
	private Paint mPaint;

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

	public void init() {
		final View view = mInflater.inflate(R.layout.view_field, this, true);

		m1B = (TextView) view.findViewById(R.id.field_1b);
		m2B = (TextView) view.findViewById(R.id.field_2b);
		m3B = (TextView) view.findViewById(R.id.field_3b);
		mHR = (TextView) view.findViewById(R.id.field_hr);
		mBB = (TextView) view.findViewById(R.id.field_bb);

		mFirstBase = view.findViewById(R.id.field_base_first);
		mSecondBase = view.findViewById(R.id.field_base_second);
		mThirdBase = view.findViewById(R.id.field_base_third);
		mHomeBase = view.findViewById(R.id.field_base_home);

		mFirstBaseNote = view.findViewById(R.id.field_note_base_first);
		mSecondBaseNote = view.findViewById(R.id.field_note_base_second);
		mThirdBaseNote = view.findViewById(R.id.field_note_base_third);
		mHomeBaseNote = view.findViewById(R.id.field_note_base_home);

		mFirstBall = (CheckBox) view.findViewById(R.id.field_ball_first);
		mSecondBall = (CheckBox) view.findViewById(R.id.field_ball_second);
		mThirdBall = (CheckBox) view.findViewById(R.id.field_ball_third);
		mFirstStrike = (CheckBox) view.findViewById(R.id.field_strike_first);
		mSecondStrike = (CheckBox) view.findViewById(R.id.field_strike_second);

		mOut = (RadioButton) view.findViewById(R.id.field_out);

		mPaint = new Paint();
		final ColorMatrix colorMatrix = new ColorMatrix();
		colorMatrix.setSaturation(0);
		mPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));

		// TODO Inflate
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.field_base_first:
			case R.id.field_base_second:
			case R.id.field_base_third:
			case R.id.field_base_home: {
				final Boolean state = !((Boolean) v.getTag());

				if (state) {
					v.setLayerType(View.LAYER_TYPE_HARDWARE, mPaint);
				} else {
					v.setLayerType(View.LAYER_TYPE_NONE, null);
				}

				v.setTag(state);
				break;
			}

			case R.id.field_1b:
			case R.id.field_2b:
			case R.id.field_3b:
			case R.id.field_hr:
			case R.id.field_bb: {
				final Boolean state = !((Boolean) v.getTag());

				if (state) {
					v.setLayerType(View.LAYER_TYPE_HARDWARE, mPaint);
				} else {
					v.setLayerType(View.LAYER_TYPE_NONE, null);
				}

				v.setTag(state);
				break;
			}

			case R.id.field_ball_first: {
				mSecondBall.setChecked(false);
				mThirdBall.setChecked(false);
				break;
			}

			case R.id.field_ball_second: {
				if (mSecondBall.isChecked()) {
					mFirstBall.setChecked(true);
					mThirdBall.setChecked(false);
				} else {
					mThirdBall.setChecked(false);
				}
				break;
			}

			case R.id.field_ball_third: {
				if (mThirdBall.isChecked()) {
					mFirstBall.setChecked(true);
					mSecondBall.setChecked(true);
				}
				break;
			}

			case R.id.field_strike_first: {
				mSecondStrike.setChecked(false);
				break;
			}

			case R.id.field_strike_second: {
				if (mSecondStrike.isChecked()) {
					mFirstBall.setChecked(true);
				}
				break;
			}
		}
	}
}
