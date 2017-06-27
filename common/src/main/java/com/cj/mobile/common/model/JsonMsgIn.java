package com.cj.mobile.common.model;

/**
 * 服务器交互时的参数
 * @author 王力杨
 *
 */
public class JsonMsgIn {
	/**类似session id 由服务端颁发 调用需要登陆权限的接口都带上 服务端用于登陆判断*/
	public String sessId;
	//本地接口使用
	public String sessionid;
	
	/**登陆用户id登陆时颁发 调用需要登陆权限的接口都带上 服务端判断sessId所记录的用户是否与此一致*/
	public String uid;
	
	//本地接口使用
	public int version;
	
	/**客户端信息*/
	public Object client;
	
	/**具体接口请求数据 ，其格式由具体接口约定，示例为字符串文本*/
	public Object data;
	
	/**接口名*/
	public String method;
	
	/** token */
	public String token;
	
	/**语言(APP 使用语言，用于服务端返回不同语言信息判断；ZH——中文（简体）； EN——英语；AR——阿拉伯语)*/
	public String language;
}
