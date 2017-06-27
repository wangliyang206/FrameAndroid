package com.cj.mobile.common.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import com.cj.mobile.common.ui.swipemenulistview.SwipeMenuListView;

/**
 * 自定义ListView<br/>
 * 1、可设置滚动到顶端时的操作；<br/>
 * 2、可支持左滑动出现“编辑”、“删除”(此功能效仿IOS)<br/>
 * @author 王力杨
 *
 */
@SuppressLint("NewApi")
public class ListViewScrollTop extends SwipeMenuListView{

	public interface OnListScrollTopListener{
		void listScrollTop();
	}

	private OnListScrollTopListener listener;
	
	/**置顶监听事件*/
	public void setOnListScrollTopListener(OnListScrollTopListener listener) {
		this.listener = listener;
	}
	
	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
			int scrollY, int scrollRangeX, int scrollRangeY,
			int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
		if(listener != null)
			listener.listScrollTop();
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
	
	public ListViewScrollTop(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
}