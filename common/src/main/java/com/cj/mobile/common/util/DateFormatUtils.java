package com.cj.mobile.common.util;

import android.annotation.SuppressLint;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 时间工具类
 * 
 * @author 王力杨
 * 
 */
public class DateFormatUtils {
	/** 时间日期格式化到年月日时分秒. */
	public static String dateFormatYMDHMS = "yyyy-MM-dd HH:mm:ss";

	/** 时间日期格式化到年月日. */
	public static String dateFormatYMD = "yyyy-MM-dd";

	/** 时间日期格式化到年月日. */
	public static String dateFormatchYMD = "yyyy年MM月dd日";

	/** 时间日期格式化到年月. */
	public static String dateFormatYM = "yyyy-MM";

	/** 时间日期格式化到年月日时分. */
	public static String dateFormatYMDHM = "yyyy-MM-dd HH:mm";

	/** 时间日期格式化到月日. */
	public static String dateFormatMD = "MM/dd";

	/** 时分秒. */
	public static String dateFormatHMS = "HH:mm:ss";

	/** 时分. */
	public static String dateFormatHM = "HH:mm";

	public DateFormatUtils() {
	}

	/**
	 * 获取当前时间(yyyy年MM月dd日 HH:mm:ss)
	 * 
	 * @param str
	 *            yyyy年MM月dd日 HH:mm:ss或yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getCurrentTime(String str) {
		SimpleDateFormat formatter = new SimpleDateFormat(str);
		Date curDate = new Date(getCurrentMSTime());// 获取当前时间
		return formatter.format(curDate);
	}

	/**
	 * 获取当前毫秒级时间
	 * 
	 * @return
	 */
	public static long getCurrentMSTime() {
		return System.currentTimeMillis();
	}

	/**
	 * 描述：标准化日期时间类型的数据，不足两位的补0.
	 * 
	 * @param dateTime
	 *            预格式的时间字符串，如:2012-3-2 12:2:20
	 * @return String 格式化好的时间字符串，如:2012-03-02 12:02:20
	 */
	public static String dateTimeFormat(String dateTime) {
		StringBuilder sb = new StringBuilder();
		try {
			if (Validate.isEmpty(dateTime)) {
				return null;
			}
			String[] dateAndTime = dateTime.split(" ");
			if (dateAndTime.length > 0) {
				for (String str : dateAndTime) {
					if (str.indexOf("-") != -1) {
						String[] date = str.split("-");
						for (int i = 0; i < date.length; i++) {
							String str1 = date[i];
							sb.append(StringUtil.format0Right(str1));
							if (i < date.length - 1) {
								sb.append("-");
							}
						}
					} else if (str.indexOf(":") != -1) {
						sb.append(" ");
						String[] date = str.split(":");
						for (int i = 0; i < date.length; i++) {
							String str1 = date[i];
							sb.append(StringUtil.format0Right(str1));
							if (i < date.length - 1) {
								sb.append(":");
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return sb.toString();
	}

	/**
	 * 描述：判断是否是闰年()
	 * <p>
	 * (year能被4整除 并且 不能被100整除) 或者 year能被400整除,则该年为闰年.
	 * 
	 * @param year
	 *            年代（如2012）
	 * @return boolean 是否为闰年
	 */
	public static boolean isLeapYear(int year) {
		if ((year % 4 == 0 && year % 400 != 0) || year % 400 == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 计算两个时间之间相差的天数
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static long diffDays(Date startDate, Date endDate) {
		long days = 0;
		long start = startDate.getTime();
		long end = endDate.getTime();
		// 一天的毫秒数1000 * 60 * 60 * 24=86400000
		days = (end - start) / 86400000;
		return days;
	}

	/**
	 * 计算剩余时间 (多少天多少时多少分)
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static String remainDateToString(Date startDate, Date endDate) {
		StringBuilder result = new StringBuilder();
		if (endDate == null) {
			return "过期";
		}
		long times = endDate.getTime() - startDate.getTime();
		if (times < -1) {
			result.append("过期");
		} else {
			long temp = 1000 * 60 * 60 * 24;
			// 天数
			long d = times / temp;

			// 小时数
			times %= temp;
			temp /= 24;
			long m = times / temp;
			// 分钟数
			times %= temp;
			temp /= 60;
			long s = times / temp;

			result.append(d);
			result.append("天");
			result.append(m);
			result.append("小时");
			result.append(s);
			result.append("分");
		}
		return result.toString();
	}

	/**
	 * 时间类型转换为字符串类型
	 * 
	 * @param date
	 *            时间类型(Date，例：1435134827852)
	 * @param pattern
	 *            模式(例如：yyyy-MM-dd HH:mm:ss或yyyyMMdd HHmmss)
	 * @return 字符串类型(String,例：2015-06-24 16:33:47)
	 */
	@SuppressLint("SimpleDateFormat")
	public static String dateToString(Date date, String pattern) {
		DateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(date);
	}

	/**
	 * 字符串类型转换为时间类型
	 * 
	 * @param dateString
	 *            字符串类型(String,例：2015-06-24 16:33:47)
	 * @param pattern
	 *            模式(例如：yyyy-MM-dd HH:mm:ss或yyyyMMdd HHmmss)
	 * @return 时间类型(Date，例：1435134827852)
	 */
	@SuppressLint("SimpleDateFormat")
	public static Date strToDate(String dateString, String pattern) {
		Date date = null;
		try {
			DateFormat dateFormat = new SimpleDateFormat(pattern);
			date = dateFormat.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/** 时间戳转换成字符窜 */
	@SuppressLint("SimpleDateFormat")
	public static String getDateToString(String time, String pattern) {
		if (Validate.isEmpty(time)) {
			return "";
		}
		long times = Long.valueOf(time);
		Date d = new Date(times * 1000L);
		SimpleDateFormat sf = new SimpleDateFormat(pattern);
		
		return sf.format(d);
	}
	
	/** 时间戳转换成字符窜 */
	@SuppressLint("SimpleDateFormat")
	public static String getDateToStringZH(String time, String pattern) {
		if (Validate.isEmpty(time)) {
			return "";
		}
		Locale.setDefault(Locale.CHINA);
		long times = Long.valueOf(time);
		Date d = new Date(times * 1000L);
		SimpleDateFormat sf = new SimpleDateFormat(pattern);
		
		return sf.format(d);
	}

	/**
	 * long型时间戳转成Str格式(默认)
	 * @param timeMillis		long型时间戳
	 * @param pattern			格式(例如：yyyy-MM-dd等)
     * @return
     */
	public static String timeFormat(long timeMillis, String pattern){
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(new Date(timeMillis));
    }

	/**
	 * long型时间戳转成Str格式（中国）
	 * @param timeMillis		long型时间戳
	 * @param pattern			格式(例如：yyyy-MM-dd等)
	 * @return
	 */
    public static String timeFormatZH(long timeMillis, String pattern){
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
        return format.format(new Date(timeMillis));
    }

	/**
	 * 文件最后修改的时间(Long)转Str格式
	 * @param path				文件路径
	 * @param pattern			格式(例如：yyyy-MM-dd等)
	 * @return
     */
    public static String formatPhotoDate(String path,String pattern){
        File file = new File(path);
        if(file.exists()){
            long time = file.lastModified();
			return timeFormat(time, pattern);
        }
        return "1970-01-01";
    }
}
