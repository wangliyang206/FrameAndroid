package com.cj.mobile.common.model;

import android.content.Context;

import com.cj.mobile.common.db.SqliteDbHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库配置
 *
 * @author 王力杨
 */
public class DaoConfig {
    /* android上下文 */
    private Context context = null;
    /* 数据库名字 */
    private String dbName = "android.db";
    /* 数据库版本 */
    private int dbVersion = 1;
    /* 是否是调试模式（调试模式 增删改查的时候显示SQL语句） */
    private boolean debug = false;
    /* 升级数据库回调方法 */
    private SqliteDbHelper.SqlUpdate dbUpdateListener;
    /* 表集合 */
    private List<Class<?>> entity = new ArrayList<Class<?>>();
    /* 数据库文件在sd卡中的目录 */
    private String targetDirectory;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public int getDbVersion() {
        return dbVersion;
    }

    public void setDbVersion(int dbVersion) {
        this.dbVersion = dbVersion;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public SqliteDbHelper.SqlUpdate getDbUpdateListener() {
        return dbUpdateListener;
    }

    public void setDbUpdateListener(SqliteDbHelper.SqlUpdate dbUpdateListener) {
        this.dbUpdateListener = dbUpdateListener;
    }

    public List<Class<?>> getEntity() {
        return entity;
    }

    public void setEntity(List<Class<?>> entity) {
        this.entity = entity;
    }

    public String getTargetDirectory() {
        return targetDirectory;
    }

    public void setTargetDirectory(String targetDirectory) {
        this.targetDirectory = targetDirectory;
    }
}
