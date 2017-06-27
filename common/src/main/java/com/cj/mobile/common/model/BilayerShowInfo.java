package com.cj.mobile.common.model;

import java.io.Serializable;

/**
 * 二级数据(描述：BilayerListView 控件中的数据)
 * 
 * @author 王力杨
 * 
 */
@SuppressWarnings("serial")
public class BilayerShowInfo implements Serializable {
	
	private String id;						//业务id
	private String title;					//标题(显示的文字)
	
	private boolean isShowRightImg;			//是否显示右侧图片
	private boolean isShowLeftImg;			//是否显示左侧图片
	
	private int imgLeft;				//指定左侧图片
	
	private int imgRight;				//指定右侧图片

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isShowRightImg() {
		return isShowRightImg;
	}

	public void setShowRightImg(boolean isShowRightImg) {
		this.isShowRightImg = isShowRightImg;
	}

	public boolean isShowLeftImg() {
		return isShowLeftImg;
	}

	public void setShowLeftImg(boolean isShowLeftImg) {
		this.isShowLeftImg = isShowLeftImg;
	}

	public int getImgLeft() {
		return imgLeft;
	}

	public void setImgLeft(int imgLeft) {
		this.imgLeft = imgLeft;
	}

	public int getImgRight() {
		return imgRight;
	}

	public void setImgRight(int imgRight) {
		this.imgRight = imgRight;
	}
	
}
