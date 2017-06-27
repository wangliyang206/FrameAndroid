package com.cj.mobile.common.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cj.mobile.common.model.DaoConfig;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

/**
 * 数据库工具类(通用方法)
 *
 * @author 王力杨
 */
public class DBUtil {
    /***
     * 所有的比对类型
     */
    private static final Map<Class<?>, String> TYPES;

    static {
        /*-------------------初始化类型----------------*/
        TYPES = new HashMap<Class<?>, String>();
        TYPES.put(byte.class, "BYTE");
        TYPES.put(boolean.class, "INTEGER");
        TYPES.put(short.class, "SHORT");
        TYPES.put(int.class, "INTEGER");
        TYPES.put(long.class, "LONG");
        TYPES.put(String.class, "TEXT");
        TYPES.put(byte[].class, "BLOB");
        TYPES.put(float.class, "FLOAT"); // REAL
        TYPES.put(double.class, "DOUBLE"); // REAL
    }

    /**
     * 对外提供创建表的操作，db可为NULL
     *
     * @param model 实体列表
     * @param db    操作数据库的句柄
     */
    public static void mgtCreateTable(DaoConfig config, List<Class<?>> model, SQLiteDatabase db) {
        synchronized (db) {
            if (model != null && model.size() > 0) {
                String sql = "";
                for (int i = 0; i < model.size(); i++) {
                    sql = getTableBuildingSQL(model.get(i));
                    debugSql(config, sql);
                    db.execSQL(sql);
                }
            }
        }

    }

    /**
     * 对外提供创建表的操作，db可为NULL
     *
     * @param model 实体
     * @param db    操作数据库的句柄
     */
    public static void mgtCreateTable(List<Class<?>> model, SQLiteDatabase db) {
        mgtCreateTable(null, model, db);
    }


    /**
     * 执行SQL语句
     *
     * @param sql
     */
    public static void execSQL(SQLiteDatabase db, String sql) {
        synchronized (db) {
            db.execSQL(sql);
        }
    }

    /**
     * 根据类结构构造表。
     */
    public static String getTableBuildingSQL(Class<?> clazz) {
        StringBuilder strBuilder = new StringBuilder(
                "create table if not exists ");
        strBuilder.append(clazz.getSimpleName());
        strBuilder.append("(");
        // getDeclaredFields():只获取该类文件中声明的字段
        // getFields():获取该类文件、其父类、接口的声明字段
        Field[] arrField = clazz.getDeclaredFields();
        for (int i = arrField.length - 1; i >= 0; i--) {
            Field f = arrField[i];
            String type = TYPES.get(f.getType());
            if (type == null) {
                continue;
            } else {
                if (i != arrField.length - 1) {
                    strBuilder.append(",");
                }

                strBuilder.append("[" + f.getName() + "]" + " " + "[" + type
                        + "]");
                if (f.getName().equals("_id")) {
                    strBuilder.append(" PRIMARY KEY AUTOINCREMENT");
                }
            }
        }
        strBuilder.append(");");
        String val = strBuilder.toString();
        val = val.replace("(,", "(");
        val = val.replace(",);", ");");
        return val;
    }


    /**
     * 获取数据库中的表名称
     *
     * @return
     */
    public static List<String> getALLTableName(SQLiteDatabase db) {
        List<String> tableName = new ArrayList<String>();
        Cursor cursor = null;
        try {
            String sql = "select name from sqlite_master where type ='table'";
            cursor = db.rawQuery(sql, null);

            while (cursor.moveToNext()) {
                String name = cursor.getString(0);
                if (name.equals("android_metadata")
                        || name.equals("sqlite_sequence"))
                    continue;
                tableName.add(name);
            }

        } catch (Exception e) {
            Timber.e("获取数据库中的表名称---报错=" + e.getMessage());
        } finally {
            closeDB(cursor);
        }
        return tableName;
    }

    /**
     * 得到数据库表的列名
     *
     * @param db
     * @param tableName
     * @return
     */
    public static String[] getColumnNames(SQLiteDatabase db, String tableName) {
        String[] columnNames = null;
        Cursor c = null;

        try {
            c = db.rawQuery("PRAGMA table_info(" + tableName + ")", null);
            if (null != c) {
                int columnIndex = c.getColumnIndex("name");
                if (-1 == columnIndex) {
                    return null;
                }

                int index = 0;
                columnNames = new String[c.getCount()];
                for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                    columnNames[index] = c.getString(columnIndex);
                    index++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDB(c);
        }

        return columnNames;
    }

    /**
     * 描述：关闭数据库与游标.
     *
     * @param cursor the cursor
     */
    public static void closeDB(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
    }


    /**
     * 判断某张表是否存在
     *
     * @param tabName 表名
     * @return
     */
    public static boolean tabIsExist(SQLiteDatabase db, String tabName) {
        boolean result = false;
        if (tabName == null) {
            return false;
        }
        Cursor cursor = null;
        try {
            String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"
                    + tabName.trim() + "' ";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
            }

        } catch (Exception e) {
        } finally {
            closeDB(cursor);
        }
        return result;
    }

    /**
     * 在debug模式下打印sql语句
     *
     * @param sql
     */
    public static void debugSql(DaoConfig config, String sql) {
        if (config != null && config.isDebug())
            android.util.Log.d("Debug SQL", ">>>>>>  " + sql);
    }

}
