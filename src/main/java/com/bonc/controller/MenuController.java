package com.bonc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bonc.service.MenuService;
import com.bonc.shiro.admin.entity.JsonResultNew;
import com.bonc.shiro.admin.entity.MyBusinessEnum;

@RestController
@RequestMapping("/menu")
@RequiresAuthentication
public class MenuController {
	@Autowired
	MenuService menuService;
	/**
	 * 增加菜单
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("sys:menu:add")
	@RequestMapping("/addMenu")
	public JsonResultNew<Object> addMenu(HttpServletRequest request,HttpServletResponse response){
		String menuName=request.getParameter("menuName");
		String menuType=request.getParameter("menuType");
		String parentId=request.getParameter("parentId");
		String perms=request.getParameter("perms");
		String url=request.getParameter("url");
		String ord=request.getParameter("ord");
		String attachment=request.getParameter("attachment");
		String remarks=request.getParameter("remarks");
		String deleteState=request.getParameter("state");
		if(menuName==null||"".equals(menuName)||menuType==null||"".equals(menuType)
				||parentId==null||"".equals(parentId)||ord==null||"".equals(ord)
				||deleteState==null||"".equals(deleteState)) {
			return new JsonResultNew<>("参数传入错误！");
		}
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("menuName", menuName);
		map.put("menuType", menuType);
		map.put("parentId", parentId);
		map.put("perms", perms);
		map.put("url", url);
		map.put("ord", ord);
		map.put("attachment", attachment);
		map.put("remarks", remarks);
		map.put("deleteState", deleteState);
		boolean result=menuService.addMenu(map);
		if(result) {
			return new JsonResultNew<>(MyBusinessEnum.BUSINESS_SUCCESS,"添加成功！");
		} else {
			return new JsonResultNew<Object>(MyBusinessEnum.BUSINESS_ERROR,"添加失败！");
		}
	}
	/**
	 * 修改前的获取
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("sys:menu:upd")
	@RequestMapping("getMenuById")
	public JsonResultNew<Object> getMenuById(HttpServletRequest request,HttpServletResponse response){
		String menuId=request.getParameter("menuId");
		if(menuId==null||"".equals(menuId)) {
			return new JsonResultNew<>("数据传入错误！");
		}
		Map<String,Object> map=menuService.getMenuById(menuId);
		if(map==null||map.isEmpty()) {
			return new JsonResultNew<>(MyBusinessEnum.BUSINESS_ERROR,"没有查找到相关数据！");
		}
		return new JsonResultNew<>(map);
	}
	/**
	 * 修改
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("sys:menu:upd")
	@RequestMapping("updateMenuById")
	public JsonResultNew<Object> updateMenuById(HttpServletRequest request,HttpServletResponse response){
		String menuId=request.getParameter("menuId");
		String menuName=request.getParameter("menuName");
		String menuType=request.getParameter("menuType");
		String parentId=request.getParameter("parentId");
		String perms=request.getParameter("perms");
		String url=request.getParameter("url");
		String ord=request.getParameter("ord");
		String attachment=request.getParameter("attachment");
		String remarks=request.getParameter("remarks");
		String deleteState=request.getParameter("state");
		if(menuName==null||"".equals(menuName)||menuType==null||"".equals(menuType)
				||parentId==null||"".equals(parentId)||ord==null||"".equals(ord)
				||deleteState==null||"".equals(deleteState)||menuId==null||"".equals(menuId)) {
			return new JsonResultNew<>("参数传入错误！");
		}
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("menuId", menuId);
		map.put("menuName", menuName);
		map.put("menuType", menuType);
		map.put("parentId", parentId);
		map.put("perms", perms);
		map.put("url", url);
		map.put("ord", ord);
		map.put("attachment", attachment);
		map.put("remarks", remarks);
		map.put("deleteState", deleteState);
		if(menuService.updateMenuById(map)) {
			return new JsonResultNew<>(MyBusinessEnum.BUSINESS_SUCCESS,"修改成功！");
		} else {
			return new JsonResultNew<Object>(MyBusinessEnum.BUSINESS_ERROR,"修改失败！");
		}
	}
	/**
	 * 删除菜单，真删除(删除菜单的同时也删除角色菜单表中的所有关于该菜单的数据)
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("sys:menu:del")
	@RequestMapping("deleteMenuById")
	public JsonResultNew<Object> deleteMenuById(HttpServletRequest request,HttpServletResponse response){
		String menuId=request.getParameter("menuId");
		if(menuId==null||"".equals(menuId)) {
			return new JsonResultNew<>("数据传入错误！");
		}
		List<Map<String,Object>> list=menuService.getMenuChildById(menuId);
		if(list==null||list.isEmpty()) {
			boolean result=menuService.deleteMenuById(menuId);
			if(result) {
				return new JsonResultNew<>(MyBusinessEnum.BUSINESS_SUCCESS,"删除成功！");
			} else {
				return new JsonResultNew<Object>(MyBusinessEnum.BUSINESS_ERROR,"删除失败！");
			}
		}else {
			return new JsonResultNew<Object>(MyBusinessEnum.BUSINESS_ERROR,"存在子菜单，删除失败！");
		}
		
	}
	
	
}
