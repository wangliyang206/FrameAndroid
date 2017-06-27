package com.cj.mobile.common.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

/**
 * 描述：自定义GridView，可设置滚动到顶端时的操作
 * @author acer
 *
 */
@SuppressLint("NewApi")
public class GridViewScrollTop extends GridViewWithHeaderAndFooter{

	public interface OnGridScrollTopListener{
		void gridScrollTop();
	}

	private OnGridScrollTopListener listener;
	
	public void setOnGridScrollTopListener(OnGridScrollTopListener listener) {
		this.listener = listener;
	}
	
	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
			int scrollY, int scrollRangeX, int scrollRangeY,
			int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
		if(listener != null)
			listener.gridScrollTop();
		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
				scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		Log.e("MyListView", " t: " + t 
			+ " oldt: " + oldt + ": " + 
				getScrollY());
	}
	
	public GridViewScrollTop(Context context) {
		super(context);
	}
	
	public GridViewScrollTop(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public GridViewScrollTop(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
}
