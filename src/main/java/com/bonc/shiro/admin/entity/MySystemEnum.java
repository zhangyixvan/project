package com.bonc.shiro.admin.entity;

/**
 *
 * @author zhijie.ma
 * @date 2017年12月8日
 * 
 */
public enum MySystemEnum {

	/**
	 * 系统级成功
	 */
	SYSTEM_SUCCESS(200),
	
	/**
	 * 系统级失败
	 */
	SYSTEM_ERROR(-1);
	
	private int systemCode;

	private MySystemEnum(int ordinal) {
		// TODO Auto-generated constructor stub
		this.systemCode = ordinal;
	}

	public int getA() {
		return systemCode;
	}

	public void setA(int systemCode) {
		this.systemCode = systemCode;
	}
}
