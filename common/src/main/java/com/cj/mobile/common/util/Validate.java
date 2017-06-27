package com.cj.mobile.common.util;

import android.widget.EditText;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * 验证
 *
 * @author 王力杨
 */
public class Validate {

    /**
     * 验证List是否非空
     *
     * @param list 需要验证的内容
     * @return 有内容返回：true；没有内容返回：false；
     */
    public static boolean isNotEmpty(List<?> list) {
        return list != null && list.size() > 0 ? true : false;
    }

    /**
     * 验证Map是否非空
     *
     * @param map 需要验证的内容
     * @return 有内容返回：true；没有内容返回：false；
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return map != null && map.size() > 0 ? true : false;
    }

    /**
     * 是否为空
     *
     * @param cs 需要验证的内容
     * @return true或false
     */
    public static boolean isEmpty(CharSequence cs) {
        return (cs == null) || (cs.length() == 0);
    }

    /**
     * 验证Class是否为空
     *
     * @param cla 需要验证的内容
     * @return true或false
     */
    public static boolean isEmpty(Class<?> cla) {
        return cla == null;
    }

    /**
     * 描述：判断一个字符串是否为null或空值.
     *
     * @param str 指定的字符串
     * @return true or false
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 描述：判断一个字符串是否为null或空值.
     *
     * @param str 指定的字符串
     * @return true or false
     */
    public static boolean isEmptyObjByStr(Object str) {
        return str == null || str.toString().length() == 0 || str.equals("null");
    }

    public static String isEmptyReturnZero(String str) {
        return "NUll".equals(str) || "null".equals(str) ? "0.00" : str;
    }

    /**
     * 验证EditText是否有输入内容
     *
     * @param edt EditText控件
     * @return true或false
     */
    public static boolean viewIsEmpty(EditText edt) {
        return isEmpty(edt.getText().toString().trim());
    }

    /**
     * 验证TextView是否有内容
     *
     * @param tv TextView控件
     * @return true或false
     */
    public static boolean viewIsEmpty(TextView tv) {
        return isEmpty(tv.getText().toString().trim());
    }

    /**
     * 如果当前是否非空，则返回当前值
     *
     * @param cs
     * @return
     */
    public static String isEmptyReturnStr(CharSequence cs) {
        return (String) ((cs != null && cs.length() > 0) ? cs : "");
    }

    /**
     * 如果当前是否非空，则返回当前值
     *
     * @param cs
     * @return
     */
    public static Double isEmptyReturnDou(CharSequence cs) {
        return ((cs != null && cs.length() > 0) ? Double.parseDouble(cs.toString()) : null);
    }

    /**
     * 比较两个字符串是否相等
     *
     * @param cs1 字符串1
     * @param cs2 字符串2
     * @return true或false
     */
    public static boolean equals(CharSequence cs1, CharSequence cs2) {
        return cs1 == null ? false : cs2 == null ? true : cs1.equals(cs2);
    }

    /**
     * 比较两个字符串是否相等(执行忽略大小写的比较)
     *
     * @param str1 字符串1
     * @param str2 字符串2
     * @return true或false
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        return str1 == null ? false : str2 == null ? true : str1.equalsIgnoreCase(str2);
    }

    /**
     * 验证对象是否为空，并且对象中的字符串长度大于0
     *
     * @param obj 需要验证的对象
     * @return 有内容返回true，无内容返回false
     */
    public static boolean isNotEmptyAndStrNotEmpty(Object obj) {
        return obj != null && obj.toString().length() > 0;
    }


    /**
     * 验证对象和对象中的内容是否为空
     *
     * @param obj 需要验证的对象
     * @return 对象为空是返回true，对象中的字符串为空时返回true，对象非空并且对象中的字符串非空时返回false
     */
    public static boolean isEmptyOrStrEmpty(Object obj) {
        return obj == null || obj.toString().length() == 0;
    }
}
