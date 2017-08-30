package com.cj.mobile.common.util;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

/**
 * @Title: SharedPreferencesHelper
 * @Package com.cj.mobile.common.util
 * @Description: SharedPreferences文件操作(轻量级的存储类，用于保存APP设置或登录密码之类的信息)
 * @author: wly
 * @date: 2016/12/5 15:09
 */
public class SharedPreferencesHelper {
    private final SharedPreferences mPref;

    @Inject
    public SharedPreferencesHelper(Context context, String fileName) {
        /*
        模式：
        1、Context.MODE_PRIVATE：为默认操作模式，代表该文件是私有数据，只能被应用本身访问，在该模式下，写入的内容会覆盖原文件的内容，如果想把新写入的内容追加到原文件中。可以使用Context.MODE_APPEND
        2、Context.MODE_APPEND：模式会检查文件是否存在，存在就往文件追加内容，否则就创建新文件。
        3、Context.MODE_WORLD_READABLE：表示当前文件可以被其他应用读取；
        4、Context.MODE_WORLD_WRITEABLE：表示当前文件可以被其他应用写入。
        Context.MODE_WORLD_READABLE和Context.MODE_WORLD_WRITEABLE用来控制其他应用是否有权限读写该文件。
        5、Context.MODE_MULTI_PROCESS
        */
        mPref = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    /*----------------------------------------------读取信息-------------------------------------------------*/

    /**
     * 读取保存信息
     *
     * @param key          最初使用保存的键
     * @param defaultValue 默认值
     * @return 有结果则是结果，无结果则是默认值
     */
    public String getPref(String key, String defaultValue) {
        try {
            return mPref.getString(key, defaultValue);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    /**
     * 读取保存信息
     *
     * @param key          最初使用保存的键
     * @param defaultValue 默认值
     * @return 有结果则是结果，无结果则是默认值
     */
    public int getPref(String key, int defaultValue) {
        try {
            return mPref.getInt(key, defaultValue);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    /**
     * 读取保存信息
     *
     * @param key          最初使用保存的键
     * @param defaultValue 默认值
     * @return 有结果则是结果，无结果则是默认值
     */
    public boolean getPref(String key, boolean defaultValue) {
        try {
            return mPref.getBoolean(key, defaultValue);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    /**
     * 读取保存信息
     *
     * @param key          最初使用保存的键
     * @param defaultValue 默认值
     * @return 有结果则是结果，无结果则是默认值
     */
    public long getPref(String key, long defaultValue) {
        try {
            return mPref.getLong(key, defaultValue);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    /**
     * 读取保存信息
     *
     * @param key          最初使用保存的键
     * @param defaultValue 默认值
     * @return 有结果则是结果，无结果则是默认值
     */
    public float getPref(String key, float defaultValue) {
        try {
            return mPref.getFloat(key, defaultValue);
        } catch (Exception ex) {
            return defaultValue;
        }
    }
    /*----------------------------------------------写入信息-------------------------------------------------*/

    /**
     * 保存信息
     *
     * @param key   保存时使用的键
     * @param value 保存值
     */
    public void savePref(String key, String value) {
        mPref.edit().putString(key, value).commit();
    }

    /**
     * 保存信息
     *
     * @param key   保存时使用的键
     * @param value 保存值
     */
    public void savePref(String key, int value) {
        mPref.edit().putInt(key, value).commit();
    }

    /**
     * 保存信息
     *
     * @param key   保存时使用的键
     * @param value 保存值
     */
    public void savePref(String key, boolean value) {
        mPref.edit().putBoolean(key, value).commit();
    }

    /**
     * 保存信息
     *
     * @param key   保存时使用的键
     * @param value 保存值
     */
    public void savePref(String key, long value) {
        mPref.edit().putLong(key, value).commit();
    }

    /**
     * 保存信息
     *
     * @param key   保存时使用的键
     * @param value 保存值
     */
    public void savePref(String key, float value) {
        mPref.edit().putFloat(key, value).commit();
    }
}
