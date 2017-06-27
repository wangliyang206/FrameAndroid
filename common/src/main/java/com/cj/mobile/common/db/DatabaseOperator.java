package com.cj.mobile.common.db;

import android.R.string;
import android.content.ContentValues;
import android.database.Cursor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 封装了一些通用的方法(实体bean转换成ContentValues、Cursor带数据库数据转换成实体bean)<br>
 * 注意：根据数据库保存位置的不同实现不同的构造方法
 *
 * @author 王力杨
 */
public class DatabaseOperator {
    private DBBase helper;

    public DatabaseOperator(DBBase helper) {
        this.helper = helper;
    }

    // 将Map类型转换为ContentValues类型
    @SuppressWarnings("rawtypes")
    private ContentValues MapToContentValues(Map map) {
        ContentValues contentValues = new ContentValues();

        if (map == null)
            return contentValues;

        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            Object value = map.get(key);
            if (value == null)
                contentValues.put(key, "");
            else
                contentValues.put(key, value.toString());
        }

        return contentValues;
    }

	/*------------------------------------------------------删------------------------------------------------------*/

    /**
     * 删除数据-支持条件删除
     *
     * @param cls    需要删除的表名
     * @param values ContentValues键值对
     */
    public void boDelete(Class<?> cls, ContentValues values) {
        helper.mgtDelete(cls.getSimpleName(), values);
    }

    /**
     * 删除数据-支持条件删除
     *
     * @param cls 需要删除的表名
     * @param map Map键值对
     */
    @SuppressWarnings("rawtypes")
    public void boDelete(Class<?> cls, Map map) {
        boDelete(cls, MapToContentValues(map));
    }

    /***
     * 清空表内容
     */
    public void boDelete(Class<?> cls) {
        helper.mgtDelete(cls.getSimpleName(), null);
    }

    /**
     * 根据_id清除数据
     *
     * @param cls
     * @param _id 表中唯一字段
     */
    public void mgtDelete(Class<?> cls, int _id) {
        helper.mgtDelete(cls.getSimpleName(), _id);
    }

	/*------------------------------------------------------增/改------------------------------------------------------*/

    /**
     * 根据实体对象更新数据库
     *
     * @param obj 实体对象
     */
    public void boUpdate(Object obj) {
        ContentValues values = BeanConversionContentValues(obj);
        helper.mgtUpdate(obj.getClass().getSimpleName(), values);
    }

    /***
     * 更新List
     */
    public void boUpdateList(List<?> list) {
        if (list != null && list.size() > 0) {
            for (Object obj : list) {
                if (obj != null)
                    boUpdate(obj);
            }
        }
    }

    /**
     * 修改 部分 数据支持条件修改
     *
     * @param cls    表名
     * @param values 需要修改的字段
     * @param wheres 条件字段
     */
    public void boUpdate(Class<?> cls, ContentValues values,
                         ContentValues wheres) {
        helper.mgtUpdate(cls.getSimpleName(), values, wheres);
    }

    /**
     * 插入新数据
     *
     * @param obj 实体
     */
    public void boInsert(Object obj) {
        ContentValues values = BeanConversionContentValues(obj);
        helper.mgtInsert(obj.getClass().getSimpleName(), values);
    }

    /***
     * 插入List
     */
    public void boInsertList(List<?> list) {

        if (list != null && list.size() > 0) {
            for (Object obj : list) {
                if (obj != null)
                    boInsert(obj);
            }
        }

    }

	/*------------------------------------------------------查------------------------------------------------------*/

    /**
     * 查询List集合
     *
     * @param cls     表名
     * @param values  条件
     * @param groupby 分组
     * @param orderby 排序
     * @return List<实体对象>
     */
    public <T> List<T> boQueryReturnList(Class<T> cls, ContentValues values,
                                         String groupby, String orderby) {
        Cursor cursor = helper.mgtQuery(cls.getSimpleName(), values, groupby, orderby);
        List<T> list = cursorConversionListBean(cursor, cls);
        DBUtil.closeDB(cursor);
        return list;
    }

    /**
     * 实体查询
     *
     * @param cls    表名
     * @param values 条件
     * @return 实体对象
     */
    public Object boQueryReturnObject(Class<?> cls, ContentValues values,
                                      String orderby) {
        Cursor cursor = helper.mgtQuery(cls.getSimpleName(), values, null, orderby);
        Object object = cursorConversionBean(cursor, cls);
        DBUtil.closeDB(cursor);
        return object;
    }

    /**
     * 自定义查询
     *
     * @param sql  SQL语句
     * @param args SQL语句中的参数
     * @return Cursor(行集合)对象
     */
    public Cursor boCustomQuery(String sql, String[] args) {
        Cursor cursor = helper.findBySql(sql, args);
        return cursor;
    }

    /**
     * 自定义查询
     *
     * @param cls     表名
     * @param where   条件
     * @param groupby 分组
     * @param orderby 排序
     * @return List<实体对象>
     */
    public List<?> boCustomQueryReturnList(Class<?> cls, String where,
                                           String groupby, String orderby) {
        Cursor cursor = helper.mgtCustomQuery(cls.getSimpleName(), where, groupby, orderby);
        List<?> list = (List<?>) cursorConversionListBean(cursor, cls);
        DBUtil.closeDB(cursor);
        return list;
    }

    /**
     * 自定义查询
     *
     * @param cls     表名
     * @param where   条件
     * @param groupby 分组
     * @param orderby 排序
     * @return 实体对象
     */
    public Object boCustomQueryReturnEntity(Class<?> cls, String where,
                                            String groupby, String orderby) {
        Cursor cursor = helper.mgtCustomQuery(cls.getSimpleName(), where, groupby, orderby);
        Object object = cursorConversionBean(cursor, cls);
        DBUtil.closeDB(cursor);
        return object;
    }

    /**
     * 模糊查询
     *
     * @param cls     表名
     * @param values  条件
     * @param groupby 分组
     * @param orderby 排序
     * @return List<实体对象>
     */
    public List<?> boFuzzyQueryReturnList(Class<?> cls, ContentValues values,
                                          String groupby, String orderby) {
        Cursor cursor = helper.mgtFuzzySearch(cls.getSimpleName(), values, groupby, orderby);
        List<?> list = (List<?>) cursorConversionListBean(cursor, cls);
        DBUtil.closeDB(cursor);
        return list;
    }

    /**
     * 模糊查询
     *
     * @param cls    表名
     * @param values 条件
     * @return 返回实体对象
     */
    public Object boFuzzyQueryReturnEntity(Class<?> cls, ContentValues values) {
        Cursor cursor = helper.mgtFuzzySearch(cls.getSimpleName(), values, null, null);
        Object object = cursorConversionBean(cursor, cls);
        DBUtil.closeDB(cursor);
        return object;
    }

    /***
     * 查询全部
     */
    public List<?> boQuery(Class<?> cls) {
        ContentValues values = null;
        return boQueryReturnList(cls, values, null, null);
    }

    /**
     * 根据条件查询条数
     *
     * @param cls    表名
     * @param values 条件
     * @return 条数
     */
    public int count(Class<?> cls, ContentValues values) {
        List<?> list = boQueryReturnList(cls, values, null, null);
        if (list != null)
            return list.size();
        else
            return 0;
    }

    /**
     * 无条件查询表中的数量
     *
     * @param cls 表名
     * @return 数量
     */
    public int count(Class<?> cls) {
        ContentValues values = null;
        List<?> list = boQueryReturnList(cls, values, null, null);
        if (list != null)
            return list.size();
        else
            return 0;
    }

    /**
     * 根据条件查询条数
     *
     * @param cls     表名
     * @param values  条件
     * @param groupby 分组
     * @param orderby 排序
     * @return 数量
     */
    public int count(Class<?> cls, ContentValues values, String groupby,
                     String orderby) {
        List<?> list = boQueryReturnList(cls, values, groupby, orderby);
        if (list != null)
            return list.size();
        else
            return 0;
    }

    /**
     * 指针换成实体【支持查询】
     *
     * @param cursor
     * @param clazz
     * @return
     */
    private Object cursorConversionBean(Cursor cursor, Class<?> clazz) {
        Object obj = null;
        while (cursor.moveToNext()) {
            try {
                obj = clazz.newInstance();
            } catch (Exception e) {
            }
            if (cursor.isBeforeFirst()) {
                cursor.moveToFirst();
            }

            Field[] arrField = clazz.getDeclaredFields();
            try {
                for (Field f : arrField) {
                    String columnName = f.getName();
                    int columnIdx = cursor.getColumnIndex(columnName);
                    if (columnIdx != -1) {
                        Field field = obj.getClass().getDeclaredField(
                                columnName);
                        field.setAccessible(true);// 就是对private
                        // 属性修改的“权限开关”，当设置为true时，可以修改，为false时会抛出异常，动行时信息将会给出该异常.

                        Class<?> type = f.getType();
                        if (type == Byte.class || type == byte.class) {
                            field.set(obj, (byte) cursor.getShort(columnIdx));
                        } else if (type == Short.class || type == short.class) {
                            field.set(obj, cursor.getShort(columnIdx));
                        } else if (type == Integer.class || type == int.class) {
                            field.set(obj, cursor.getInt(columnIdx));
                        } else if (type == Long.class || type == long.class) {
                            field.set(obj, cursor.getLong(columnIdx));
                        } else if (type == String.class || type == string.class) {
                            field.set(obj, cursor.getString(columnIdx));
                        } else if (type == Byte[].class || type == byte[].class) {
                            field.set(obj, cursor.getBlob(columnIdx));
                        } else if (type == Boolean.class
                                || type == boolean.class) {
                            field.set(obj, cursor.getInt(columnIdx) == 1);
                        } else if (type == Float.class || type == float.class) {
                            field.set(obj, cursor.getFloat(columnIdx));
                        } else if (type == Double.class || type == double.class) {
                            field.set(obj, cursor.getDouble(columnIdx));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return obj;
    }

    /**
     * 指针换成实体【支持查询】
     *
     * @param cursor
     * @param clazz
     * @return
     */
    private <T> List<T> cursorConversionListBean(Cursor cursor, Class<T> clazz) {
        List<T> list = new ArrayList<T>();
        while (cursor.moveToNext()) {
            T obj = null;
            try {
                obj = clazz.newInstance();
            } catch (Exception e) {
                System.out
                        .println("cursorConversionListBean(Cursor cursor,Class<?> clazz)-报错");
            }
            if (cursor.isBeforeFirst()) {
                cursor.moveToFirst();
            }
            Field[] arrField = clazz.getDeclaredFields();
            try {
                for (Field f : arrField) {
                    String columnName = f.getName();
                    int columnIdx = cursor.getColumnIndex(columnName);
                    if (columnIdx != -1) {
                        Field field = obj.getClass().getDeclaredField(
                                columnName);
                        field.setAccessible(true);// 就是对private
                        // 属性修改的“权限开关”，当设置为true时，可以修改，为false时会抛出异常，动行时信息将会给出该异常.

                        Class<?> type = f.getType();
                        if (type == Byte.class || type == byte.class) {
                            field.set(obj, (byte) cursor.getShort(columnIdx));
                        } else if (type == Short.class || type == short.class) {
                            field.set(obj, cursor.getShort(columnIdx));
                        } else if (type == Integer.class || type == int.class) {
                            field.set(obj, cursor.getInt(columnIdx));
                        } else if (type == Long.class || type == long.class) {
                            field.set(obj, cursor.getLong(columnIdx));
                        } else if (type == String.class || type == string.class) {
                            field.set(obj, cursor.getString(columnIdx));
                        } else if (type == Byte[].class || type == byte[].class) {
                            field.set(obj, cursor.getBlob(columnIdx));
                        } else if (type == Boolean.class
                                || type == boolean.class) {
                            field.set(obj, cursor.getInt(columnIdx) == 1);
                        } else if (type == Float.class || type == float.class) {
                            field.set(obj, cursor.getFloat(columnIdx));
                        } else if (type == Double.class || type == double.class) {
                            field.set(obj, cursor.getDouble(columnIdx));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            list.add(obj);
        }
        return list;
    }

    /**
     * 类转换成内容值【支持增、删、改】
     */
    private ContentValues BeanConversionContentValues(Object obj) {
        ContentValues values = new ContentValues();
        Field[] arrField = obj.getClass().getDeclaredFields();
        try {
            for (Field f : arrField) {
                if (f.isAccessible() == false) {
                    f.setAccessible(true);
                }
                String name = f.getName();
                Object value = f.get(obj);
                Class<?> type = f.getType();

                if (value instanceof Byte || type == byte[].class) {
                    values.put(name, (Byte) value);
                } else if (value instanceof Short || type == short.class) {
                    values.put(name, (Short) value);
                } else if (value instanceof Integer || type == int.class) {
                    values.put(name, (Integer) value);
                } else if (value instanceof Long || type == long.class) {
                    values.put(name, (Long) value);
                } else if (value instanceof String) {
                    values.put(name, (String) value);
                } else if (value instanceof byte[]) {
                    values.put(name, (byte[]) value);
                } else if (value instanceof Boolean || type == boolean.class) {
                    values.put(name, (Boolean) value);
                } else if (value instanceof Float || type == float.class) {
                    values.put(name, (Float) value);
                } else if (value instanceof Double || type == double.class) {
                    values.put(name, (Double) value);
                }

            }
        } catch (Exception e) {
        }
        return values;
    }
}
