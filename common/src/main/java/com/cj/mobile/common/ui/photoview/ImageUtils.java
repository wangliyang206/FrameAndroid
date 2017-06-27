package com.cj.mobile.common.ui.photoview;

import android.graphics.PointF;
import android.view.MotionEvent;

/**
 * 图像工具
 *
 * @author 王力杨
 */
public class ImageUtils {
    /**
     * 距离
     *
     * @param event
     * @return
     */
    public static float distance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);//FloatMath
    }

    /**
     * 距离
     *
     * @param p1
     * @param p2
     * @return
     */
    public static float distance(PointF p1, PointF p2) {
        float x = p1.x - p2.x;
        float y = p1.y - p2.y;
        return (float) Math.sqrt(x * x + y * y);//FloatMath
    }

    /**
     * 距离
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static float distance(float x1, float y1, float x2, float y2) {
        float x = x1 - x2;
        float y = y1 - y2;
        return (float) Math.sqrt(x * x + y * y);//FloatMath
    }

    /**
     * 中点，正中央
     *
     * @param event
     * @param point
     */
    public static void midpoint(MotionEvent event, PointF point) {
        float x1 = event.getX(0);
        float y1 = event.getY(0);
        float x2 = event.getX(1);
        float y2 = event.getY(1);
        midpoint(x1, y1, x2, y2, point);
    }

    /**
     * 中点，正中央
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param point
     */
    public static void midpoint(float x1, float y1, float x2, float y2, PointF point) {
        point.x = (x1 + x2) / 2.0f;
        point.y = (y1 + y2) / 2.0f;
    }

    /**
     * Rotates p1 around p2 by angle degrees.(旋转p1 p2角度左右。)
     *
     * @param p1
     * @param p2
     * @param angle
     */
    public void rotate(PointF p1, PointF p2, float angle) {
        float px = p1.x;
        float py = p1.y;
        float ox = p2.x;
        float oy = p2.y;
        p1.x = (float) (Math.cos(angle) * (px - ox) - Math.sin(angle) * (py - oy) + ox);//FloatMath
        p1.y = (float) (Math.sin(angle) * (px - ox) + Math.cos(angle) * (py - oy) + oy);//FloatMath
    }

    public static float angle(PointF p1, PointF p2) {
        return angle(p1.x, p1.y, p2.x, p2.y);
    }

    public static float angle(float x1, float y1, float x2, float y2) {
        return (float) Math.atan2(y2 - y1, x2 - x1);
    }
}
