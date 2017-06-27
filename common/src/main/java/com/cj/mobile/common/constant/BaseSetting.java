package com.cj.mobile.common.constant;

import com.bumptech.glide.load.engine.cache.DiskCache;
import com.cj.mobile.common.base.BaseApplication;
import com.cj.mobile.common.util.DiskLruCacheHelper;

/**
 * 通用类
 *
 * @author wly
 */
public abstract class BaseSetting {
    /*----------------------------请求内容类型----------------------------*/
    /**
     * json格式
     */
    public static final String CONTENT_TYPE_JSON = "application/json";
    /**
     * json格式-UTF8
     */
    public static final String CONTENT_TYPE_JSON_UTF8 = "application/json;charset=utf-8";
    public static final String CONTENT_TYPE_OCTET_STREAM = "application/octet-stream";
    public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";
    public static final String CONTENT_TYPE_MULTIPART_FORM = "multipart/form-data";
    public static final String CONTENT_TYPE_UTF8 = "UTF-8";

    /**
     * 当前国家价格保留小数点后几位
     */
    public static int CURRENT_COUNTRY_AFTER_DOT_NUM = 2;

    /**
     * 本地保存图片路径
     */
    public static String IMAGE_SAVE_PATH = DiskLruCacheHelper.getDiskCacheDir(BaseApplication.getInstance(), DiskCache.Factory.DEFAULT_DISK_CACHE_DIR).getAbsolutePath();
}
