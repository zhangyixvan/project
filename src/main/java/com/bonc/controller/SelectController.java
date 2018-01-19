package com.bonc.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bonc.service.SelectService;
import com.bonc.shiro.admin.entity.JsonResultNew;
import com.bonc.shiro.admin.entity.MyBusinessEnum;

@RestController
@RequestMapping("/select")
public class SelectController {
	@Autowired
	SelectService selectService;
	
	/**
	 * 获取所有的角色(便于添加菜单时的操作，以及添加新用户时的获取作用)
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getAllRoles")
	@RequiresAuthentication
	public JsonResultNew<Object> getAllRoles(HttpServletRequest request,HttpServletResponse response){
		List<Map<String,Object>> list=selectService.getAllRoles();
		if(list==null||list.isEmpty()) {
			return new JsonResultNew<>(MyBusinessEnum.BUSINESS_ERROR,"未成功获取到数据！");
		}
		return new JsonResultNew<>(list);
	}
	/**
	 * 获取所有的部门(便于添加用户时的操作)
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("getAllDept")
	@RequiresAuthentication
	public JsonResultNew<Object> getAllDept(HttpServletRequest request,HttpServletResponse response){
		List<Map<String,Object>> list=selectService.getAllDepts();
		if(list==null||list.isEmpty()) {
			return new JsonResultNew<>(MyBusinessEnum.BUSINESS_ERROR,"未成功获取到数据！");
		}
		return new JsonResultNew<>(list);
	}
	/**
	 * 获取所有的菜单，包括按钮(便于添加角色时的操作)
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("getAllMenus")
	@RequiresAuthentication
	public JsonResultNew<Object> getAllMenus(HttpServletRequest request,HttpServletResponse response){
		List<Map<String,Object>> list=selectService.getAllMenus();
		if(list==null||list.isEmpty()) {
			return new JsonResultNew<>(MyBusinessEnum.BUSINESS_ERROR,"未成功获取到数据！");
		}
		return new JsonResultNew<>(list);
	}
	/**
	 * 获取所有的用户
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("getAllUsers")
	@RequiresAuthentication
	public JsonResultNew<Object> getAllUsers(HttpServletRequest request,HttpServletResponse response){
		List<Map<String,Object>> list=selectService.getAllUsers();
		if(list==null||list.isEmpty()) {
			return new JsonResultNew<>(MyBusinessEnum.BUSINESS_ERROR,"未成功获取到数据！");
		}
		return new JsonResultNew<>(list);
	}
}
