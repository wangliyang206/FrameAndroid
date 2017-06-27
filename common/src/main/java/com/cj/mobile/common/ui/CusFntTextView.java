package com.css.mobile.framework.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 自定义TextView（设置第三方字体）
 * 
 * @author 作者 : 李航
 * @date 创建时间：2016-3-14 下午5:47:39 
 * Android为整个应用切换第三方字体
 */
public class CusFntTextView extends TextView {

	public CusFntTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public CusFntTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CusFntTextView(Context context) {
		super(context);
		init();
	}

	private void init() {
		if (!isInEditMode()) {
			Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
					"Futura.ttf");
			setTypeface(tf);
		}
	}
}
