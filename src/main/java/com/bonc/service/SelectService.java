package com.bonc.service;

import java.util.List;
import java.util.Map;

public interface SelectService {
	/**
	 * 获取所有的角色
	 * @return
	 */
	public List<Map<String,Object>> getAllRoles();
	/**
	 * 获取所有的部门
	 * @return
	 */
	public List<Map<String,Object>> getAllDepts();
	/**
	 * 获取所有的菜单(包括按钮)
	 * @return
	 */
	public List<Map<String,Object>> getAllMenus();
	/**
	 * 获取所有的用户
	 * @return
	 */
	public List<Map<String, Object>> getAllUsers();
}
