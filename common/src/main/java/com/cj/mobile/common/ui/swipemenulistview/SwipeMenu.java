package com.cj.mobile.common.ui.swipemenulistview;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * 滑动菜单(更新日期：2015-09-02 下午7：03(中国：上海))
 * @author 王力杨
 * @date 2015-9-30
 * 
 */
public class SwipeMenu {

	private Context mContext;
	private List<SwipeMenuItem> mItems;
	private int mViewType;

	public SwipeMenu(Context context) {
		mContext = context;
		mItems = new ArrayList<SwipeMenuItem>();
	}

	public Context getContext() {
		return mContext;
	}

	public void addMenuItem(SwipeMenuItem item) {
		mItems.add(item);
	}

	public void removeMenuItem(SwipeMenuItem item) {
		mItems.remove(item);
	}

	public List<SwipeMenuItem> getMenuItems() {
		return mItems;
	}

	public SwipeMenuItem getMenuItem(int index) {
		return mItems.get(index);
	}

	public int getViewType() {
		return mViewType;
	}

	public void setViewType(int viewType) {
		this.mViewType = viewType;
	}

}
