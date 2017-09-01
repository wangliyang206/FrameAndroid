package com.cj.mobile.common.util.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.GlideModule;
import com.bumptech.glide.request.target.ViewTarget;
import com.cj.mobile.common.R;
import com.cj.mobile.common.util.DiskLruCacheHelper;

/**
 * @Title: GlideConfiguration
 * @Package com.cj.mobile.common.util.glide
 * @Description: Glide 在默认的 RGB_565 格式下加载的图片质量可以接受的话，可以什么都不做。但如果你觉得难以接受，或者是你的实际需求对图片的质量有更高的要求的话，你可以像下面的代码那样创建一个 GlideModule 子类，把 Bitmap 的格式转换到 ARGB_8888：<br/>
 * 使用方法：
 * 在AndroidManifest.xml文件中添加以下内容：
 * <meta-data android:name="com.cj.mobile.common.util.glide.GlideConfiguration" android:value="GlideModule"/>
 * @author: wly
 * @date: 2016/12/13 10:34
 */
public class GlideConfiguration implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        /*改变图片质量*/
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        /*给图片设置tag*/
        ViewTarget.setTagId(R.id.glide_tag_id);
        /*----------------自定义磁盘缓存----------------*/
        //设置大小和外部与内部(设置最大值到100M)
        int cacheSize100MegaBytes = 104857600;

        //内部
//        builder.setDiskCache(
//                new InternalCacheDiskCacheFactory(context, cacheSize100MegaBytes));

        //外部
//        builder.setDiskCache(
//                new ExternalCacheDiskCacheFactory(context, cacheSize100MegaBytes));

        String IMAGE_SAVE_PATH = DiskLruCacheHelper.getDiskCacheDir(context, DiskCache.Factory.DEFAULT_DISK_CACHE_DIR).getAbsolutePath();

        //自定义特定的位置
        builder.setDiskCache(
                new DiskLruCacheFactory(IMAGE_SAVE_PATH, cacheSize100MegaBytes));

    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
