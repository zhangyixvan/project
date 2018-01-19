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

import com.alibaba.fastjson.JSONArray;
import com.bonc.service.UserService;
import com.bonc.shiro.admin.entity.JsonResultNew;
import com.bonc.shiro.admin.entity.Md5Util;
import com.bonc.shiro.admin.entity.MyBusinessEnum;

@RestController
@RequestMapping("user")
@RequiresAuthentication
public class UserController {
	@Autowired
	UserService userService;
	
	
	/**
	 * 添加用户,并给这个用户赋予初始权限,即用户权限表中的user_state全部都为1,同时创建其与部门的关系,用户部门表中的dept_state全部为1
	 * 2018-01-16修改为：仅仅添加用户，并不对中间表进行添加操作
	 * 2018-01-17修改为：添加用户时，仅对中间表执行遍历所有部门，对所有的部门进行添加操作，state为1，role_id为空
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("sys:user:add")
	@RequestMapping("/addUser")
	public JsonResultNew<Object> addUser(HttpServletRequest request,HttpServletResponse response){
		String userName=request.getParameter("userName");//用户名
		String password=request.getParameter("password");//密码
		String contactName=request.getParameter("contactName");//真实名字
		String email=request.getParameter("email");//邮箱
//		String deptNos=request.getParameter("departmentKeys");
		String deleteState=request.getParameter("state");//状态，停用状态或启用状态
		String remarks=request.getParameter("remarks");//备注
		if(userName==null||"".equals(userName)||password==null||"".equals(password)||
				contactName==null||"".equals(contactName)||deleteState==null||
				"".equals(deleteState)
//				||deptKey==null||"".equals(deptKey)||roles==null||"".equals(roles)
//				||deptNos==null||"".equals(deptNos)
				) {
			return new JsonResultNew<>("数据传入错误！");
		}
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("userName", userName);
		map.put("password", Md5Util.encoding(password));
		map.put("contactName", contactName);
		map.put("email", email);
		map.put("deleteState", deleteState);
		map.put("remarks", remarks);
//		map.put("deptNos", deptNos);
		Map<String,Object> user=userService.getUser(userName);
		if(user==null||user.isEmpty()) {
			if(userService.addUser(map)) {
				return new JsonResultNew<>(MyBusinessEnum.BUSINESS_SUCCESS,"添加成功！");
			}else {
				return new JsonResultNew<>(MyBusinessEnum.BUSINESS_ERROR,"添加失败！");
			}
		}else {
			return new JsonResultNew<>(MyBusinessEnum.BUSINESS_ERROR,"该用户名已存在！");
		}

	}
	/**
	 * 修改用户信息前的获取，不修改其角色权限
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("sys:user:upd")
	@RequestMapping("getUserById")
	public JsonResultNew<Object> getUserById(HttpServletRequest request,HttpServletResponse response){
		String userId=request.getParameter("userId");
		if(userId==null||"".equals(userId)) {
			return new JsonResultNew<>("数据传入错误！");
		}
		Map<String,Object> map=userService.getUserById(userId);
		if(map==null||map.isEmpty()) {
			return new JsonResultNew<>(MyBusinessEnum.BUSINESS_ERROR,"没有获取到相关数据！");
		}
		return new JsonResultNew<>(map);
	}
	/**
	 * 修改角色信息，不修改其角色权限以及部门权限
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("sys:user:upd")
	@RequestMapping("updateUserById")
	public JsonResultNew<Object> updateUserById(HttpServletRequest request,HttpServletResponse response){
		String userId=request.getParameter("userId");
		String userName=request.getParameter("userName");//用户名
		String contactName=request.getParameter("contactName");//真实名字
		String email=request.getParameter("email");//邮箱
		String deleteState=request.getParameter("state");//状态，停用状态或启用状态
		String remarks=request.getParameter("remarks");
		if(userName==null||"".equals(userName)||
				contactName==null||"".equals(contactName)||deleteState==null||
				"".equals(deleteState)||userId==null||"".equals(userId)) {
			return new JsonResultNew<>("数据传入错误！");
		}
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("userName", userName);
		map.put("contactName", contactName);
		map.put("email", email);
		map.put("deleteState", deleteState);
		map.put("remarks", remarks);
		if(userService.updateUserById(map)) {
			return new JsonResultNew<>(MyBusinessEnum.BUSINESS_SUCCESS,"成功修改！");
		} else {
			return new JsonResultNew<>(MyBusinessEnum.BUSINESS_ERROR,"修改失败！");
		}
	}
	/**
	 * 根据用户的id重置用户的密码
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("sys:user:upd")
	@RequestMapping("updatePasswordByUserId")
	public JsonResultNew<Object> updatePasswordByUserId(HttpServletRequest request,HttpServletResponse response){
		String userId=request.getParameter("userId");
		String password=Md5Util.encoding(request.getParameter("password"));
		if(userId==null||"".equals(userId)||password==null||"".equals(password)) {
			return new JsonResultNew<>("数据传入错误！");
		}
		if(userService.updatePasswordByUserId(userId,password)) {
			return new JsonResultNew<>(MyBusinessEnum.BUSINESS_SUCCESS,"成功修改！");
		} else {
			return new JsonResultNew<>(MyBusinessEnum.BUSINESS_ERROR,"修改失败！");
		}
		
	}
	
	/**
	 * 修改用户在该部门下的角色权限，获取该用户在该部门下的权限
	 * 2018-01-17预计修改：修改角色部门信息为存在之后，修改角色按钮才可以执行（大改动！）
	 * 10:33修改
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("sys:user:upd:role")
	@RequestMapping("getUserPermsById")
	public JsonResultNew<Object> getUserPermsById(HttpServletRequest request,HttpServletResponse response){
		String userId=request.getParameter("userId");
		String departmentKey=request.getParameter("departmentKey");
		if(userId==null||"".equals(userId)||departmentKey==null||"".equals(departmentKey)) {
			return new JsonResultNew<>("参数传入错误！");
		}
//		List<Map<String,Object>> list=userService.getUserPermsById(userId);
//		if(list==null||list.isEmpty()) {
//			return new JsonResultNew<>(MyBusinessEnum.BUSINESS_ERROR,"没有获取到数据！");
//		}
//		return new JsonResultNew<>(list);
		//2018-01-17修改
		Map<String,Object> map=userService.getUserDeptPermsById(userId,departmentKey);
		if(map==null||map.isEmpty()) {
			return new JsonResultNew<>(MyBusinessEnum.BUSINESS_ERROR,"没有获取到数据！");
		}
		return new JsonResultNew<>(map);
	}
	
	/**
	 * 修改用户在该部门下的角色权限，每次点击都进行获取修改
	 * 2018-01-17修改：将原来的表更改为user_role_dept_1
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("sys:user:upd:role")
	@RequestMapping("updateUserPermsById")
	public JsonResultNew<Object> updateUserPermsById(HttpServletRequest request,HttpServletResponse response){
		String userId=request.getParameter("userId");
		String departmentKey=request.getParameter("departmentKey");
		String roleId=request.getParameter("roleId");
		if(userId==null||"".equals(userId)||roleId==null||"".equals(roleId)||departmentKey==null||"".equals(departmentKey)) {
			return new JsonResultNew<>("参数传入错误！");
		}
//		if(userService.updateUserPermsById(userId, roleId, state)) {
//			return new JsonResultNew<>(MyBusinessEnum.BUSINESS_SUCCESS,"修改成功！");
//		} else {
//			return new JsonResultNew<>(MyBusinessEnum.BUSINESS_ERROR,"修改失败！");
//		}
		//2018-01-17修改
		if(userService.updateUserDeptPermsById(userId, departmentKey, roleId)) {
			return new JsonResultNew<>(MyBusinessEnum.BUSINESS_SUCCESS,"修改成功！");
		} else {
			return new JsonResultNew<>(MyBusinessEnum.BUSINESS_ERROR,"修改失败！");
		}
		
	}
	/**
	 * 获取这个用户的部门相关信息
	 * 2018-01-16修改为，没有查找到该用户相关的部门信息的，全部设置为1
	 * 2018-01-17修改为，查找对应的用户部门相关信息，表更改为user_role_dept_1
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("sys:user:upd:dept")
	@RequestMapping("getUserDeptsById")
	public JsonResultNew<Object> getUserDeptsById(HttpServletRequest request,HttpServletResponse response){
		String userId=request.getParameter("userId");
		if(userId==null||"".equals(userId)) {
			return new JsonResultNew<>("参数传入错误！");
		}
		List<Map<String,Object>> list=userService.getUserDeptsById(userId);
		if(list==null||list.isEmpty()) {
			return new JsonResultNew<>(MyBusinessEnum.BUSINESS_ERROR,"没有获取到数据！");
		}
		return new JsonResultNew<>(list);
	}
	/**
	 * 修改这个用户的部门相关信息
	 * 2018-01-17修改为，传入的部门id唯一，不再使用departmentKeys
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("sys:user:upd:dept")
	@RequestMapping("updateUserDeptsById")
	public JsonResultNew<Object> updateUserDeptsById(HttpServletRequest request,HttpServletResponse response){
		String userId=request.getParameter("userId");
//		String departmentKeys=request.getParameter("departmentKeys");
		String departmentKey=request.getParameter("departmentKey");
		String state=request.getParameter("state");
		if(userId==null||"".equals(userId)||departmentKey==null||"".equals(departmentKey)||state==null||"".equals(state)) {
			return new JsonResultNew<>("参数传入错误！");
		}
		if(userService.updateUserDeptsById(userId, departmentKey, state)) {
			return new JsonResultNew<>(MyBusinessEnum.BUSINESS_SUCCESS,"修改成功！");
		} else {
			return new JsonResultNew<>(MyBusinessEnum.BUSINESS_ERROR,"修改失败！");
		}

	}
	
	/**
	 * 删除这个用户的所有相关信息，包括这个用户的部门相关信息，这个用户的角色信息，这个用户的信息
	 * 2018-01-17修改，删除时，只删除这个用户的信息，以及user_role_dept_1表中的该用户的所有的信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("sys:user:del")
	@RequestMapping("deleteUserById")
	public JsonResultNew<Object> deleteUserById(HttpServletRequest request,HttpServletResponse response){
		String userId=request.getParameter("userId");
		if(userId==null||"".equals(userId)) {
			return new JsonResultNew<>("传入的参数错误！");
		}
		if(userService.deleteUserById(userId)) {
			return new JsonResultNew<>(MyBusinessEnum.BUSINESS_SUCCESS,"删除成功！");
		} else {
			return new JsonResultNew<>(MyBusinessEnum.BUSINESS_ERROR,"删除失败！");
		}
	}
	/**
	 * 获取当前登陆用户可操作的部门列表(仅当isAdmin字段为0,deptState字段也为0时可操作)
	 * 2018-01-18修改：仅仅获取当前登陆用户可操作的部门列表，而不管其是否为管理者还是普通员工
	 * 返回值中state为0的表示可以进行操作的，1的表示不可以进行操作的
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("sys:user:read")
	@RequestMapping("getAllDeptByUserId")
	public JsonResultNew<Object> getAllDeptByUserId(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> map=(Map<String, Object>) request.getSession().getAttribute("user");
		String userId=map.get("userId").toString();
		List<Map<String,Object>> list=userService.getAllDeptByUserId(userId);
		if(list==null||list.isEmpty()) {
			return new JsonResultNew<>(MyBusinessEnum.BUSINESS_ERROR,"没有可操作的菜单！");
		}
		return new JsonResultNew<>(list);
	}
	
	
	
//	/**
//	 * 获取当前登陆用户可操作的所有用户列表
//	 * 2018-01-18修改删除此部分
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@RequiresPermissions("sys:user:read")
//	@RequestMapping("getAllUserByUserId")
//	public JsonResultNew<Object> getAllUserByUserId(HttpServletRequest request,HttpServletResponse response){
//		Map<String,Object> map=(Map<String, Object>) request.getSession().getAttribute("user");
//		String userId=map.get("userId").toString();
//		List<Map<String,Object>> list=userService.getAllUserByUserId(userId);
//		if(list==null||list.isEmpty()) {
//			return new JsonResultNew<>(MyBusinessEnum.BUSINESS_ERROR,"没有可操作的用户！");
//		}
//		return new JsonResultNew<>(list);
//	}
	
	
	
	/**
	 * 获取这个部门下的所有的可操作的员工
	 * 2018-01-18修改为：获取这个部门下当前用户的角色可操作的员工数（即当前用户角色对应的下级角色）
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("sys:user:read")
	@RequestMapping("getAllUserByDeptNo")
	public JsonResultNew<Object> getAllUserByDeptNo(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> map=(Map<String, Object>) request.getSession().getAttribute("user");
		String userId=map.get("userId").toString();
		String departmentKey=request.getParameter("departmentKey");
		if(departmentKey==null||"".equals(departmentKey)) {
			return new JsonResultNew<>("参数传入错误！");
		}
		List<Map<String,Object>> list=userService.getAllUserByDeptNo(userId,departmentKey);
		if(list==null||list.isEmpty()) {
			return new JsonResultNew<>(MyBusinessEnum.BUSINESS_ERROR,"没有查询到可以操作的用户");
		}
		return new JsonResultNew<>(list);
	}
	
	
	
}
