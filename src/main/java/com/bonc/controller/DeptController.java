package com.bonc.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bonc.service.DeptService;
import com.bonc.shiro.admin.entity.JsonResultNew;
import com.bonc.shiro.admin.entity.MyBusinessEnum;

@RestController
@RequestMapping("/dept")
@RequiresAuthentication
public class DeptController {
	@Autowired
	DeptService deptService;
	/**
	 * 添加一个新的部门，并在用户部门表中添加对应的关系，state都为1
	 * 2018-01-16修改为，设置为单次添加，只添加部门信息
	 * 2018-01-17修改为，设置为单词添加，添加部门信息之后，为所有的用户，
	 * 		在用户部门角色表中添加对应的关系，角色role_id都为空，state都为1
	 * 
	 * parentDeptKey 	父亲部门
	 * departmentValue 	部门名称
	 * description 		描述，可为空，可不传
	 * deleteState 		是否删除的判定
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("sys:dept:add")
	@RequestMapping("addDept")
	public JsonResultNew<Object> addDept(HttpServletRequest request,HttpServletResponse response){
		String parentDeptKey=request.getParameter("parentDeptKey");
		String departmentValue=request.getParameter("departmentValue");
		String description=request.getParameter("description");
		String createTime=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String deleteState=request.getParameter("state");
		String ord=request.getParameter("ord");
		if(parentDeptKey==null||"".equals(parentDeptKey)||departmentValue==null||"".equals(departmentValue)
				||deleteState==null||"".equals(deleteState)||ord==null||"".equals(ord)) {
			return new JsonResultNew<Object>("参数传入错误!");
		}
		boolean result=deptService.addDept(parentDeptKey,departmentValue,description,createTime,deleteState,ord);
		if(result) {
			return new JsonResultNew<Object>(MyBusinessEnum.BUSINESS_SUCCESS,"成功！");
		} else {
			return new JsonResultNew<Object>(MyBusinessEnum.BUSINESS_ERROR,"失败！");
		}
	}
	/**
	 * 修改前获取
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("sys:dept:upd")
	@RequestMapping("getDeptById")
	public JsonResultNew<Object> getDeptById(HttpServletRequest request,HttpServletResponse response){
		String departmentKey=request.getParameter("departmentKey");
		if(departmentKey==null||"".equals(departmentKey)) {
			return new JsonResultNew<>("输入传入错误!");
		}
		Map<String,Object> map=deptService.getDeptById(departmentKey);
		if(map==null||map.isEmpty()) {
			return new JsonResultNew<>(MyBusinessEnum.BUSINESS_ERROR,"没有相关的信息!");
		}
		return new JsonResultNew<>(map);
	}
	/**
	 * 修改
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("sys:dept:upd")
	@RequestMapping("updateDeptById")
	public JsonResultNew<Object> updateDeptById(HttpServletRequest request,HttpServletResponse response){
		String departmentKey=request.getParameter("departmentKey");
		String departmentValue=request.getParameter("departmentValue");
		String description=request.getParameter("description");
		String deleteState=request.getParameter("state");
		String ord=request.getParameter("ord");
		if(departmentValue==null||"".equals(departmentValue)||departmentKey==null||"".equals(departmentKey)
				||deleteState==null||"".equals(deleteState)||ord==null||"".equals(ord)) {
			return new JsonResultNew<Object>("参数传入错误!");
		}
		if(deptService.updateDeptById(departmentKey,departmentValue,description,deleteState,ord)){
			return new JsonResultNew<Object>(MyBusinessEnum.BUSINESS_SUCCESS,"成功！");
		} else {
			return new JsonResultNew<Object>(MyBusinessEnum.BUSINESS_ERROR,"失败！");
		}
	}
	/**
	 * 删除，有子部门，有用户存在时不能删除
	 *2018-01-16修改：查找有无用户的表更改为user_role_dept_1
	 *2018-01-17修改：删除时删除user_role_dept_1表中的数据
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("sys:dept:del")
	@RequestMapping("deleteDeptById")
	public JsonResultNew<Object> deleteDeptById(HttpServletRequest request,HttpServletResponse response){
		String departmentKey=request.getParameter("departmentKey");
		if(departmentKey==null||"".equals(departmentKey)) {
			return new JsonResultNew<>("输入传入错误!");
		}
		boolean s=deptService.deleteDeptById(departmentKey);
		
		if(s){
			return new JsonResultNew<Object>(MyBusinessEnum.BUSINESS_SUCCESS,"成功！");
		} else {
			return new JsonResultNew<Object>(MyBusinessEnum.BUSINESS_ERROR,"失败！");
		}
	}
	
}
