package com.cj.mobile.common.ui;

/**
 * 包名： com.cj.mobile.common.ui
 * 对象名： Animatable
 * 描述：
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2017/7/25 15:03
 */
/**
 * Interface that drawables supporting animations should implement.
 * <p>
 * Form:https://github.com/qiujuer/Genius-Android
 *
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public interface Animatable extends android.graphics.drawable.Animatable {
    /**
     * This is drawable animation frame duration
     */
    int FRAME_DURATION = 16;

    /**
     * This is drawable animation duration
     */
    int ANIMATION_DURATION = 250;
}
