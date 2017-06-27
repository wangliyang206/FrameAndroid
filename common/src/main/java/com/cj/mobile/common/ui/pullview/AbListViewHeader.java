package com.cj.mobile.common.ui.pullview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cj.mobile.common.R;
import com.cj.mobile.common.util.ActivityUtils;
import com.cj.mobile.common.util.DateFormatUtils;
import com.cj.mobile.common.util.MobileUtil;

/**
 * ListView 的头部
 * 
 * @author 王力杨
 * 
 */
public class AbListViewHeader extends LinearLayout {

	/** The header view. */
	private LinearLayout headerView;

	/** The arrow image view. */
	private ImageView arrowImageView;

	/** The header progress bar. */
	private ProgressBar headerProgressBar;

	/** The arrow image. */
	private Bitmap arrowImage = null;

	/** The tips textview. */
	private TextView tipsTextview;

	/** The header time view. */
	private TextView headerTimeView;

	/** The m state. */
	private int mState = -1;

	/** The m rotate up anim. */
	private Animation mRotateUpAnim;

	/** The m rotate down anim. */
	private Animation mRotateDownAnim;

	/** The rotate anim duration. */
	private final int ROTATE_ANIM_DURATION = 180;

	/** The Constant STATE_NORMAL. */
	public final static int STATE_NORMAL = 0;

	/** The Constant STATE_READY. */
	public final static int STATE_READY = 1;

	/** The Constant STATE_REFRESHING. */
	public final static int STATE_REFRESHING = 2;

	/** 保存上一次的刷新时间. */
	private String lastRefreshTime = null;

	/** The head content height. */
	private int headerHeight;

	/**
	 * Instantiates a new ab list view header.
	 * 
	 * @param context
	 *            the context
	 */
	public AbListViewHeader(Context context) {
		super(context);
		initView(context);
	}

	/**
	 * Instantiates a new ab list view header.
	 * 
	 * @param context
	 *            the context
	 * @param attrs
	 *            the attrs
	 */
	public AbListViewHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	/**
	 * Inits the view.
	 * 
	 * @param context
	 *            the context
	 */
	private void initView(Context context) {

		// 顶部刷新栏整体内容
		headerView = new LinearLayout(context);
		headerView.setOrientation(LinearLayout.HORIZONTAL);
		// setBackgroundColor(Color.rgb(225, 225,225));
		headerView.setGravity(Gravity.CENTER);
		headerView.setPadding(0, 5, 0, 5);

		// 显示箭头与进度
		FrameLayout headImage = new FrameLayout(context);
		arrowImageView = new ImageView(context);
		// 从包里获取的箭头图片
		arrowImage = MobileUtil.getBitmapFormSrc("image/arrow.png");
		arrowImageView.setImageBitmap(arrowImage);

		// style="?android:attr/progressBarStyleSmall" 默认的样式
		headerProgressBar = new ProgressBar(context, null,
				android.R.attr.progressBarStyle);
		headerProgressBar.setVisibility(View.GONE);

		LinearLayout.LayoutParams layoutParamsWW = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParamsWW.gravity = Gravity.CENTER;
		layoutParamsWW.width = 50;
		layoutParamsWW.height = 50;
		headImage.addView(arrowImageView, layoutParamsWW);
		headImage.addView(headerProgressBar, layoutParamsWW);

		// 顶部刷新栏文本内容
		LinearLayout headTextLayout = new LinearLayout(context);
		tipsTextview = new TextView(context);
		headerTimeView = new TextView(context);
		headTextLayout.setOrientation(LinearLayout.VERTICAL);
		headTextLayout.setGravity(Gravity.CENTER_VERTICAL);
		headTextLayout.setPadding(12, 0, 0, 0);
		LinearLayout.LayoutParams layoutParamsWW2 = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		headTextLayout.addView(tipsTextview, layoutParamsWW2);
		headTextLayout.addView(headerTimeView, layoutParamsWW2);
		tipsTextview.setTextColor(Color.rgb(107, 107, 107));
		headerTimeView.setTextColor(Color.rgb(107, 107, 107));
		tipsTextview.setTextSize(15);
		headerTimeView.setTextSize(14);

		LinearLayout.LayoutParams layoutParamsWW3 = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParamsWW3.gravity = Gravity.CENTER;
		layoutParamsWW3.bottomMargin = 5;
		layoutParamsWW3.topMargin = 5;

		LinearLayout headerLayout = new LinearLayout(context);
		headerLayout.setOrientation(LinearLayout.HORIZONTAL);
		headerLayout.setGravity(Gravity.CENTER);

		headerLayout.addView(headImage, layoutParamsWW3);
		headerLayout.addView(headTextLayout, layoutParamsWW3);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.BOTTOM;
		// 添加大布局
		headerView.addView(headerLayout, lp);

		this.addView(headerView, lp);
		// 获取View的高度
		ActivityUtils.measureView(this);
		headerHeight = this.getMeasuredHeight();
		// 向上偏移隐藏起来
		headerView.setPadding(0, -1 * headerHeight, 0, 0);

		mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateUpAnim.setFillAfter(true);
		mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateDownAnim.setFillAfter(true);

		setState(STATE_NORMAL);
	}

	/**
	 * Sets the state.
	 * 
	 * @param state
	 *            the new state
	 */
	public void setState(int state) {
		if (state == mState)
			return;

		if (state == STATE_REFRESHING) {
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.INVISIBLE);
			headerProgressBar.setVisibility(View.VISIBLE);
		} else {
			arrowImageView.setVisibility(View.VISIBLE);
			headerProgressBar.setVisibility(View.INVISIBLE);
		}

		switch (state) {
		case STATE_NORMAL:
			if (mState == STATE_READY) {
				arrowImageView.startAnimation(mRotateDownAnim);
			}
			if (mState == STATE_REFRESHING) {
				arrowImageView.clearAnimation();
			}
			tipsTextview.setText(ActivityUtils.getString(getContext(),
					R.string.common_pull_down_to_refresh));

			if (lastRefreshTime == null) {
				lastRefreshTime = DateFormatUtils
						.getCurrentTime(DateFormatUtils.dateFormatHMS);
				headerTimeView.setText(ActivityUtils.getString(getContext(),
						R.string.common_refresh_time) + lastRefreshTime);
			} else {
				headerTimeView.setText(ActivityUtils.getString(getContext(),
						R.string.common_last_refresh_time) + lastRefreshTime);
			}

			break;
		case STATE_READY:
			if (mState != STATE_READY) {
				arrowImageView.clearAnimation();
				arrowImageView.startAnimation(mRotateUpAnim);
				tipsTextview.setText(ActivityUtils.getString(getContext(),
						R.string.common_loosen_to_refresh));
				headerTimeView.setText(ActivityUtils.getString(getContext(),
						R.string.common_last_refresh_time) + lastRefreshTime);
				lastRefreshTime = DateFormatUtils
						.getCurrentTime(DateFormatUtils.dateFormatHMS);

			}
			break;
		case STATE_REFRESHING:
			tipsTextview.setText(ActivityUtils.getString(getContext(),
					R.string.common_refreshing));
			headerTimeView.setText(ActivityUtils.getString(getContext(),
					R.string.common_this_refresh_time) + lastRefreshTime);
			break;
		default:
		}

		mState = state;
	}

	/**
	 * Sets the visiable height.
	 * 
	 * @param height
	 *            the new visiable height
	 */
	public void setVisiableHeight(int height) {
		if (height < 0)
			height = 0;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) headerView
				.getLayoutParams();
		lp.height = height;
		headerView.setLayoutParams(lp);
	}

	/**
	 * Gets the visiable height.
	 * 
	 * @return the visiable height
	 */
	public int getVisiableHeight() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) headerView
				.getLayoutParams();
		return lp.height;
	}

	/**
	 * 描述：获取HeaderView.
	 * 
	 * @return the header view
	 */
	public LinearLayout getHeaderView() {
		return headerView;
	}

	/**
	 * set last refresh time.
	 * 
	 * @param time
	 *            the new refresh time
	 */
	public void setRefreshTime(String time) {
		headerTimeView.setText(time);
	}

	/**
	 * Gets the header height.
	 * 
	 * @return the header height
	 */
	public int getHeaderHeight() {
		return headerHeight;
	}

	/**
	 * 
	 * 描述：设置字体颜色
	 * 
	 * @param color
	 * @throws
	 */
	public void setTextColor(int color) {
		tipsTextview.setTextColor(color);
		headerTimeView.setTextColor(color);
	}

	/**
	 * 
	 * 描述：设置背景颜色
	 * 
	 * @param color
	 * @throws
	 */
	public void setBackgroundColor(int color) {
		headerView.setBackgroundColor(color);
	}

	/**
	 * 
	 * 描述：获取Header ProgressBar，用于设置自定义样式
	 * 
	 * @return
	 * @throws
	 */
	public ProgressBar getHeaderProgressBar() {
		return headerProgressBar;
	}

	/**
	 * 
	 * 描述：设置Header ProgressBar样式
	 * 
	 * @return
	 * @throws
	 */
	public void setHeaderProgressBarDrawable(Drawable indeterminateDrawable) {
		headerProgressBar.setIndeterminateDrawable(indeterminateDrawable);
	}

}
