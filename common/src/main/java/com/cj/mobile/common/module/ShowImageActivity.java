package com.cj.mobile.common.module;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.cj.mobile.common.R;
import com.cj.mobile.common.module.dialog.ImageMenuDialog;
import com.cj.mobile.common.ui.MyViewPager;
import com.cj.mobile.common.ui.photoview.GestureImageView;
import com.cj.mobile.common.util.DateFormatUtils;
import com.cj.mobile.common.util.MobileUtil;
import com.cj.mobile.common.util.Validate;
import com.cj.mobile.common.util.etoast2.EToast2;
import com.cj.mobile.common.util.etoast2.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * 展示大图片，可支持扩大、缩小、左右滑动、单击关闭等功能；<br/>
 * 传入参数：<br/>
 * 1、key：position，数据类型：int<br/>
 * 2、key：ImagePaths，数据类型：List<\String>
 * 3、key：SavePath，数据类型：String
 *
 * @author 王力杨
 */
public class ShowImageActivity extends Activity {
    /**
     * 当前选中的下标(集合或数组下标)
     */
    private int position;
    private GestureImageView[] mImageViews;
    private MyViewPager viewPager;
    private TextView page;
    private int count;
    /**
     * 图片集合数据
     */
    private ArrayList<String> imagePaths;
    /**
     * 此变量用于长按图片后保存的图片路径（此路径不包含文件名称）
     */
    private String midPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_activity);

        getIntentValue();
        initViews();
    }

    @Override
    protected void onDestroy() {
        Glide.with(this).onDestroy();
        if (mImageViews != null) {
            mImageViews = null;
        }
        super.onDestroy();
    }

    /**
     * 获取从外部传进来的值(URL集合、当前显示item)
     */
    private void getIntentValue() {
        position = this.getIntent().getExtras().getInt("position");
        imagePaths = this.getIntent().getExtras().getStringArrayList("ImagePaths");
        midPath = this.getIntent().getExtras().getString("SavePath", "");
        count = imagePaths.size();
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        page = (TextView) findViewById(com.cj.mobile.common.R.id.txvi_imageactivity_textpage);
        if (count <= 1) {
            page.setVisibility(View.GONE);
        } else {
            page.setVisibility(View.VISIBLE);
            page.setText((position + 1) + "/" + count);
        }

        viewPager = (MyViewPager) findViewById(com.cj.mobile.common.R.id.mvp_imageactivity_viewpager);
        viewPager.setPageMargin(20);
        viewPager.setAdapter(new ImagePagerAdapter(getWebImageViews()));
        viewPager.setCurrentItem(position);
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {

            public void onPageSelected(int arg0) {
                page.setText((arg0 + 1) + "/" + count);
                mImageViews[arg0].reset();
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    /**
     * 保存图片（长按触发图片保存功能）
     *
     * @param url 图片地址
     */
    private void saveImg(String url) {

        //路径为空时默认保存到相册中，否则保存到指定路径下
        if (Validate.isEmpty(midPath))
            midPath = MobileUtil.getCacheSavePath() + "/DCIM/Camera/" + DateFormatUtils.getCurrentMSTime() + ".jpg";
        else
            midPath = midPath + DateFormatUtils.getCurrentMSTime() + ".jpg";

        Glide.with(this)
                .load(url)
                .asBitmap()
                .placeholder(R.drawable.mis_default_error)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        //保存图片
                        MobileUtil.saveImage(resource, midPath);
                        //按路径执行扫描一下，如果保存的位置是相册不扫描相册不显示
                        MobileUtil.MediaScannerConnection(ShowImageActivity.this, midPath);
                        //提示
                        Toast.makeText(ShowImageActivity.this, R.string.tip_save_image_suc, EToast2.LENGTH_SHORT).show();
                    }
                });

    }

    private List<View> getWebImageViews() {
        mImageViews = new GestureImageView[count];
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        List<View> views = new ArrayList<View>();
        for (int i = 0; i < count; i++) {
            View view = layoutInflater.inflate(com.cj.mobile.common.R.layout.imageactivity_item_layout, null);
            final GestureImageView imageView = (GestureImageView) view.findViewById(com.cj.mobile.common.R.id.giv_imageactivity_item_layout_img);
//            final ProgressBar progressBar = (ProgressBar) view.findViewById(com.cj.mobile.common.R.id.prba_imageactivity_item_layout_loading);
            mImageViews[i] = imageView;

//            progressBar.setVisibility(View.VISIBLE);
//            progressBar.setVisibility(View.GONE);

            Glide.with(this)
                    .load(imagePaths.get(i))
                    .placeholder(R.drawable.mis_default_error)
                    .error(R.drawable.mis_default_error)
                    .fitCenter()
                    .into(imageView);

            imageView.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    finish();
                }
            });

            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final String url = imagePaths.get(viewPager.getCurrentItem());
                    final ImageMenuDialog dialog = new ImageMenuDialog(ShowImageActivity.this);
                    dialog.show();
                    dialog.setCancelable(true);
                    dialog.setOnMenuClickListener(new ImageMenuDialog.OnMenuClickListener() {
                        @Override
                        public void onClick(TextView menuItem) {
                            if (menuItem.getId() == R.id.menu1) {
                                saveImg(url);
                            } else if (menuItem.getId() == R.id.menu3) {
                                MobileUtil.copyUrl(ShowImageActivity.this, url);
                            }
                            dialog.dismiss();
                        }
                    });
                    return true;
                }
            });
            views.add(view);
        }
        viewPager.setGestureImages(mImageViews);
        return views;
    }

    private class ImagePagerAdapter extends PagerAdapter {
        private List<View> views;

        public ImagePagerAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public Object instantiateItem(View view, int position) {
            ((ViewPager) view).addView(views.get(position), 0);
            return views.get(position);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (arg1);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
