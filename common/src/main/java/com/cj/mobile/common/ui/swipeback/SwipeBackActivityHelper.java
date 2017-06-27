package com.cj.mobile.common.ui.swipeback;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import com.cj.mobile.common.R;
import com.cj.mobile.common.util.MobileUtil;

/**
 * 包名： com.cj.mobile.common.ui.swipeback
 * 对象名： SwipeBackActivityHelper
 * 描述：
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2017/4/11 15:54
 */

public class SwipeBackActivityHelper {
    private Activity mActivity;

    private SwipeBackLayout mSwipeBackLayout;

    public SwipeBackActivityHelper(Activity activity) {
        mActivity = activity;
    }

    @SuppressWarnings("deprecation")
    public void onActivityCreate() {
        mActivity.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mActivity.getWindow().getDecorView().setBackgroundDrawable(null);
        mSwipeBackLayout = (SwipeBackLayout) LayoutInflater.from(mActivity).inflate(R.layout.swipeback_layout, null);
        mSwipeBackLayout.addSwipeListener(new SwipeBackLayout.SwipeListener() {
            @Override
            public void onScrollStateChange(int state, float scrollPercent) {
            }

            @Override
            public void onEdgeTouch(int edgeFlag) {
                MobileUtil.convertActivityToTranslucent(mActivity);
            }

            @Override
            public void onScrollOverThreshold() {

            }
        });
    }

    public void onPostCreate() {
        mSwipeBackLayout.attachToActivity(mActivity);
    }

    public View findViewById(int id) {
        if (mSwipeBackLayout != null) {
            return mSwipeBackLayout.findViewById(id);
        }
        return null;
    }

    public SwipeBackLayout getSwipeBackLayout() {
        return mSwipeBackLayout;
    }
}
