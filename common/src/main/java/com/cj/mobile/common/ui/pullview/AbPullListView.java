package com.cj.mobile.common.ui.pullview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Scroller;

import com.bumptech.glide.RequestManager;

/**
 * 自定义ListView控件，支持下拉刷新和加载更多
 *
 * @author 王力杨
 */
public class AbPullListView extends ListView implements OnScrollListener {
    public interface AbOnListViewListener {

        /**
         * On refresh.
         */
        public void onRefresh();

        /**
         * On load more.
         */
        public void onLoadMore();
    }

    /**
     * The m last y.
     */
    private float mLastY = -1;

    /**
     * The m scroller.
     */
    private Scroller mScroller;

    /**
     * The m list view listener.
     */
    private AbOnListViewListener mListViewListener;

    /**
     * The m header view.
     */
    private AbListViewHeader mHeaderView;

    /**
     * The m footer view.
     */
    private AbListViewFooter mFooterView;

    /**
     * The m header view height.
     */
    private int mHeaderViewHeight;

    /**
     * The m footer view height.
     */
    private int mFooterViewHeight;

    /**
     * The m enable pull refresh.
     */
    private boolean mEnablePullRefresh = true;

    /**
     * The m enable pull load.
     */
    private boolean mEnablePullLoad = true;

    /**
     * The m pull refreshing.
     */
    private boolean mPullRefreshing = false;

    /**
     * The m pull loading.
     */
    private boolean mPullLoading;

    /**
     * The m is footer ready.
     */
    private boolean mIsFooterReady = false;

    /**
     * 总条数.
     */
    private int mTotalItemCount;

    /**
     * The m scroll back.
     */
    private int mScrollBack;

    /**
     * The Constant SCROLLBACK_HEADER.
     */
    private final static int SCROLLBACK_HEADER = 0;

    /**
     * The Constant SCROLLBACK_FOOTER.
     */
    private final static int SCROLLBACK_FOOTER = 1;

    /**
     * The Constant SCROLL_DURATION.
     */
    private final static int SCROLL_DURATION = 200;

    /**
     * The Constant OFFSET_RADIO.
     */
    private final static float OFFSET_RADIO = 1.8f;

    /**
     * 数据相关.
     */
    private ListAdapter mAdapter = null;

    /**
     * 上一次的数量
     */
    private int count = 0;

    /**
     * 新增显示图片对象，作用：在滑动时控制是否显示
     */
    private RequestManager glide;                //显示图片对象
    private boolean pauseOnScroll;        //控制我们缓慢滑动ListView，GridView是否停止加载图片
    private boolean pauseOnFling;        //控制猛的滑动ListView，GridView是否停止加载图片

    /**
     * 构造.
     *
     * @param context the context
     */
    public AbPullListView(Context context) {
        super(context);
        initView(context);
    }

    /**
     * 构造.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public AbPullListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    /**
     * 初始化View.
     *
     * @param context the context
     */
    private void initView(Context context) {

        mScroller = new Scroller(context, new DecelerateInterpolator());

        super.setOnScrollListener(this);

        // init header view
        mHeaderView = new AbListViewHeader(context);

        // init header height
        mHeaderViewHeight = mHeaderView.getHeaderHeight();
        mHeaderView.setGravity(Gravity.BOTTOM);
        addHeaderView(mHeaderView);

        // init footer view
        mFooterView = new AbListViewFooter(context);

        mFooterViewHeight = mFooterView.getFooterHeight();

        //默认是打开刷新与更多
        setPullRefreshEnable(true);
        setPullLoadEnable(true);

        //先隐藏
        mFooterView.hide();
    }

    /**
     * 描述：设置适配器
     */
    @Override
    public void setAdapter(ListAdapter adapter) {
        mAdapter = adapter;
        if (mIsFooterReady == false) {
            mIsFooterReady = true;
            mFooterView.setGravity(Gravity.TOP);
            addFooterView(mFooterView);
        }
        super.setAdapter(adapter);
    }

    /**
     * 打开或者关闭下拉刷新功能.
     *
     * @param enable 开关标记
     */
    public void setPullRefreshEnable(boolean enable) {
        mEnablePullRefresh = enable;
        if (!mEnablePullRefresh) {
            mHeaderView.setVisibility(View.INVISIBLE);
        } else {
            mHeaderView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 打开或者关闭加载更多功能.
     *
     * @param enable 开关标记
     */
    public void setPullLoadEnable(boolean enable) {
        mEnablePullLoad = enable;
        if (!mEnablePullLoad) {
            mFooterView.hide();
            mFooterView.setOnClickListener(null);
        } else {
            mPullLoading = false;
            mFooterView.setState(AbListViewFooter.STATE_READY);
            //load more点击事件.
            mFooterView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startLoadMore();
                }
            });
        }
    }

    /**
     * 停止刷新并重置header的状态.
     */
    public void stopRefresh() {
        if (mPullRefreshing == true) {
            mPullRefreshing = false;
            resetHeaderHeight();
        }
        try {
            count = mAdapter.getCount();
        } catch (Exception e) {

        }
        //判断有没有数据
        if (count > 0) {
            mFooterView.setState(AbListViewFooter.STATE_READY);
        } else {
            mFooterView.setState(AbListViewFooter.STATE_EMPTY);
        }
    }

    /**
     * 更新header的高度.
     *
     * @param delta 差的距离
     */
    private void updateHeaderHeight(float delta) {
        int newHeight = (int) delta + mHeaderView.getVisiableHeight();
        mHeaderView.setVisiableHeight(newHeight);
        if (mEnablePullRefresh && !mPullRefreshing) {
            if (mHeaderView.getVisiableHeight() >= mHeaderViewHeight) {
                mHeaderView.setState(AbListViewHeader.STATE_READY);
            } else {
                mHeaderView.setState(AbListViewHeader.STATE_NORMAL);
            }
        }
        setSelection(0);
    }

    /**
     * 根据状态设置Header的位置.
     */
    private void resetHeaderHeight() {
        //当前下拉到的高度
        int height = mHeaderView.getVisiableHeight();
        if (height < mHeaderViewHeight || !mPullRefreshing) {
            //距离短  隐藏
            mScrollBack = SCROLLBACK_HEADER;
            mScroller.startScroll(0, height, 0, -1 * height, SCROLL_DURATION);
        } else if (height > mHeaderViewHeight || !mPullRefreshing) {
            //距离多的  弹回到mHeaderViewHeight
            mScrollBack = SCROLLBACK_HEADER;
            mScroller.startScroll(0, height, 0, -(height - mHeaderViewHeight), SCROLL_DURATION);
        }

        invalidate();
    }


    /**
     * 开始加载更多.
     */
    public void startLoadMore() {
        Log.d("TAG", "startLoadMore");
        mFooterView.show();
        mPullLoading = true;
        mFooterView.setState(AbListViewFooter.STATE_LOADING);
        if (mListViewListener != null) {
            //开始下载数据
            mListViewListener.onLoadMore();
        }
    }

    /**
     * 停止加载更多并重置footer的状态.
     */
    public void stopLoadMore() {
        mFooterView.hide();
        mPullLoading = false;
        int countNew = 0;
        try {
            countNew = mAdapter.getCount();
        } catch (Exception e) {

        }
        //判断有没有更多数据了
        if (countNew > count) {
            mFooterView.setState(AbListViewFooter.STATE_READY);
        } else {
            mFooterView.setState(AbListViewFooter.STATE_NO);
        }
    }

    /**
     * 描述：onTouchEvent
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                if (mEnablePullRefresh && getFirstVisiblePosition() == 0 && (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
                    updateHeaderHeight(deltaY / OFFSET_RADIO);
                } else if (mEnablePullLoad && !mPullLoading && getLastVisiblePosition() == mTotalItemCount - 1 && deltaY < 0) {
                    startLoadMore();
                }
                break;
            case MotionEvent.ACTION_UP:
                mLastY = -1;
                if (getFirstVisiblePosition() == 0) {
                    //需要刷新的条件
                    if (mEnablePullRefresh && mHeaderView.getVisiableHeight() >= mHeaderViewHeight) {
                        mPullRefreshing = true;
                        mHeaderView.setState(AbListViewHeader.STATE_REFRESHING);
                        if (mListViewListener != null) {
                            //刷新
                            mListViewListener.onRefresh();
                        }
                    }

                    if (mEnablePullRefresh) {
                        //弹回
                        resetHeaderHeight();
                    }
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 描述：TODO
     *
     * @see android.view.View#computeScroll()
     */
    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (mScrollBack == SCROLLBACK_HEADER) {
                mHeaderView.setVisiableHeight(mScroller.getCurrY());
            }
            postInvalidate();
        }
        super.computeScroll();
    }

    /**
     * 描述：设置ListView的监听器.
     *
     * @param listViewListener
     */
    public void setAbOnListViewListener(AbOnListViewListener listViewListener) {
        mListViewListener = listViewListener;
    }

    /**
     * 描述：TODO
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (glide != null) {
            switch (scrollState) {
                case OnScrollListener.SCROLL_STATE_IDLE:
                    glide.resumeRequests();
                    break;
                case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                    if (pauseOnScroll) {
                        glide.pauseRequests();
                    }
                    break;
                case OnScrollListener.SCROLL_STATE_FLING:
                    if (pauseOnFling) {
                        glide.pauseRequests();
                    }
                    break;
            }
        }

    }

    /**
     * 描述：TODO
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mTotalItemCount = totalItemCount;
    }

    /**
     * 描述：获取Header View
     *
     * @return
     * @throws
     */
    public AbListViewHeader getHeaderView() {
        return mHeaderView;
    }

    /**
     * 描述：获取Footer View
     *
     * @return
     * @throws
     */
    public AbListViewFooter getFooterView() {
        return mFooterView;
    }

    /**
     * 描述：获取Header ProgressBar，用于设置自定义样式
     *
     * @return
     * @throws
     */
    public ProgressBar getHeaderProgressBar() {
        return mHeaderView.getHeaderProgressBar();
    }


    /**
     * 描述：获取Footer ProgressBar，用于设置自定义样式
     *
     * @return
     * @throws
     */
    public ProgressBar getFooterProgressBar() {
        return mFooterView.getFooterProgressBar();
    }

    /**
     * 列表滑动时控制图片是否显示
     *
     * @param glide         显示图片对象
     * @param pauseOnScroll 控制我们缓慢滑动ListView，GridView是否停止加载图片
     * @param pauseOnFling  控制猛的滑动ListView，GridView是否停止加载图片
     */
    public void setOnScrollControlPicture(RequestManager glide, boolean pauseOnScroll, boolean pauseOnFling) {
        this.glide = glide;
        this.pauseOnScroll = pauseOnScroll;
        this.pauseOnFling = pauseOnFling;
    }
}
