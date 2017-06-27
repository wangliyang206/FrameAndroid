package com.cj.mobile.common.util;

import android.content.Context;
import android.os.Environment;
import com.jakewharton.disklrucache.DiskLruCache;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @Title: DiskLruCacheHelper
 * @Package com.cj.mobile.common.util
 * @Description: 缓存工具类
 * @author: wly
 * @date: 2016/12/7 10:03
 */
public class DiskLruCacheHelper {

    //缓存操作对象
    private DiskLruCache diskLruCache;

    //版本号
    private int mAppVersion = 1;

    // 同一个key可以对应多少个缓存文件
    private static int valueCount = 1;

    // 一个缓存文件最大可以缓存10M
    private static long maxSize = 10 * 1024 * 1024;

    /**
     * 初始化入口一
     *
     * @param context    句柄
     * @param uniqueName 文件名称(唯一标识，非包含路径；本系统会自动选择缓存路径，在有SD卡情况下选择 /sdcard/Android/data/<application package>/cache 这个路径下面，否则会存放在/data/data/<application package>/cache )
     * @param appVersion 版本号：指定当前应用程序的版本号(每当版本号改变，缓存路径下存储的所有数据都会被清除掉，因为DiskLruCache认为当应用程序有版本更新的时候，所有的数据都应该从网上重新获取)；
     */
    public DiskLruCacheHelper(Context context, String uniqueName, int appVersion) throws IOException {
        mAppVersion = appVersion;
        diskLruCache = DiskLruCache.open(getDiskCacheDir(context, uniqueName), appVersion, valueCount, maxSize);
    }

    /**
     * 初始化入口二
     *
     * @param dir        指定的是数据的缓存地址(有效的文件路径，路径+文件名称)
     * @param appVersion 指定当前应用程序的版本号(每当版本号改变，缓存路径下存储的所有数据都会被清除掉，因为DiskLruCache认为当应用程序有版本更新的时候，所有的数据都应该从网上重新获取)
     * @param maxSize    指定最多可以缓存多少字节的数据
     * @throws IOException
     */
    public DiskLruCacheHelper(File dir, int appVersion, long maxSize) throws IOException {
        this.mAppVersion = appVersion;
        this.maxSize = maxSize;
        diskLruCache = DiskLruCache.open(dir, appVersion, valueCount, maxSize);
    }

    /**
     * 获取相应的缓存目录
     *
     * @param context    上下文(句柄)
     * @param uniqueName 文件名称(唯一标识符)
     * @return
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        // 当SD卡存在或者SD卡不可被移除的时候，就调用getExternalCacheDir()方法来获取缓存路径，否则就调用getCacheDir()方法来获取缓存路径。
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            //获取到 /sdcard/Android/data/<application package>/cache/ 目录，一般存放临时缓存数据
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            //获取 /data/data/<application package>/cache/ 目录
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * 获取 缓存操作对象
     *
     * @return
     */
    public DiskLruCache getCache() {
        return diskLruCache;
    }

    /**
     * 这个方法用于将所有的缓存数据全部删除，比如说网易新闻中的那个手动清理缓存功能，其实只需要调用一下DiskLruCache的delete()方法就可以实现了
     *
     * @throws IOException
     */
    public void clear() throws IOException {
        File dir = diskLruCache.getDirectory();
        long maxSize = diskLruCache.getMaxSize();
        diskLruCache.delete();
        diskLruCache = DiskLruCache.open(dir, mAppVersion, valueCount, maxSize);
    }

    /**
     * 将key使用MD5加密
     *
     * @param key
     * @return
     */
    private String toInternalKey(String key) {
        MD5Util md = new MD5Util();
        return md.getMD5Str(key);
    }

    /**
     * key是否已存在
     *
     * @param key
     * @return
     * @throws IOException
     */
    public boolean contains(String key) throws IOException {
        DiskLruCache.Snapshot snapshot = getCache().get(toInternalKey(key));
        if (snapshot == null) return false;

        snapshot.close();
        return true;
    }

    /**
     * 保存对象缓存
     *
     * @param context
     * @param ser
     * @param key
     */
    public void saveObject(Context context, Serializable ser, String key) {
        ObjectOutputStream oos = null;
        try {
            DiskLruCache.Editor editor = getCache().edit(toInternalKey(key));
            if (editor != null) {
                oos = new ObjectOutputStream(editor.newOutputStream(0));
                oos.writeObject(ser);
                oos.flush();
                editor.commit();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            FileUtil.closeIO(oos);
        }
    }


    /**
     * 读取对象缓存
     *
     * @param context
     * @param key
     * @return
     */
    public Serializable readObject(Context context, String key) {
        ObjectInputStream ois = null;
        try {
            DiskLruCache.Editor editor = getCache().edit(toInternalKey(key));
            ois = new ObjectInputStream(editor.newInputStream(0));
            return (Serializable) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            FileUtil.closeIO(ois);
        }
        return null;
    }
}
