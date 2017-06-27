package com.cj.mobile.common.ui.topview;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.BitmapTypeRequest;
import com.bumptech.glide.Glide;
import com.cj.mobile.common.R;
import com.cj.mobile.common.util.MobileUtil;
import com.cj.mobile.common.util.NetworkUtil;
import com.cj.mobile.common.util.ToastManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 顶部轮播图展示控件；<br/>
 * 调用方式：<br/>
 * //验证数据有效性<br/>
 * if (Validate.isNotEmpty(imgUrls)) {<br/>
 * //加载之前先清理一下(old数据，目的是重新加载界面是避免重复数据)；<br/>
 * slidingPlayView.removeAllViews();<br/>
 * //拿到宽度<br/>
 * int diaplayWidth = ActivityUtils.getScreenWidth(this) - ActivityUtils.dip2px(this, 20);<br/>
 * // 设置控件宽度and高度<br/>
 * MobileUtil.setViewGroupWidthHeightBy(slidingPlayView, diaplayWidth, heightPersent);<br/>
 * slidingPlayView.setPageLineHorizontalGravity(Gravity.RIGHT);<br/>
 * TopicImageLayout view = new TopicImageLayout(this, diaplayWidth, heightPersent, null, R.drawable.defaultposter_local1);<br/>
 * for (String item : imgUrls) {<br/>
 * view.addData(item, "", UtilTools.removeHttpsFlag(item));<br/>
 * }<br/>
 * view.loadData(slidingPlayView);<br/>
 * //开启轮播效果<br/>
 * slidingPlayView.startPlay();<br/>
 * //增加点击事件<br/>
 * slidingPlayView.setOnItemClickListener(this);<br/>
 * }<br/>
 * <br/>
 * 说明：<br/>
 * //imgUrls为数据集；<br/>
 * //顶部轮播图的比例(1表示屏幕1:1，0.8表示屏幕1;0.8)<br/>
 * public final double heightPersent = 1;<br/>
 * //配合控件：<br/>
 * AbSlidingPlayView<br/>
 * //跳转大图：<br/>
 * ShowImageActivity<br/>
 * //点击事件：<br/>
 * AbOnItemClickListener<br/>
 */
public class TopicImageLayout {
    // 高度比例
    private double heightPersent;
    // 宽度
    private int screenWidth;
    // 句柄
    private Activity activity;
    /*占位图，未请求下来图片前使用这个展示*/
    private int logo = -1;

    /**
     * 构造方法
     *
     * @param activity      句柄
     * @param screenWidth   屏幕宽度
     * @param heightPersent 高度比例
     * @param logo          加载中的logo
     */
    public TopicImageLayout(Activity activity, int screenWidth, double heightPersent, int logo) {
        this.activity = activity;
        this.heightPersent = heightPersent;
        this.screenWidth = screenWidth;
        this.logo = logo;
    }

    // 需要展示的数据
    private List<ImageDataItem> imageDataItems = new ArrayList<ImageDataItem>();

    public class ImageDataItem {
        public String id; // 唯一ID
        public String digest; // 摘要
        public String image; // 图片ID或名称
    }

    /***
     * 添加数据
     */
    public void addData(String id, String digest, String img) {

        ImageDataItem item = new ImageDataItem();
        item.id = id;
        item.digest = digest;
        item.image = img;
        imageDataItems.add(item);
    }

    /***
     * 刷新列表
     */
    public void loadData(AbSlidingPlayView view) {

        // 下载数据
        toDownloadImages(view);

    }

    private void toDownloadImages(AbSlidingPlayView view) {
        if (!MobileUtil.HasSDCard()) {
            ToastManager.instance().show(activity, R.string.nstall_sd);
            return;
        }
        boolean todownloadimage = NetworkUtil.isNetWorkAvilable(activity);
        if (!todownloadimage)
            return;
        if (imageDataItems != null && imageDataItems.size() > 0) {
            for (int i = 0; i < imageDataItems.size(); i++) {
                ImageDataItem itemImage = imageDataItems.get(i);
                if (itemImage.image == null || "".equals(itemImage.image))
                    continue;
                loadImageSuccess(itemImage.image, view);
            }
        }
    }

    // 图片下载成功后的触发
    @SuppressWarnings("deprecation")
    private void loadImageSuccess(String path, AbSlidingPlayView view) {

        LayoutInflater mInflater = LayoutInflater.from(activity);
        View mPlayView = mInflater.inflate(R.layout.play_view_item, null);
        ImageView imgVi = (ImageView) mPlayView.findViewById(R.id.mPlayImage);

        BitmapTypeRequest builder = Glide.with(activity).load(path).asBitmap();
        if (logo != -1) {
            builder.placeholder(logo).into(imgVi);
        } else {
            builder.into(imgVi);
        }

        ViewGroup.LayoutParams layoutparam = imgVi.getLayoutParams();
        layoutparam.width = screenWidth;
        layoutparam.height = (int) ((double) screenWidth * heightPersent);
        imgVi.setLayoutParams(layoutparam);
        imgVi.setPadding(0, 0, 0, 0);
        view.addView(mPlayView);
    }

}