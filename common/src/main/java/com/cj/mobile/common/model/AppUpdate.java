package com.cj.mobile.common.model;

import java.io.Serializable;

/**
 * APP升级(实体类)
 *
 * @author 王力杨
 */
@SuppressWarnings("serial")
public class AppUpdate implements Serializable {
    /**
     * 自增
     */
    private int _id;
    /**
     * 版本号(1)
     */
    private int verCode = 1;
    /**
     * 版本名称(1.0.0)
     */
    private String verName = "1.0.0.1";
    /**
     * APP名称(显示名称：21chinamall)
     */
    private String name;
    /**
     * 文件名称(cm.apk)
     */
    private String fileName;
    /**
     * 文件下载地址(http://www.21chinamall.com/download/cm.apk)
     */
    private String filePath;
    /**
     * 是否强制升级（1强制；0可选）
     */
    private int force = 0;

    /*---------------------------以下为【下载】过程中用的到---------------------------*/

    /**
     * 下载标识
     */
    private int downloadid = 0;

    /**
     * 本地保存路径
     */
    private String saveDir = "";

    /**
     * 图标
     */
    private int icon = -1;

    /**
     * 下载时提示(app名称+版本名称)
     */
    private String noticeTitle = "";

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getForce() {
        return force;
    }

    public void setForce(int force) {
        this.force = force;
    }

    public int getDownloadid() {
        return downloadid;
    }

    public void setDownloadid(int downloadid) {
        this.downloadid = downloadid;
    }

    public String getSaveDir() {
        return saveDir;
    }

    public void setSaveDir(String saveDir) {
        this.saveDir = saveDir;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public int getVerCode() {
        return verCode;
    }

    public void setVerCode(int verCode) {
        this.verCode = verCode;
    }

    public String getVerName() {
        return verName;
    }

    public void setVerName(String verName) {
        this.verName = verName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "AppUpdate{" +
                "_id=" + _id +
                ", verCode=" + verCode +
                ", verName='" + verName + '\'' +
                ", name='" + name + '\'' +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", force=" + force +
                ", downloadid=" + downloadid +
                ", saveDir='" + saveDir + '\'' +
                ", icon=" + icon +
                ", noticeTitle='" + noticeTitle + '\'' +
                '}';
    }
}
