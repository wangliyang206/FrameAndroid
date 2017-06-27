package com.cj.mobile.common.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import java.util.Locale;

/**
 * 语言设置工具类(单例模式)
 * <p>
 * 首先需要在res文件下以国际标准创建对应文件夹，例如：values-en=英语；values-zh=中文简体；
 * <p>
 * 调用方式：LanguageSettingUtil.init(this); //初始化
 * <p>
 * languageSetting = LanguageSettingUtil.get(); //检查是否已经初始化
 * <p>
 * languageSetting.saveLanguage(LanguageSettingUtil.ENGLISH); //设置为"en"语言
 * <p>
 * LanguageSettingUtil.get().refreshLanguage();
 * //刷新(描述：调用刷新以后下一步需要重新调用一下loadData()方法才能正常显示)
 * 
 * @author 王力杨
 * 
 */
public class LanguageSettingUtil {
	// 单例模式-
	private static LanguageSettingUtil instance;
	private Context context;
	// 存储当前系统的language设置-
	private String defaultLanguage;
	// 存储当前系统Locale-
	private Locale defaultLocale;

	/** 英语 */
	public final static String ENGLISH = "en";
	/** 中文简体 */
	public final static String CHINESE = "zh";
	/** 阿拉伯语 */
	public final static String ARAB = "ar";
	/** 印度尼西亚 */
	public final static String ID = "id";

	private LanguageSettingUtil(Context paramContext) {
		// 得到系统语言-
		Locale localLocale = Locale.getDefault();
		this.defaultLocale = localLocale;

		// 保存系统语言到defaultLanguage
		String str = this.defaultLocale.getLanguage();
		this.defaultLanguage = str;

		this.context = paramContext;
	}

	/** 检验自身是否被创建 */
	public static LanguageSettingUtil get() {
		if (instance == null)
			throw new IllegalStateException(
					"language setting not initialized yet");
		return instance;
	}

	/** 初始化 */
	public static void init(Context paramContext) {
		if (instance == null) {
			instance = new LanguageSettingUtil(paramContext);
		}
	}

	// 创建Configuration-
	private Configuration getUpdatedLocaleConfig(String paramString) {

		Configuration localConfiguration = context.getResources()
				.getConfiguration();
		Locale localLocale = getLocale(paramString);
		localConfiguration.locale = localLocale;
		return localConfiguration;
	}

	/** 得到APP配置文件目前的语言设置(如果当前程序没有设置language属性就返回系统语言) */
	public String getLanguage() {
		SharedPreferences localSharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this.context);
		// 如果当前程序没有设置language属性就返回系统语言，如果有，就返回以前的-
		return localSharedPreferences.getString("language",
				this.defaultLanguage);
	}

	/** 得到APP配置文件目前的语言设置(如果当前程序没有设置language属性就返回空) */
	public String getLanguageDefaultNull() {
		SharedPreferences localSharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this.context);
		// 如果当前程序没有设置language属性就返回系统语言，如果有，就返回以前的-
		return localSharedPreferences.getString("language", "");
	}

	/** 如果配置文件中没有语言设置 */
	public Locale getLocale() {
		String str = getLanguage();
		return getLocale(str);
	}

	/** 创建新Locale并覆盖原Locale */
	public Locale getLocale(String paramString) {
		Locale localLocale = new Locale(paramString);
		Locale.setDefault(localLocale);
		return localLocale;
	}

	/** 刷新显示配置 */
	public void refreshLanguage() {
		String str = getLanguage();
		Resources localResources = this.context.getResources();
		if (!localResources.getConfiguration().locale.getLanguage().equals(str)) {
			Configuration localConfiguration = getUpdatedLocaleConfig(str);
			// A structure describing general information about a display, such
			// as its size, density, and font scaling.
			DisplayMetrics localDisplayMetrics = localResources
					.getDisplayMetrics();
			localResources.updateConfiguration(localConfiguration,
					localDisplayMetrics);
		}
	}

	/** 设置系统语言 */
	public void saveLanguage(String paramString) {
		PreferenceManager.getDefaultSharedPreferences(this.context).edit()
				.putString("language", paramString).commit();
	}

	/** 保存系统的语言设置到SharedPreferences */
	public void saveSystemLanguage() {
		PreferenceManager.getDefaultSharedPreferences(this.context).edit()
				.putString("PreSysLanguage", this.defaultLanguage).commit();
	}

	/** 检查系统语言发生变化 */
	public void checkSysChanged(String cuerSysLanguage) {
		// 如果系统语言设置发生变化-
		if (!cuerSysLanguage.equals(PreferenceManager
				.getDefaultSharedPreferences(this.context).getString(
						"PreSysLanguage", "zh"))) {
			// 如果系统保存了this对象，就在这里修改defaultLanguage的值为当前系统语言cuerSysLanguage
			this.defaultLanguage = cuerSysLanguage;
			saveLanguage(cuerSysLanguage);
			saveSystemLanguage();
		}
	}
}
