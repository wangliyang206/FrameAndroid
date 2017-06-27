package com.cj.mobile.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 字符串工具类
 *
 * @author 王力杨
 */
public class StringUtil {

    /**
     * 将半角转成全角
     *
     * @param input
     * @return
     */
    public String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 替换、过滤特殊字符
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public String StringFilter(String str) throws PatternSyntaxException {
        str = str.replaceAll("【", "[").replaceAll("】", "]")
                .replaceAll("！", "!").replaceAll("“", "\"")
                .replaceAll("”", "\"").replaceAll("，", ",")
                .replaceAll("1", "1 ").replaceAll("2", "2 ")
                .replaceAll("3", "3 ").replaceAll("4", "4 ")
                .replaceAll("5", "5 ").replaceAll("6", "6 ")
                .replaceAll("7", "7 ").replaceAll("8", "8 ")
                .replaceAll("9", "9 ").replaceAll("0", "0 ");// 替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("");
    }

    /**
     * 产生固定位数随机数
     *
     * @param width 位数
     * @return 字符串
     */
    public static String getRand(int width) {
        Random random = new Random();
        String strRand = "";
        for (int i = 0; i < width; i++) {
            String rand = String.valueOf(random.nextInt(10));
            strRand += rand;
        }
        return strRand;
    }

    /**
     * 将输入流的内容转换成String(字符串)
     *
     * @param is 输入流
     * @return String(字符串)
     */
    public static String getPlainText(InputStream is) {
        try {
            BufferedReader bin = new BufferedReader(new InputStreamReader(is,
                    "UTF-8"));
            StringBuffer sb = new StringBuffer();
            String s = null;
            while ((s = bin.readLine()) != null) {
                sb.append(s);
            }

            return sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 获取性别字符串(1=男、2=女、…=未知)
     *
     * @param sexint 性别代号(int 类型)
     * @return 字符串(String)
     */
    public static String getSexStr(int sexint) {
        if (sexint == 1)
            return "男";
        else if (sexint == 2)
            return "女";
        else
            return "未知";
    }

    /**
     * 获取性别字符串(1=男、2=女、…=未知)
     *
     * @param sexintstr 性别代号(String 类型但内容为数字)
     * @return 字符串(String)
     */
    public static String getSexStr(String sexintstr) {
        try {
            return getSexStr(Integer.parseInt(sexintstr));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "未知";
    }

    /**
     * 获取当前字符串在数组中第几个item
     *
     * @param strs 数组
     * @param item 要比对的字符串
     * @return 数组中的下标
     */
    public static int getIndexFromArray(String[] strs, String item) {
        int index = 0;
        for (int i = 0; i < strs.length; i++) {
            if (item.equals(strs[i])) {
                index = i;
                break;
            }
        }

        return index;
    }

    /**
     * 将字符串中包含英文‘,’、‘;’替换成中文‘，’、‘；’
     *
     * @param input 字符串
     * @return 新的字符串
     */
    public static String repleaceCNToENSplitChar(String input) {
        input = input.replaceAll(",", "，");
        input = input.replaceAll(";", "；");

        return input;
    }

    /**
     * 格式化输出 字符串 [*]左对齐,右补空格
     *
     * @param str
     * @param min_length : 最小输出长度
     * @return
     */
    public static String formatLeftS(String str, int min_length) {
        String format = "%-" + (min_length < 1 ? 1 : min_length) + "s";
        return String.format(format, str);
    }

    /**
     * 格式化输出 整数 [*]右对齐,左补0
     *
     * @param num
     * @param min_length : 最小输出长度
     * @return
     */
    public static String format0Right(long num, int min_length) {
        String format = "%0" + (min_length < 1 ? 1 : min_length) + "d";
        return String.format(format, num);
    }

    /**
     * 描述：不足2个字符的在前面补“0”.
     *
     * @param str 指定的字符串
     * @return 至少2个字符的字符串
     */
    public static String format0Right(String str) {
        try {
            if (str.length() <= 1) {
                str = "0" + str;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 格式化输出 浮点数 [*]右对齐,左补0
     *
     * @param d
     * @param min_length : 最小输出长度
     * @param precision  : 小数点后保留位数
     * @return
     */
    public static String format0Right(double d, int min_length, int precision) {
        String format = "%0" + (min_length < 1 ? 1 : min_length) + "."
                + (precision < 0 ? 0 : precision) + "f";
        return String.format(format, d);
    }

    /**
     * 取消下划线把吧下划线后面的第一个字母转换成大写
     *
     * @param convert
     * @return
     */
    public static String convertStr(String convert) {
        byte aa[] = convert.getBytes();
        for (int i = 0; i < aa.length; i++) {
            // 下划线的 ASCLL=95小写字母a ASCLL=97大写A ASCLL =65
            if (aa[i] == 95 && aa[i + 1] >= 97 && aa[i + 1] <= 123) {
                aa[i + 1] = (byte) (aa[i + 1] - 32);
            }
        }
        String convert1 = new String(aa);
        return convert1.replace("_", "");
    }

    /**
     * 将String数组转换成String类型
     *
     * @param join   分隔符
     * @param strAry 需要转换的数组
     * @return
     */
    public static String Join(String join, String[] strAry) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strAry.length; i++) {
            if (i == (strAry.length - 1)) {
                sb.append(strAry[i]);
            } else {
                sb.append(strAry[i]).append(join);
            }
        }

        return new String(sb);
    }

    /**
     * 描述：获取字符串的长度.
     *
     * @param str 指定的字符串
     * @return 字符串的长度（中文字符计2个）
     */
    public static int strLength(String str) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        if (!Validate.isEmpty(str)) {
            // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
            for (int i = 0; i < str.length(); i++) {
                // 获取一个字符
                String temp = str.substring(i, i + 1);
                // 判断是否为中文字符
                if (temp.matches(chinese)) {
                    // 中文字符长度为2
                    valueLength += 2;
                } else {
                    // 其他字符长度为1
                    valueLength += 1;
                }
            }
        }
        return valueLength;
    }

    /**
     * 删除字符串开头的字符
     *
     * @param str 字符串；例：000215000
     * @param a   字符；例：0
     * @return 结果：215000
     */
    public static String delStringBeginStr(String str, String a) {
        str = str.replaceFirst("^" + a + "+", "");
        return str;
    }

    /**
     * 删除字符串结尾的字符
     *
     * @param str 字符串；例：000215000
     * @param a   字符；例：0
     * @return 结果：000215
     */
    public static String delStringEndStr(String str, String a) {
        str = str.replaceFirst(a + "+$", "");
        return str;
    }

    /**
     * 小数点后保留两个0
     *
     * @param str 字符串；例：12.0
     * @return 结果：12.00
     */
    public static String getString(String str) {
        if (Validate.isEmpty(str)) {
            str = "0.00";
        }
        java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
        double d = Double.parseDouble(str);
        return df.format(d);
    }

    /**
     * 小数点后保留两个0
     *
     * @param value 字符串；例：12.0
     * @return 结果：12.00
     */
    public static String ReservedTwoDecimalPlaces(double value) {
        DecimalFormat df = new DecimalFormat("##0.00");
        return df.format(value);
    }

    /**
     * 小数点后保留两个0
     *
     * @param value       字符串；例：12.0
     * @param paramString true = 将语言先转换成china在进行字符串转换；false = 不做语言转换直接转换字符串
     * @return 结果：12.00
     */
    public static String getChinaString(Double value, boolean paramString) {
        if (value == null) {
            return "0.00";
        }
        if (paramString)
            Locale.setDefault(Locale.CHINA);
        DecimalFormat df = new DecimalFormat("#,##0.00");

        String str = df.format(((double) value));
        if (paramString) {
            String lan = LanguageSettingUtil.get().getLanguage();
            Locale localLocale = new Locale(lan);
            Locale.setDefault(localLocale);
        }
        return str;
    }

    // 把一个字符串中的大写转为小写或小写转为大写
    public static String exChange(String str) {
        String s = null;
        if (str != null) {
            //大写转小写
            s = str.toLowerCase();
            //小写转大写
//			s = str.toUpperCase();

        }

        return s;
    }
}
