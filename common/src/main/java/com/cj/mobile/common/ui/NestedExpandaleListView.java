package com.cj.mobile.common.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ExpandableListView;

/**
 * Created by lenovo on 2018/2/1.
 */

public class NestedExpandaleListView extends ExpandableListView {
    public NestedExpandaleListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,

                View.MeasureSpec.AT_MOST);

        //将重新计算的高度传递回去
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}