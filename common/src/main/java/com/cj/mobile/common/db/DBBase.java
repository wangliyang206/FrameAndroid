package com.cj.mobile.common.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cj.mobile.common.exception.DbException;
import com.cj.mobile.common.model.DaoConfig;
import com.cj.mobile.common.util.FileUtil;
import com.cj.mobile.common.util.Validate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 操作数据库的入口
 *
 * @author 王力杨
 */
public class DBBase {
    private SQLiteDatabase db;
    private DaoConfig config;
    private static boolean isDebug = false;
    private static HashMap<String, DBBase> daoMap = new HashMap<String, DBBase>();

    /**
     * 构造方法(初始化数据库)
     * 根据数据库配置初始化数据库
     *
     * @param config 默认参数
     */
    private DBBase(DaoConfig config) {
        if (config == null)
            throw new DbException("daoConfig is null");
        if (config.getContext() == null)
            throw new DbException("android context is null");
        //在SD卡上创建数据库
        if (!Validate.isEmpty(config.getTargetDirectory())) {
            closeDB();
            this.db = createDbFileOnSDCard(config.getTargetDirectory(), config.getDbName());
            if (Validate.isNotEmpty(config.getEntity()))
                DBUtil.mgtCreateTable(config.getEntity(), this.db);
        } else {
            //使用SqliteDbHelper创建数据库
            closeDB();
            this.db = new SqliteDbHelper(
                    config.getContext().getApplicationContext(),
                    config.getDbName(),
                    null,
                    config.getDbVersion(),
                    config.getEntity(),
                    config.getDbUpdateListener()).getWritableDatabase();
        }
        this.config = config;
        isDebug = config.isDebug();
    }

    /**
     * 判断数据库实例实例是否存在
     *
     * @param daoConfig 数据库配置文件
     * @return 数据库实例
     */
    private synchronized static DBBase getInstance(DaoConfig daoConfig) {
        DBBase dao = daoMap.get(daoConfig.getDbName());
        if (dao == null) {
            dao = new DBBase(daoConfig);
            daoMap.put(daoConfig.getDbName(), dao);
        }
        return dao;
    }

    /**
     * 在SD卡的指定目录上创建文件
     *
     * @param sdcardPath
     * @param dbfilename
     * @return
     */
    private SQLiteDatabase createDbFileOnSDCard(String sdcardPath, String dbfilename) {
        File dbf = new File(sdcardPath, dbfilename);
        if (!dbf.exists()) {
            FileUtil.mkDir(sdcardPath);
            try {
                if (dbf.createNewFile()) {
                    return SQLiteDatabase.openOrCreateDatabase(dbf, null);
                }
            } catch (IOException ioex) {
                throw new DbException("数据库文件创建失败", ioex);
            }
        } else {
            return SQLiteDatabase.openOrCreateDatabase(dbf, null);
        }
        return null;
    }

	/*###################################################创建数据库###################################################*/

    /**
     * 验证必填项
     *
     * @param context   上下文
     * @param dbVersion 版本号
     */
    private static void verReq(Context context, int dbVersion) {
        if (context == null)
            throw new DbException("Context is null");
        if (dbVersion < 1)
            throw new DbException("dbVersion Less than zero");
    }

    /**
     * 验证必填项
     *
     * @param context 上下文
     * @param entity  表结构
     */
    private static void verReq(Context context, List<Class<?>> entity) {
        if (context == null)
            throw new DbException("Context is null");
        if (!Validate.isNotEmpty(entity))
            throw new DbException("table is null");
    }

    /**
     * 作用范围：APP启动时第一次创建数据库创建表；<br>
     * 1、如果DBBase对象不存在，则创建，如果存在则获取DBBase对象(DBBase对象是通过数据库名称获取的，获取DBBase对象后可操作数据库数据)。<br>
     * 2、如果未创建数据库，则会创建；<br>
     * 3、数据库保存的位置：默认位置。<br>
     * 4、如果entity不为null时会自动创建表；<br>
     * 5、支持调试模式(调试模式下可见执行的sql)<br>
     * 6、支持创建多个数据库<br>
     *
     * @param context          上下文
     * @param isDebug          是否是调试模式：调试模式会log出sql信息
     * @param dbName           数据库名字(选填：默认数据库名称android.db)
     * @param dbVersion        数据库版本信息
     * @param entity           表集合(可为NULL，NULL情况下不会创建任何表)
     * @param dbUpdateListener 数据库升级回调，如果数据库在升级时需要处理特殊情况，可以在回调中实现
     * @return
     */
    public static DBBase create(Context context, boolean isDebug,
                                String dbName, int dbVersion,
                                List<Class<?>> entity, SqliteDbHelper.SqlUpdate dbUpdateListener) {

        verReq(context, dbVersion);

        DaoConfig config = new DaoConfig();
        config.setContext(context);
        if (!Validate.isEmpty(dbName))
            config.setDbName(dbName);
        config.setDebug(isDebug);
        config.setDbVersion(dbVersion);
        config.setDbUpdateListener(dbUpdateListener);
        if (Validate.isNotEmpty(entity)) {
            config.setEntity(entity);
        }
        return create(config);
    }

    /**
     * 作用范围：APP启动时第一次创建数据库创建表；<br>
     * 1、如果DBBase对象不存在，则创建，如果存在则获取DBBase对象(DBBase对象是通过数据库名称获取的，获取DBBase对象后可操作数据库数据)。<br>
     * 2、如果未创建数据库，则会创建；<br>
     * 3、数据库保存的位置：可指定sdcard位置。<br>
     * 4、如果entity不为null时会自动创建表；<br>
     * 5、支持调试模式(调试模式下可见执行的sql)<br>
     * 6、支持创建多个数据库<br>
     *
     * @param context          上下文
     * @param isDebug          是否是调试模式：调试模式会log出sql信息
     * @param targetDirectory  db文件路径，可以配置为sdcard的路径
     * @param dbName           数据库名字(选填：默认数据库名称android.db)
     * @param dbVersion        数据库版本信息
     * @param entity           表集合(可为NULL，NULL情况下不会创建任何表)
     * @param dbUpdateListener 数据库升级回调，如果数据库在升级时需要处理特殊情况，可以在回调中实现
     * @return
     */
    public static DBBase create(Context context, boolean isDebug,
                                String targetDirectory,
                                String dbName, int dbVersion,
                                List<Class<?>> entity, SqliteDbHelper.SqlUpdate dbUpdateListener) {

        verReq(context, dbVersion);

        DaoConfig config = new DaoConfig();
        config.setContext(context);
        config.setTargetDirectory(targetDirectory);
        if (!Validate.isEmpty(dbName))
            config.setDbName(dbName);
        config.setDebug(isDebug);
        config.setDbVersion(dbVersion);
        config.setDbUpdateListener(dbUpdateListener);
        if (Validate.isNotEmpty(entity)) {
            config.setEntity(entity);
        }
        return create(config);
    }

    /**
     * 自定义数据库名称、版本、表结构等
     *
     * @param daoConfig
     * @return
     */
    public static DBBase create(DaoConfig daoConfig) {
        return getInstance(daoConfig);
    }

    /**
     * 如果数据库在系统默认位置则选择此方法进行【新增表结构】
     *
     * @param context 句柄(又称上下文，必填)
     * @param dbName  数据库名字(选填：默认数据库名称android.db)
     * @param entity  表集合(必填)
     */
    public static void newCreateTable(Context context, String dbName, List<Class<?>> entity) {

        verReq(context, entity);

        DaoConfig con = new DaoConfig();
        con.setContext(context);
        if (!Validate.isEmpty(dbName))
            con.setDbName(dbName);
        con.setEntity(entity);
        con.setDebug(isDebug);

        SQLiteDatabase db = getSQLiteDatabase(context, isDebug, con.getDbName());
        DBUtil.mgtCreateTable(con, entity, db);

    }

    /**
     * 如果数据库在SDcart位置则选择此方法进行【新增表结构】
     *
     * @param context         句柄(又称上下文，必填)
     * @param targetDirectory db文件路径，可以配置为sdcard的路径
     * @param dbName          数据库名字(选填：默认数据库名称android.db)
     * @param entity          表集合(必填)
     */
    public static void newCreateTable(Context context, String targetDirectory, String dbName, List<Class<?>> entity) {

        verReq(context, entity);

        DaoConfig config = new DaoConfig();
        config.setContext(context);
        if (!Validate.isEmpty(dbName))
            config.setDbName(dbName);
        config.setEntity(entity);
        config.setTargetDirectory(targetDirectory);
        config.setDebug(isDebug);

        SQLiteDatabase db = getSQLiteDatabase(context, isDebug, config.getTargetDirectory(), config.getDbName());
        DBUtil.mgtCreateTable(config, entity, db);
    }

    /**
     * 如果数据库在系统默认位置则选择此方法进行【新增表结构】
     *
     * @param db     句柄(又称上下文，必填)
     * @param entity 表集合(必填)
     */
    public static void newCreateTable(SQLiteDatabase db, List<Class<?>> entity) {
        DaoConfig config = new DaoConfig();
        config.setDebug(isDebug);
        DBUtil.mgtCreateTable(config, entity, db);

    }

    /**
     * 作用：获取sdcard目录中数据库操作对象
     *
     * @param context         句柄(又称上下文)
     * @param targetDirectory 数据库文件在sd卡中的目录(SD路径，例如：/sdcard/test/db/)
     * @param dbName          数据库名字(选填：默认数据库名称android.db)
     * @return
     */
    public static SQLiteDatabase getSQLiteDatabase(Context context, boolean isDebug, String targetDirectory, String dbName) {

        DaoConfig config = new DaoConfig();
        config.setContext(context);
        if (!Validate.isEmpty(dbName))
            config.setDbName(dbName);
        config.setTargetDirectory(targetDirectory);
        config.setDebug(isDebug);
        return create(config).db;
    }

    /**
     * 作用：获取系统默认中数据库操作对象
     *
     * @param context 句柄(又称上下文)
     * @param dbName  数据库名字(选填：默认数据库名称android.db)
     * @return
     */
    public static SQLiteDatabase getSQLiteDatabase(Context context, boolean isDebug, String dbName) {
        DaoConfig config = new DaoConfig();
        config.setContext(context);
        if (!Validate.isEmpty(dbName))
            config.setDbName(dbName);
        config.setDebug(isDebug);
        return create(config).db;
    }

    /**
     * 作用：获取sdcard目录中数据库操作对象
     *
     * @param context         句柄(又称上下文)
     * @param targetDirectory 数据库文件在sd卡中的目录(SD路径，例如：/sdcard/test/db/)
     * @param dbName          数据库名字(选填：默认数据库名称android.db)
     * @return
     */
    public static DBBase getDBBase(Context context, boolean isDebug, String targetDirectory, String dbName) {

        DaoConfig config = new DaoConfig();
        config.setContext(context);
        if (!Validate.isEmpty(dbName))
            config.setDbName(dbName);
        config.setTargetDirectory(targetDirectory);
        config.setDebug(isDebug);
        return create(config);
    }

    /**
     * 作用：获取系统默认中数据库操作对象
     *
     * @param context 句柄(又称上下文)
     * @param dbName  数据库名字(选填：默认数据库名称android.db)
     * @return
     */
    public static DBBase getDBBase(Context context, boolean isDebug, String dbName) {
        DaoConfig config = new DaoConfig();
        config.setContext(context);
        if (!Validate.isEmpty(dbName))
            config.setDbName(dbName);
        config.setDebug(isDebug);
        return create(config);
    }

	/*#############################################--数据库具体操作--#############################################*/

	/*---------------------------------------新增---------------------------------------*/

    /**
     * 增
     *
     * @param tablename 表名
     * @param values    数据
     * @return
     */
    public int mgtInsert(String tablename, ContentValues values) {
        int strid = 0;

        synchronized (SqliteDbHelper.class) {
            ContentValues newValues = null;

            if (values != null) {
                newValues = new ContentValues();
                Set<Entry<String, Object>> set = values.valueSet();
                Iterator<Entry<String, Object>> it = set.iterator();

                while (it.hasNext()) {
                    Entry<String, Object> entry = it.next();
                    newValues.put("[" + entry.getKey() + "]", entry.getValue()
                            + "");
                }

                newValues.remove("[_id]");
            }

            String sql = "insert into " + tablename + " values (" + newValues.toString() + ")";
            DBUtil.debugSql(config, sql);

            db.insert(tablename, null, newValues);

            Cursor cursor = db.rawQuery("select last_insert_rowid() from "
                    + tablename, null);

            if (cursor.moveToFirst())
                strid = cursor.getInt(0);

            DBUtil.closeDB(cursor);
        }

        return strid;
    }

	/*---------------------------------------删除---------------------------------------*/

    /**
     * 删除
     *
     * @author 王力杨
     */
    public void mgtDelete(String tablename, ContentValues values) {
        if (values == null) {// 删除全部
            mgtDelete(tablename, null, null);
        } else {// 有Where条件
            StringBuffer cols = new StringBuffer();
            String[] objs = createDeleteColumnSql(values, cols);

            mgtDelete(tablename, cols.toString(), objs);
        }
    }

    /**
     * 用户可以自定义删除
     *
     * @param tablename   表名
     * @param whereClause 条件语句
     * @param whereArgs   条件值
     */
    public void mgtDelete(String tablename, String whereClause, String[] whereArgs) {
        synchronized (SqliteDbHelper.class) {
            //--------------输出显示--------------//

            String sql = "DELETE FROM " + tablename + (Validate.isEmpty(whereClause) == true ? "" : " WHERE " + whereClause + getParameter(whereArgs));
            DBUtil.debugSql(config, sql);
            //--------------输出显示--------------//
            db.delete(tablename, whereClause, whereArgs);
        }
    }

    /**
     * 清空全部表数据
     */
    public void mgtCleanAllTablesData() {
        try {
            synchronized (SqliteDbHelper.class) {
                db.beginTransaction();// 通过事务
                List<String> listTableName = DBUtil.getALLTableName(db);
                for (String string : listTableName) {
                    if (!Validate.isEmpty(string)) {
                        mgtDelete(string, null);
                    }
                }
                db.setTransactionSuccessful();
                db.endTransaction();
            }
        } catch (Exception ex) {

        }
    }

    /**
     * 根据ID删除数据库内容
     *
     * @param tablename
     * @param id
     */
    public void mgtDelete(String tablename, int id) {
        synchronized (SqliteDbHelper.class) {
            //--------------输出显示--------------//
            String sql = "delete from " + tablename + " WHERE _id=" + new String[]{String.valueOf(id)};
            DBUtil.debugSql(config, sql);
            //--------------输出显示--------------//
            db.delete(tablename, "_id=?", new String[]{String.valueOf(id)});
        }
    }

    /**
     * 拼装删除的where条件
     *
     * @param values
     * @param sb
     * @return
     */
    private String[] createDeleteColumnSql(ContentValues values, StringBuffer sb) {
        int index = 0;
        String[] objs = new String[values.size()];

        Set<Entry<String, Object>> set = values.valueSet();
        Iterator<Entry<String, Object>> it = set.iterator();

        while (it.hasNext()) {
            Entry<String, Object> entry = it.next();
            if (index == 0) {
                sb.append("[" + entry.getKey() + "] = ?");
            } else {
                sb.append(" and  [" + entry.getKey() + "] = ?");
            }

            if (entry.getValue() == null)
                objs[index] = "";
            else
                objs[index] = entry.getValue().toString();
            index++;
        }

        return objs;
    }

	/*---------------------------------------修改---------------------------------------*/

    /**
     * 修改
     *
     * @param tablename 表名
     * @param columns   需要修改的字段
     */
    public void mgtUpdate(String tablename, ContentValues columns) {
        if (columns == null)
            return;

        StringBuffer cols = new StringBuffer();
        String[] objs = createUpdateColumnSql(columns, cols);

        String whe = "where _id = " + columns.get("_id");

        //--------------输出显示--------------//
        String sql = "update " + tablename + " set " + cols.toString() + " "
                + whe + getParameter(objs);
        DBUtil.debugSql(config, sql);
        //--------------输出显示--------------//

        synchronized (SqliteDbHelper.class) {
            db.execSQL("update " + tablename + " set " + cols.toString() + " "
                    + whe, objs);
        }
    }

    /**
     * 修改
     *
     * @param tablename 表名
     * @param columns   需要修改的字段
     * @param wheres    条件
     */
    public void mgtUpdate(String tablename, ContentValues columns, ContentValues wheres) {
        if (columns == null)
            return;

        StringBuffer cols = new StringBuffer();
        String[] objs = createUpdateColumnSql(columns, cols);

        StringBuffer where = new StringBuffer();
        String[] wherearray = createWhereColumnSql(wheres, where);

        List<String> listcols = new ArrayList<String>();
        if (objs != null) {
            for (String value : objs) {
                listcols.add(value);
            }
        }
        if (wherearray != null) {
            for (String value : wherearray) {
                listcols.add(value);
            }
        }

        synchronized (SqliteDbHelper.class) {
            //--------------输出显示--------------//
            try {
                String sql = "update " + tablename + " set " + cols.toString() + " "
                        + where.toString() + getParameter((String[]) listcols.toArray());
                DBUtil.debugSql(config, sql);
            } catch (Exception e) {
            }
            //--------------输出显示--------------//
            db.execSQL("update " + tablename + " set " + cols.toString() + " "
                    + where.toString(), listcols.toArray());
        }
    }

    /**
     * 拼装修改的where条件
     *
     * @param values
     * @param where
     * @return
     */
    private String[] createWhereColumnSql(ContentValues values,
                                          StringBuffer where) {
        String[] objs = null;

        if (values != null && values.size() > 0) {
            where.append("where ");

            int index = 0;
            objs = new String[values.size()];

            Set<Entry<String, Object>> set = values.valueSet();
            Iterator<Entry<String, Object>> it = set.iterator();

            while (it.hasNext()) {
                Entry<String, Object> entry = it.next();
                if (index == 0) {
                    where.append("[" + entry.getKey() + "] = ? ");
                } else {
                    where.append(" and  [" + entry.getKey() + "] = ? ");
                }

                if (entry.getValue() == null)
                    objs[index] = "";
                else
                    objs[index] = entry.getValue().toString();
                index++;
            }
        }

        return objs;
    }

    /**
     * 拼装修改的set条件
     *
     * @param values
     * @param sb
     * @return
     */
    private String[] createUpdateColumnSql(ContentValues values, StringBuffer sb) {
        int index = 0;
        String[] objs = new String[values.size()];

        Set<Entry<String, Object>> set = values.valueSet();
        Iterator<Entry<String, Object>> it = set.iterator();

        while (it.hasNext()) {
            Entry<String, Object> entry = it.next();
            if (index == 0) {
                sb.append("[" + entry.getKey() + "] = ?");
            } else {
                sb.append(" ,  [" + entry.getKey() + "] = ?");
            }

            if (entry.getValue() == null)
                objs[index] = "";
            else
                objs[index] = entry.getValue().toString();
            index++;
        }

        return objs;
    }

	/*---------------------------------------查询---------------------------------------*/

    /**
     * 查询
     *
     * @param tablename 表名
     * @param values    条件
     * @param groupby   分组
     * @param orderby   排序
     * @return
     */
    public Cursor mgtQuery(String tablename, ContentValues values, String groupby, String orderby) {
        String sql = "select * from " + tablename;

        String group = "";
        String order = "";
        String[] objs = null;

        StringBuffer where = new StringBuffer();
        objs = createWhereColumnSql(values, where);

        if (groupby != null && !"".equals(groupby)) {
            group = " GROUP BY " + groupby;
        }

        if (orderby != null && !"".equals(orderby)) {
            order = " order by " + orderby;
        }

        sql = sql + " " + where.toString() + group + order;

        DBUtil.debugSql(config, sql + getParameter(objs));
        return findBySql(sql, objs);
    }

    /**
     * 自定义查询
     *
     * @param tablename 表名
     * @param where     条件
     * @param groupby   分组
     * @param orderby   排序
     * @return
     */
    public Cursor mgtCustomQuery(String tablename, String where, String groupby, String orderby) {
        String sql = "select * from " + tablename;

        String group = "";
        String order = "";

        if (groupby != null && !"".equals(groupby)) {
            group = " GROUP BY " + groupby;
        }

        if (orderby != null && !"".equals(orderby)) {
            order = " order by " + orderby;
        }

        sql = sql + " " + "where " + where + group + order;
        DBUtil.debugSql(config, sql);
        return findBySql(sql, null);
    }

    /**
     * 模糊查询
     *
     * @author 王力杨
     */
    public Cursor mgtFuzzySearch(String tablename, ContentValues values, String groupby, String orderby) {
        String sql = "select * from " + tablename;
        String group = "";
        String order = "";
        String[] objs = null;

        StringBuffer where = new StringBuffer();
        objs = createWhereLikeColumnSql(values, where);

        if (groupby != null && !"".equals(groupby)) {
            group = " GROUP BY " + groupby;
        }

        if (orderby != null && !"".equals(orderby)) {
            order = " order by " + orderby;
        }

        sql = sql + " " + where + group + order;
        DBUtil.debugSql(config, sql + getParameter(objs));
        return findBySql(sql, objs);
    }

    /**
     * 模糊查询where拼装
     *
     * @param values
     * @param where
     * @return
     */
    private String[] createWhereLikeColumnSql(ContentValues values,
                                              StringBuffer where) {
        String[] objs = null;

        if (values != null && values.size() > 0) {
            where.append("where ");

            int index = 0;
            objs = new String[values.size()];

            Set<Entry<String, Object>> set = values.valueSet();
            Iterator<Entry<String, Object>> it = set.iterator();

            while (it.hasNext()) {
                Entry<String, Object> entry = it.next();
                if (index == 0) {
                    where.append("[" + entry.getKey() + "] like ? ");
                } else {
                    where.append(" and  [" + entry.getKey() + "] like ? ");
                }

                if (entry.getValue() == null)
                    objs[index] = "";
                else
                    objs[index] = entry.getValue().toString();
                index++;
            }
        }

        return objs;
    }

    public Cursor findBySql(String sql, String[] args) {
        synchronized (SqliteDbHelper.class) {
            Cursor cursor = db.rawQuery(sql, args);
            return cursor;
        }
    }

	/*---------------------------------------结束---------------------------------------*/

    private String getParameter(String[] whereArgs) {
        String parameter = "";
        if (whereArgs != null) {
            List<String> list = Arrays.asList(whereArgs);
            parameter = "；参数=" + list.toString();
        }
        return parameter;
    }

    /**
     * 验证当前db有没有关闭，如果没有关闭则需要先关闭
     */
    private void closeDB() {
        //如果当前未关闭则需要先关闭在开启
        if (this.db != null && this.db.isOpen()) {
            this.db.close();
            this.db = null;
        }
    }
}