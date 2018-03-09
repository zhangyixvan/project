package com.bonc.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.bonc.service.RoleService;
import com.bonc.shiro.admin.entity.JsonResultNew;
import com.bonc.shiro.admin.entity.MyBusinessEnum;

@RestController
@RequestMapping("/role")
@RequiresAuthentication
public class RoleController {
	@Autowired
	RoleService roleService;
	
	/**
	 * 添加角色(同时在角色菜单表中添加该角色对应的所有菜单，权限都为不能访问，且设置该角色对应的用户，权限都为不拥有)
	 * 2018-01-16修改，添加角色时，对应的用户角色部门表不进行任何操作
	 * 2018-01-18修改，增加字段：parent_role_id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("sys:role:add")
	@RequestMapping("/addRole")
	public JsonResultNew<Object> addRole(HttpServletRequest request,HttpServletResponse response){
		String roleName=request.getParameter("roleName");
		String remarks=request.getParameter("remarks");
		String parentRoleId=request.getParameter("parentRoleId");
		if(roleName==null||"".equals(roleName)||parentRoleId==null||"".equals(parentRoleId)) {
			return new JsonResultNew<>("参数传入错误！");
		}
		if(roleService.addRole(roleName,remarks,parentRoleId)) {
			return new JsonResultNew<>(MyBusinessEnum.BUSINESS_SUCCESS,"添加成功！");
		} else {
			return new JsonResultNew<>(MyBusinessEnum.BUSINESS_ERROR,"添加失败！");
		}
	}
	
	/**
	 * 修改角色信息前的获取(仅查询角色名，角色备注)
	 * 2018-01-18修改：增加结果：parent_role_id,parent_role_name
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("sys:role:upd")
	@RequestMapping("/getRoleById")
	public 	JsonResultNew<Object> getRoleById(HttpServletRequest request,HttpServletResponse response){
		String id=request.getParameter("roleId");
		if(id==null||"".equals(id)) {
			return new JsonResultNew<>("参数传入错误!");
		}
		Map<String,Object> map=roleService.getRoleById(id);
		if(map==null||map.isEmpty()) {
			return new JsonResultNew<>(MyBusinessEnum.BUSINESS_ERROR,"未获取到数据！");
		}
		return new JsonResultNew<>(map);
	}
	/**
	 * 修改角色的信息(仅修改角色名，角色备注)
	 * 2018-01-18修改：增加修改信息：parent_role_id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("sys:role:upd")
	@RequestMapping("/updateRoleById")
	public JsonResultNew<Object> updateRoleById(HttpServletRequest request,HttpServletResponse response){
		String roleName=request.getParameter("roleName");
		String remarks=request.getParameter("remarks");
		String roleId=request.getParameter("roleId");
		String parentRoleId=request.getParameter("parentRoleId");
		if(roleId==null||"".equals(roleId)||roleName==null||"".equals(roleName)||parentRoleId==null||"".equals(parentRoleId)) {
			return new JsonResultNew<>("参数传入错误!");
		}
		if(roleService.updateRoleById(roleId,roleName,remarks,parentRoleId)) {
			return new JsonResultNew<>(MyBusinessEnum.BUSINESS_SUCCESS,"修改成功！");
		} else {
			return new JsonResultNew<>(MyBusinessEnum.BUSINESS_ERROR,"修改失败！");
		}

	}
	/**
	 * 修改角色菜单权限前的查找
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("sys:role:upd:menu")
	@RequestMapping("/getRoleMenuById")
	public JsonResultNew<Object> getRoleMenuById(HttpServletRequest request,HttpServletResponse response){
		String id=request.getParameter("roleId");
		if(id==null||"".equals(id)) {
			return new JsonResultNew<>("参数传入错误!");
		}
		List<Map<String,Object>> list=roleService.getRoleMenuById(id);
		if(list==null||list.isEmpty()) {
			return new JsonResultNew<>(MyBusinessEnum.BUSINESS_ERROR,"没有查询到数据！");
		}
		return new JsonResultNew<>(list);
	}
	/**
	 * 每次点击权限操作都会访问这个接口修改权限,如果是父子级修改,前端需要传入一个字符串,包含父子的menuId,以","分隔
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("sys:role:upd:menu")
	@RequestMapping("/updateRoleMenuById")
	public JsonResultNew<Object> updateRoleMenuById(HttpServletRequest request,HttpServletResponse response){
		String roleId=request.getParameter("roleId");
		String deleteState=request.getParameter("state");
		String menuId=request.getParameter("menuId");
		if(roleId==null||"".equals(roleId)||deleteState==null||"".equals(deleteState)||menuId==null||"".equals(menuId)) {
			return new JsonResultNew<>("参数传入错误！");
		}
		String[] menuIds=menuId.split(",");
		List<Map<String,Object>> list=new ArrayList<>();
		for(int i=0;i<menuIds.length;i++) {
			Map<String,Object> map=new HashMap<>();
			map.put("roleId", roleId);
			map.put("deleteState", deleteState);
			map.put("menuId", menuIds[i].toString());
			list.add(map);
		}
		if(roleService.updateRoleMenuById(list)) {
			return new JsonResultNew<>(MyBusinessEnum.BUSINESS_SUCCESS,"修改成功！");
		} else {
			return new JsonResultNew<>(MyBusinessEnum.BUSINESS_ERROR,"修改失败！");
		}
	}
	/**
	 * 删除角色，删除前进行了判断，这个角色是否有用户调用，如果有，则删除失败！
	 * 2018-01-16修改，对删除角色前的查找用户从原来的user_role_1表更改为user_role_dept_1
	 * 2018-01-18修改，增加删除时的判断：看看该角色下是否存在子角色
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("sys:role:del")
	@RequestMapping("/deleteRoleById")
	public JsonResultNew<Object> deleteRoleById(HttpServletRequest request,HttpServletResponse response){
		String roleId=request.getParameter("roleId");
		if(roleId==null||"".equals(roleId)) {
			return new JsonResultNew<>("参数传入错误!");
		}
		boolean result= roleService.deleteRoleById(roleId);
		if(result) {
			return new JsonResultNew<>(MyBusinessEnum.BUSINESS_SUCCESS,"删除成功！");
		}else {
			return new JsonResultNew<>(MyBusinessEnum.BUSINESS_ERROR,"删除失败！");
		}

	}
	
}
