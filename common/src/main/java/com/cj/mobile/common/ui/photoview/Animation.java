package com.cj.mobile.common.ui.photoview;

/**
 * 
 * 动画
 * @author 王力杨
 *
 */
public interface Animation {
	
	/**
	 * Transforms the view.(转换视图)
	 * @param view	(视图)
	 * @param time	(得到时间差)
	 * @return true if this animation should remain active.  False otherwise.
	 */
	public boolean update(GestureImageView view, long time);
	
}
