package com.cj.mobile.frame;

import android.view.View;

import com.cj.mobile.common.base.BaseBackActivity;
import com.cj.mobile.common.util.ActivityUtils;

import butterknife.OnClick;

/**
 * 包名： com.cj.mobile.frame
 * 对象名： TestActivity
 * 描述：测试
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2017/9/12 10:06
 */

public class TestActivity extends BaseBackActivity {

    @Override
    protected int getViewID() {
        return R.layout.activity_main_frame;
    }

    @OnClick(R.id.txvi_frame_test)
    @Override
    protected void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.txvi_frame_test:
                ActivityUtils.jump(this, TestActivity.class);
                break;
        }
    }
}
