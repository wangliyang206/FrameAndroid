package com.cj.mobile.common.ui.photoview;

/**
 * 抛动画侦听器
 * @author 王力杨
 *
 */
public interface FlingAnimationListener {

	/**移动*/
	public void onMove(float x, float y);
	
	/**完成*/
	public void onComplete();
	
}
