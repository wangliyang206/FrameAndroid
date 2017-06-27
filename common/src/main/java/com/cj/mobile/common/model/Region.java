package com.cj.mobile.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 地区(省、市、区县)
 * @author 王力杨
 *
 */
@SuppressWarnings("serial")
public class Region implements Serializable{
	/** 省 */
	public List<Cityinfo> provinces = new ArrayList<Cityinfo>();
	
	/** 市 */
	public HashMap<String, List<Cityinfo>> city = new HashMap<String, List<Cityinfo>>();
	
	/** 区 */
	public HashMap<String, List<Cityinfo>> area = new HashMap<String, List<Cityinfo>>();
}
