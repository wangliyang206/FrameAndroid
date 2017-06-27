package com.cj.mobile.common.model;

import java.util.List;

/**
 * 分组信息(描述：BilayerListView 控件中的数据)
 *
 * @author 王力杨
 */
public class GroupInfo {

    private String groupID;                        //组ID
    private String groupName;                    //组名称
    private boolean isIMG;                        //是否有图片
    private int imgNormal;                        //指定图片 - 正常显示
    private int imgPressed;                        //指定图片 - 展开后显示
    private List<BilayerShowInfo> friendInfoList;    //当前组下的好友信息

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public boolean isIMG() {
        return isIMG;
    }

    public void setIMG(boolean isIMG) {
        this.isIMG = isIMG;
    }

    public int getImgNormal() {
        return imgNormal;
    }

    public void setImgNormal(int imgNormal) {
        this.imgNormal = imgNormal;
    }

    public int getImgPressed() {
        return imgPressed;
    }

    public void setImgPressed(int imgPressed) {
        this.imgPressed = imgPressed;
    }

    public List<BilayerShowInfo> getFriendInfoList() {
        return friendInfoList;
    }

    public void setFriendInfoList(List<BilayerShowInfo> friendInfoList) {
        this.friendInfoList = friendInfoList;
    }

}