package com.cj.mobile.common.util;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.protocol.HTTP;

/**
 * 字符集实用工具
 *
 * @author 王力杨
 */
public class CharsetUtils {

    private CharsetUtils() {
    }

    /**
     * 要获得正确的编码序列
     *
     * @param str                需要转的字符串
     * @param charset            字符格式
     * @param judgeCharsetLength 字符串长度
     * @return
     */
    public static String toCharset(final String str, final String charset, int judgeCharsetLength) {
        try {
            String oldCharset = getEncoding(str, judgeCharsetLength);
            return new String(str.getBytes(oldCharset), charset);
        } catch (Throwable ex) {
            return str;
        }
    }

    /**
     * 编码
     *
     * @param str
     * @param judgeCharsetLength
     * @return
     */
    public static String getEncoding(final String str, int judgeCharsetLength) {
        String encode = CharsetUtils.DEFAULT_ENCODING_CHARSET;
        for (String charset : SUPPORT_CHARSET) {
            if (isCharset(str, charset, judgeCharsetLength)) {
                encode = charset;
                break;
            }
        }
        return encode;
    }

    public static boolean isCharset(final String str, final String charset, int judgeCharsetLength) {
        try {
            String temp = str.length() > judgeCharsetLength ? str.substring(0, judgeCharsetLength) : str;
            return temp.equals(new String(temp.getBytes(charset), charset));
        } catch (Throwable e) {
            return false;
        }
    }

    public static final String DEFAULT_ENCODING_CHARSET = HTTP.DEFAULT_CONTENT_CHARSET;

    public static final List<String> SUPPORT_CHARSET = new ArrayList<String>();

    static {
        SUPPORT_CHARSET.add("ISO-8859-1");

        SUPPORT_CHARSET.add("GB2312");
        SUPPORT_CHARSET.add("GBK");
        SUPPORT_CHARSET.add("GB18030");

        SUPPORT_CHARSET.add("US-ASCII");
        SUPPORT_CHARSET.add("ASCII");

        SUPPORT_CHARSET.add("ISO-2022-KR");

        SUPPORT_CHARSET.add("ISO-8859-2");

        SUPPORT_CHARSET.add("ISO-2022-JP");
        SUPPORT_CHARSET.add("ISO-2022-JP-2");

        SUPPORT_CHARSET.add("UTF-8");
    }
}
