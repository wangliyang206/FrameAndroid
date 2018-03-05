package com.cj.mobile.frame;

import android.view.View;

import com.cj.mobile.common.base.BaseActivity;
import com.cj.mobile.common.util.ActivityUtils;

import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Override
    protected int getViewID() {
        return R.layout.activity_main_frame;
    }

    @OnClick({
            R.id.txvi_frame_test
    })
    @Override
    protected void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.txvi_frame_test:
                ActivityUtils.jump(this, TestActivity.class);
                break;
        }
    }

    @Override
    protected boolean isNeedLoadStatusBar() {
        return false;
    }
}
