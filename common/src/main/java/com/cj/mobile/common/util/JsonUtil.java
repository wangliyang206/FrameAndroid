package com.cj.mobile.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

/**
 * 完成对json数据的解析
 * @author 王力杨
 *
 */
public class JsonUtil {
	
	public JsonUtil() {}

    public static String getJsonStr(Object obj) {
        return JsonUtil.createJsonString(obj);
    }
    
    public static <T>T getByJson(String jsonStr,Class<T> cls){
        T t = null;
        
        t = JsonUtil.getObject(jsonStr,cls);

        return t;
    }
    
	/**
	 * 使用JSON工具把数据转换成json对象
	 * @param value 是对解析的集合的类型
	 */
	public static String createJsonString(Object value) {
		String str = JSON.toJSONString(value);
		return str;
	}

	/**
	 * 对单个javabean进行解析
	 * @param <T>
	 * @param json 要解析的json字符串
	 * @param cls 
	 * @return
	 */
	public static <T>T getObject(String json,Class<T> cls){
		T t = null;
		try {
			t = JSON.parseObject(json,cls);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}
	
	/**
	 * 对list类型进行解析
	 * @param <T>
	 * @param json
	 * @param cls
	 * @return
	 */
	public static <T> List<T> getListObject(String json,Class<T> cls){
		List<T> list = new ArrayList<T>();
		try {
			list = JSON.parseArray(json, cls);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 对MapString类型数据进行解析
	 * @param json
	 * @return
	 */
	public static Map<String, String> getMapStr(String json){
		Map<String, String> mapStr = new HashMap<String, String>();
		try {
			mapStr = JSON.parseObject(json, new TypeReference<Map<String, String>>(){});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapStr;
	}
	/**
	 * 对LinkedHashMapString类型数据进行解析
	 * @param json
	 * @return
	 */
	public static LinkedHashMap<String, String> getLinkedHashMapStr(String json){
		LinkedHashMap<String, String> mapStr = new LinkedHashMap<String, String>();
		try {
			mapStr = JSON.parseObject(json, new TypeReference<LinkedHashMap<String, String>>(){});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapStr;
	}
	/**
	 * 对MapObject类型数据进行解析
	 * @param json
	 * @return
	 */
	public static Map<String, Object> getMapObj(String json){
		Map<String, Object> mapStr = new HashMap<String, Object>();
		try {
			mapStr = JSON.parseObject(json, new TypeReference<Map<String, Object>>(){});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapStr;
	}
	
	/**
	 * 对listmap类型进行解析
	 * @param json
	 * @return
	 */
	public static List<Map<String, Object>> getListMap(String json){
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		try {
			list = JSON.parseObject(json,new TypeReference<List<Map<String, Object>>>(){});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
