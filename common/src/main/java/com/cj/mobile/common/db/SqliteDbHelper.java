package com.cj.mobile.common.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.cj.mobile.common.util.StringUtil;

import java.util.List;

/**
 * 数据库管理类
 *
 * @author 王力杨
 */
public class SqliteDbHelper extends SQLiteOpenHelper {
    public interface SqlUpdate {
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
    }

    private SqlUpdate sqlUpdate;// 数据库升级
    private List<Class<?>> model;

    public SqliteDbHelper(Context context, String name, CursorFactory factory,
                          int version, List<Class<?>> model, SqlUpdate sqlUpdate) {
        super(context, name, factory, version);
        this.model = model;
        this.sqlUpdate = sqlUpdate;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        DBUtil.mgtCreateTable(model, db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (!db.isOpen())
            db = getWritableDatabase();

        List<String> listTableName = DBUtil.getALLTableName(db);

        for (String tableName : listTableName) {
            try {
                upgradeTables(db, tableName, StringUtil.Join(",", DBUtil.getColumnNames(db, tableName)));
            } catch (Exception e) {
                //如果报错只有一种可能，就是model变量中不存在附加的表(例如：GPS定位、或在框架中创建的表)
                //如何避免：在数据库升级前把封装在框架中的model移动到框架外面的model中进行初始化
            }
        }

        if (sqlUpdate != null)
            sqlUpdate.onUpgrade(db, oldVersion, newVersion);
    }


    /**
     * 升级表
     *
     * @param db        数据库
     * @param tableName 表名
     * @param columns   字段名
     */
    private void upgradeTables(SQLiteDatabase db, String tableName,
                               String columns) {

        // 1、重命名原数据库表
        String tempTableName = tableName + "_temp";
        String sql = "ALTER TABLE " + tableName + " RENAME TO " + tempTableName;
        DBUtil.execSQL(db, sql);
        // 2、
        DBUtil.mgtCreateTable(model, db);

        // 3, Load data
        sql = "INSERT INTO " + tableName + " (" + columns + ") " + " SELECT "
                + columns + " FROM " + tempTableName;

        DBUtil.execSQL(db, sql);

        // 4, Drop the temporary table.
        DBUtil.execSQL(db, "DROP TABLE IF EXISTS " + tempTableName);

    }

}
