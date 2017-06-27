package com.cj.mobile.common.model;

import java.util.List;


/**
 * 服务器交互时返回的请求
 * @author 王力杨
 *
 */
public class JsonMsgOut {
	
	/**
	 * 描述：请求码，指当前请求是否请求成功<br/>
	 * 0 代表成功<br/>
	 * 其它代表失败<br/>
	 */
	public int status = 0;
	
	public String version = "";
	public List<Object> errorinfo;
    /**
     * 接口返回数据 ，其格式由具体接口约定，示例为字符串文本<br/>
     */
	public Object data;
    
	/**
	 * 描述：错误信息，具体接口返回的信息或说明
	 */
    public String msg;
}
