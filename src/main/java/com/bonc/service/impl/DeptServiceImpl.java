package com.bonc.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonc.mapper.DeptMapper;
import com.bonc.mapper.SelectMapper;
import com.bonc.service.DeptService;

@Service
public class DeptServiceImpl implements DeptService {
	@Autowired
	DeptMapper deptMapper;
	@Autowired
	SelectMapper selectMapper;
	
	
	@Override
	public boolean addDept(String parentDeptKey, String departmentValue, String description, String createTime,
			String deleteState,String ord) {
		String id=UUID.randomUUID().toString();
		int i=deptMapper.addDept(id,parentDeptKey,departmentValue,description,createTime,deleteState,ord);
//		2018-01-17修改
//		//2018-01-16修改
////		List<Map<String,Object>> users=selectMapper.getAllUsers();
////		List<Map<String,Object>> list=new ArrayList<>();
////		for(Map<String,Object> map:users) {
////			map.put("id", id);
////			list.add(map);
////		}
////		int i2=deptMapper.addDeptUser(list);
////		if(i>0&&i2>0) {
////			return true;
////		}else {
////			return false;
////		}
//		//2018-01-16修改
//		if(i>0) {
//			return true;
//		}else {
//			return false;
//		}
		List<Map<String,Object>> users=selectMapper.getAllUsers();
		List<Map<String,Object>> list=new ArrayList<>();
		for(Map<String,Object> map:users) {
			map.put("id", id);
			map.put("state", "1");
			map.put("roleId", "238d3bc9-0a7d-4c8a-87b4-7cb823c7139e");
			list.add(map);
		}
		int i2=deptMapper.addDeptRoleUser(list);
		if(i>0&&i2>0) {
			return true;
		}else {
			return false;
		}
		
	}

	@Override
	public Map<String, Object> getDeptById(String id) {
		Map<String,Object> map=deptMapper.getDeptById(id);
		if(map==null||map.isEmpty()) {
			return null;
		}
		String parentDeptKey=(String) map.get("parentDeptKey");
		if("0".equals(parentDeptKey)) {
			map.put("parentDeptValue", "总部门");
		}else {
			String parentDeptValue=deptMapper.getDeptValueById(parentDeptKey);
			map.put("parentDeptValue", parentDeptValue);
		}
		return map;
	}

	@Override
	public boolean updateDeptById(String id,String departmentValue, String description, String deleteState,String ord) {
		int i=deptMapper.updateDeptById(departmentValue,id,description,deleteState,ord);
		if(i>0) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public boolean deleteDeptById(String departmentKey) {
		//查找对应的用户,2018-01-16查找从原user_department_1表更改为user_role_dept_1
		List<Map<String,Object>> map=deptMapper.getUserDeptById(departmentKey);
		//查找对应的子部门
		List<Map<String,Object>> map2=deptMapper.getChildDeptById(departmentKey);
		if((map==null||map.isEmpty())&&(map2==null||map2.isEmpty())) {
			int i=deptMapper.deleteDeptById(departmentKey);
			//2018-01-16修改
//			int i2=deptMapper.deleteDeptUserById(departmentKey);
			int i2=deptMapper.deleteDeptRoleUserById(departmentKey);
			if(i>0&&i2>0) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
	
}
