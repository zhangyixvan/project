package com.bonc.shiro.admin.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于封装服务器到客户端的Json返回值
 * @author zhijie.ma
 * @date 2017年12月6日
 *
 * @param <T>
 */
public class JsonResultNew<T> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int state;
	private String message = "";
//	private T data;
	private Map<String,Object> data;
	
	public JsonResultNew() {
		super();
	}
	
	/**
	 * @param state	系统状态
	 * @param message	描述信息
	 * @param code	业务状态
	 * @param data	数据
	 */
	public JsonResultNew(MySystemEnum systemEnum, String message, MyBusinessEnum businessEnum, T data) {
		this.state = systemEnum.getA();
		this.message = message;
		Map<String,Object> hashMap = new HashMap<String,Object>();
		hashMap.put("code", businessEnum.getA());
		hashMap.put("data", data);
		this.data = hashMap;
	}
	
	/**
	 * 系统级错误描述
	 * @param error
	 */
	public JsonResultNew(String error){
		this(MySystemEnum.SYSTEM_ERROR, error, MyBusinessEnum.BUSINESS_ERROR, null);
	}
	
	/**
	 * 业务级自定义，适用于仅有描述，没有返回的数据
	 * @param businessEnum
	 * @param msg
	 */
	public JsonResultNew(MyBusinessEnum businessEnum,String msg){
		this(MySystemEnum.SYSTEM_SUCCESS, msg, businessEnum, null);
	} 
	
	/**
	 * 系统级成功，业务级自定义
	 * @param msg	描述信息
	 * @param businessEnum	
	 */
	public JsonResultNew(String msg,MyBusinessEnum businessEnum,T data){
		this(MySystemEnum.SYSTEM_SUCCESS, msg, businessEnum, data);
	}
	
	/**
	 * 数据请求成功
	 * @param data
	 */
	public JsonResultNew(T data){
		this(MySystemEnum.SYSTEM_SUCCESS, "", MyBusinessEnum.BUSINESS_SUCCESS, data);
	}
	
	/**
	 * 数据请求成功，自定义描述
	 * @param msg
	 * @param data
	 */
	public JsonResultNew(String msg,T data){
		this(MySystemEnum.SYSTEM_SUCCESS, msg, MyBusinessEnum.BUSINESS_SUCCESS, data);
	}
	
	/**
	 * 结果异常
	 * @param e
	 */
	public JsonResultNew(Throwable e){
		this(MySystemEnum.SYSTEM_ERROR, e.getMessage(), MyBusinessEnum.BUSINESS_ERROR, null);
	}
	
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	public Map<String, Object> getMap() {
		return data;
	}

	public void setMap(Map<String, Object> map) {
		this.data = map;
	}

	@Override
	public String toString() {
		return "JsonResultNew [state=" + state + ", message=" + message + ", map=" + data + "]";
	}
	
}
