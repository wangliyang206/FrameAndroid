package com.cj.mobile.common.ui.photoview;

/**
 * 变焦动画侦听器
 * @author 王力杨
 *
 */
public interface ZoomAnimationListener {
	/**变焦*/
	public void onZoom(float scale, float x, float y);
	/**完成*/
	public void onComplete();
}
