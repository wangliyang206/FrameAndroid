package com.cj.mobile.common.ui.clipImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;
import com.cj.mobile.common.util.MobileUtil;
/**
 * 剪辑图像布局 
 * @author wly
 *
 */
public class ClipImageLayout extends RelativeLayout
{

	private ClipZoomImageView mZoomImageView;
	private ClipImageBorderView mClipImageView;

	/**
	 * 这里测试，直接写死了大小，真正使用过程中，可以提取为自定义属性
	 */
	private int mHorizontalPadding = 20;

	public ClipImageLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		mZoomImageView = new ClipZoomImageView(context);
		mClipImageView = new ClipImageBorderView(context);

		android.view.ViewGroup.LayoutParams lp = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		
		this.addView(mZoomImageView, lp);
		this.addView(mClipImageView, lp);

		
		// 计算padding的px
		mHorizontalPadding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources()
						.getDisplayMetrics());
		mZoomImageView.setHorizontalPadding(mHorizontalPadding);
		mClipImageView.setHorizontalPadding(mHorizontalPadding);
	}
	
	/**显示图片*/
	public void setImg(String path){
		try {
			Bitmap bitmap = MobileUtil.loadBitmapByPath(path);
			if(bitmap != null){
				mZoomImageView.setImageBitmap(bitmap);
			}
			
		} catch (Exception e) {
		}
		
	}

	/**
	 * 对外公布设置边距的方法,单位为dp
	 * 
	 * @param mHorizontalPadding
	 */
	public void setHorizontalPadding(int mHorizontalPadding)
	{
		this.mHorizontalPadding = mHorizontalPadding;
	}

	/**
	 * 已裁切好的图片
	 * 
	 * @return
	 */
	public Bitmap clip()
	{
		return mZoomImageView.clip();
	}

}
