package com.cj.mobile.common.ui.pullview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cj.mobile.common.R;
import com.cj.mobile.common.util.ActivityUtils;

/**
 * ListView 页脚
 * 
 * @author 王力杨
 * 
 */
public class AbListViewFooter extends LinearLayout {

	/** The Constant STATE_READY. */
	public final static int STATE_READY = 1;

	/** The Constant STATE_LOADING. */
	public final static int STATE_LOADING = 2;

	/** The Constant STATE_NO. */
	public final static int STATE_NO = 3;

	/** The Constant STATE_EMPTY. */
	public final static int STATE_EMPTY = 4;

	/** The footer view. */
	private LinearLayout footerView;

	/** The footer progress bar. */
	private ProgressBar footerProgressBar;

	/** The footer text view. */
	private TextView footerTextView;

	/** The footer content height. */
	private int footerHeight;
	Context context;

	/**
	 * Instantiates a new ab list view footer.
	 * 
	 * @param context
	 *            the context
	 */
	public AbListViewFooter(Context context) {
		super(context);
		this.context = context;
		initView(context);
	}

	/**
	 * Instantiates a new ab list view footer.
	 * 
	 * @param context
	 *            the context
	 * @param attrs
	 *            the attrs
	 */
	public AbListViewFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView(context);
		setState(STATE_READY);
	}

	/**
	 * Inits the view.
	 * 
	 * @param context
	 *            the context
	 */
	private void initView(Context context) {

		// 底部刷新
		footerView = new LinearLayout(context);
		// 设置布局 水平方向
		footerView.setOrientation(LinearLayout.HORIZONTAL);
		footerView.setGravity(Gravity.CENTER);
		// setBackgroundColor(Color.rgb(225, 225,225));
		footerTextView = new TextView(context);
		footerTextView.setGravity(Gravity.CENTER_VERTICAL);
		setTextColor(Color.rgb(107, 107, 107));
		footerTextView.setTextSize(15);
		footerTextView.setMinimumHeight(50);
		footerView.setPadding(0, 10, 0, 10);

		footerProgressBar = new ProgressBar(context, null,
				android.R.attr.progressBarStyle);
		footerProgressBar.setVisibility(View.GONE);

		LinearLayout.LayoutParams layoutParamsWW = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParamsWW.gravity = Gravity.CENTER;
		layoutParamsWW.width = 50;
		layoutParamsWW.height = 50;
		layoutParamsWW.rightMargin = 10;
		footerView.addView(footerProgressBar, layoutParamsWW);

		LinearLayout.LayoutParams layoutParamsWW1 = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		footerView.addView(footerTextView, layoutParamsWW1);

		LinearLayout.LayoutParams layoutParamsFW = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		addView(footerView, layoutParamsFW);

		// 获取View的高度
		ActivityUtils.measureView(this);
		footerHeight = this.getMeasuredHeight();
	}

	/**
	 * 设置当前状态.
	 * 
	 * @param state
	 *            the new state
	 */
	public void setState(int state) {

		if (state == STATE_READY) {
			footerView.setVisibility(View.VISIBLE);
			footerTextView.setVisibility(View.VISIBLE);
			footerProgressBar.setVisibility(View.GONE);
			footerTextView.setText("载入更多");
		} else if (state == STATE_LOADING) {
			footerView.setVisibility(View.VISIBLE);
			footerTextView.setVisibility(View.VISIBLE);
			footerProgressBar.setVisibility(View.VISIBLE);
			footerTextView.setText(ActivityUtils.getString(context,
					R.string.common_loading));
		} else if (state == STATE_NO) {
			footerView.setVisibility(View.GONE);
			footerTextView.setVisibility(View.VISIBLE);
			footerProgressBar.setVisibility(View.GONE);
			footerTextView.setText("已是全部");
		} else if (state == STATE_EMPTY) {
			footerView.setVisibility(View.GONE);
			footerTextView.setVisibility(View.GONE);
			footerProgressBar.setVisibility(View.GONE);
			footerTextView.setText("没有数据");
		}
	}

	/**
	 * Gets the visiable height.
	 * 
	 * @return the visiable height
	 */
	public int getVisiableHeight() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) footerView
				.getLayoutParams();
		return lp.height;
	}

	/**
	 * 隐藏footerView.
	 */
	public void hide() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) footerView
				.getLayoutParams();
		lp.height = 0;
		footerView.setLayoutParams(lp);
		footerView.setVisibility(View.GONE);
	}

	/**
	 * 显示footerView.
	 */
	public void show() {
		footerView.setVisibility(View.VISIBLE);
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) footerView
				.getLayoutParams();
		lp.height = LayoutParams.WRAP_CONTENT;
		footerView.setLayoutParams(lp);
	}

	/**
	 * 
	 * 描述：设置字体颜色
	 * 
	 * @param color
	 * @throws
	 */
	public void setTextColor(int color) {
		footerTextView.setTextColor(color);
	}

	/**
	 * 
	 * 描述：设置背景颜色
	 * 
	 * @param color
	 * @throws
	 */
	public void setBackgroundColor(int color) {
		footerView.setBackgroundColor(color);
	}

	/**
	 * 
	 * 描述：获取Footer ProgressBar，用于设置自定义样式
	 * 
	 * @return
	 * @throws
	 */
	public ProgressBar getFooterProgressBar() {
		return footerProgressBar;
	}

	/**
	 * 
	 * 描述：设置Footer ProgressBar样式
	 * 
	 * @return
	 * @throws
	 */
	public void setFooterProgressBarDrawable(Drawable indeterminateDrawable) {
		footerProgressBar.setIndeterminateDrawable(indeterminateDrawable);
	}

	/**
	 * 
	 * 描述：获取高度
	 * 
	 * @return
	 * @throws
	 */
	public int getFooterHeight() {
		return footerHeight;
	}

	/**
	 * 设置高度.
	 * 
	 * @param height
	 *            新的高度
	 */
	public void setVisiableHeight(int height) {
		if (height < 0)
			height = 0;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) footerView
				.getLayoutParams();
		lp.height = height;
		footerView.setLayoutParams(lp);
	}

}
