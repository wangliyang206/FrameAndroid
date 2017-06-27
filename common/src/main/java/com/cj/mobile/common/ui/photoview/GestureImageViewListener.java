package com.cj.mobile.common.ui.photoview;

/**
 * 手势视图侦听器
 * @author 王力杨
 *
 */
public interface GestureImageViewListener {
	/**触摸*/
	public void onTouch(float x, float y);
	/**范围*/
	public void onScale(float scale);
	/**位置，方位*/
	public void onPosition(float x, float y);
	
}
