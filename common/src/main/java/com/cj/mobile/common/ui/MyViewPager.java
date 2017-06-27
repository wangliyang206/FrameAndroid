package com.cj.mobile.common.ui;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.cj.mobile.common.ui.photoview.GestureImageView;

/**
 * 一款仿优酷Android客户端图片左右滑动（自动滑动）	<br/>
 * 调用过程：										<br/>
 * <\com.chinamall21.mobile.common.ui.MyViewPager	<br/>
 * android:id="@+id/web_image_viewpager"		<br/>
 * android:layout_width="fill_parent"			<br/>
 * android:layout_height="fill_parent" />		<br/>
 * @author 王力杨
 *
 */
public class MyViewPager extends ViewPager {
	private GestureImageView[] images;
	float diffX;

	public MyViewPager(Context context) {
		super(context);
	}

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected boolean canScroll(View arg0, boolean arg1, int arg2, int arg3,
			int arg4) {
		return super.canScroll(arg0, arg1, arg2, arg3, arg4);
	}

	private final PointF current = new PointF();
	private final PointF last = new PointF();

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		return super.onInterceptTouchEvent(event);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {

			last.x = event.getX();
			last.y = event.getY();
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			if (event.getPointerCount() <= 1) {
				GestureImageView image = images[getCurrentItem()];
				// 图片宽度
				float width = image.getScale() * image.getImageWidth();
				float centerX = image.getImageX();
				// 图片左边缘坐标
				float left = centerX - width / 2;
				// 图片右边缘坐标
				float right = left + width;
				current.x = event.getX();
				diffX = (current.x - last.x);
				if (diffX >= 0) {// 往左切换
					if ((int) left >= 0) {
					} else {
						return false;
					}
				} else {// 往右切换
					if ((int) right <= image.getDisplayWidth()) {
					} else {
						return false;
					}
				}
				last.x = current.x;
			}
		}
		return super.onTouchEvent(event);
	}

	public void setGestureImages(GestureImageView[] images) {
		this.images = images;
	}
}
