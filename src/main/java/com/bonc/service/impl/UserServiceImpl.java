package com.bonc.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonc.mapper.SelectMapper;
import com.bonc.mapper.UserMapper;
import com.bonc.service.SelectService;
import com.bonc.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	UserMapper userMapper;
	@Autowired
	SelectMapper selectMapper;
	
	@Override
	public Map<String, Object> getUser(String userName) {
		return userMapper.getUser(userName);
	}
	
	@Override
	public boolean addUser(Map<String, Object> map) {
		String userId=UUID.randomUUID().toString();
		map.put("userId", userId);
//		List<Map<String,Object>> deptList=new ArrayList<>();
//		List<Map<String,Object>> dlist=selectMapper.getAllDepts();
//		for(Map<String,Object> oldMap:dlist) {
//			Map<String,Object> deptMap=new HashMap<>();
//			deptMap.put("userId", userId);
//			deptMap.put("deptNo", oldMap.get("departmentKey"));
//			deptMap.put("isAdmin", "1");
//			deptMap.put("deptState", "1");
//			deptList.add(deptMap);
//		}
//		List<Map<String,Object>> list=selectMapper.getAllRoles();
//		List<Map<String,Object>> roleList=new ArrayList<>();
//		for(Map<String,Object> oldMap:list) {
//			Map<String,Object> newMap=new HashMap<>();
//			newMap.put("userId", userId);
//			newMap.put("roleId", oldMap.get("roleId"));
//			newMap.put("state", "1");
//			roleList.add(newMap);
//		}
		List<Map<String,Object>> deptList=new ArrayList<>();
		List<Map<String,Object>> dlist=selectMapper.getAllDepts();
		for(Map<String,Object> oldMap:dlist) {
			Map<String,Object> deptMap=new HashMap<>();
			deptMap.put("userId", userId);
			deptMap.put("deptNo", oldMap.get("departmentKey"));
			deptMap.put("state", "1");
			deptMap.put("roleId", "238d3bc9-0a7d-4c8a-87b4-7cb823c7139e");
			deptList.add(deptMap);
		}
		int i1=userMapper.addUser(map);
		int i2=userMapper.addUserRoleDept(deptList);
//		int i2=userMapper.addUserDept(deptList);
//		int i3=userMapper.addUserRole(roleList);
		if(i1>0&&i2>0/*&&i3>0*/) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public Map<String, Object> getUserById(String userId) {
		Map<String,Object> map=userMapper.getUserById(userId);
		return map;
	}

	@Override
	public boolean updateUserById(Map<String, Object> map) {
		int i=userMapper.updateUserById(map);
//		String userId=map.get("userId").toString();
//		String deptNo=map.get("deptNos").toString();
//		int i2=userMapper.updateUserDeptById(userId,deptNo);
		if(i>0) {
			return true;
		}else {
			return false;
		}
	}
	
	@Override
	public boolean updatePasswordByUserId(String userId, String password) {
		int i=userMapper.updatePasswordByUserId(userId,password);
		if(i>0) {
			return true;
		}else {
			return false;
		}
	}
	

	@Override
	public List<Map<String, Object>> getUserPermsById(String userId) {
		List<Map<String,Object>> list=userMapper.getUserPermsById(userId);
		return list;
	}
	
	
	@Override
	public boolean updateUserPermsById(String userId, String roleId, String state) {
		int i=userMapper.updateUserPermsById(userId,roleId,state);
		if(i>0) {
			return true;
		}else {
			return false;
		}
	}
	@Override
	public boolean updateUserDeptPermsById(String userId, String departmentKey, String roleId) {
		int i=userMapper.updateUserDeptPermsById(userId,departmentKey,roleId);
		if(i>0) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public boolean deleteUserById(String userId) {
//		int i1=userMapper.deleteUserById(userId);
//		int i2=userMapper.deleteUserDeptById(userId);
//		int i3=userMapper.deleteUserRoleById(userId);
//		if(i1>0&&i2>0&&i3>0) {
//			return true;
//		}else {
//			return false;
//		}
//		2018-01-17修改
		int i1=userMapper.deleteUserById(userId);
		int i2=userMapper.deleteUserDeptRoleById(userId);
		if(i1>0&&i2>0) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public List<Map<String, Object>> getAllDeptByUserId(String userId) {
		List<Map<String,Object>> list=userMapper.getAllDeptByUserId(userId);
		List<Map<String,Object>> userDeptList=new ArrayList<>();
		if(list==null||list.isEmpty()) {
			return null;
		}
		for(Map<String,Object> map:list) {
			String parentDept=map.get("parentDept").toString();
			String departmentKey=map.get("departmentKey").toString();
			if("0".equals(parentDept)) {
				map.put("childNode", getChildNode(list,departmentKey));
				userDeptList.add(map);
			}
		}
		return userDeptList;
	}
	public List<Map<String,Object>> getChildNode(List<Map<String,Object>> list,String num){
		List<Map<String,Object>> childList=new ArrayList<>();
		for(Map<String,Object> map:list) {
			String parentDept=map.get("parentDept").toString();
			String departmentKey=map.get("departmentKey").toString();
			if(num.equals(parentDept)) {
				map.put("childNode", getChildNode(list,departmentKey));
				childList.add(map);
			}
		}
		return childList;
	}
	
	
	
	@Override
	public List<Map<String, Object>> getAllUserByUserId(String userId) {
		List<Map<String,Object>> list=userMapper.getUserDeptByUserId(userId);
		if(list==null||list.isEmpty()) {
			return null;
		}
		List<String> departmentKeys=new ArrayList<>();
		for(Map<String,Object> map:list) {
			departmentKeys.add(map.get("departmentKey").toString());
		}
		if(departmentKeys==null||departmentKeys.isEmpty()) {
			return null;
		}
		List<String> newLi=new ArrayList<>(new HashSet<>(departmentKeys));
		List<Map<String,Object>> usersList=userMapper.getUsers(newLi);
		return usersList;
	}

	
	
	
	
	
	@Override
	public List<Map<String, Object>> getUserDeptsById(String userId) {
		List<Map<String,Object>> list=userMapper.getUserDeptsById(userId);
		if(list==null||list.isEmpty()) {
			return null;
		}
		List<Map<String,Object>> newList=new ArrayList<>();
		for(Map<String,Object> map:list) {
			String parentDeptKey=map.get("parentDeptKey").toString();
			String departmentKey=map.get("departmentKey").toString();
			if("0".equals(parentDeptKey)) {
				map.put("childDept", getChildDept(list,departmentKey));
				newList.add(map);
			}
		}
		return newList;
	}
	public List<Map<String,Object>> getChildDept(List<Map<String, Object>> list,String id){
		List<Map<String,Object>> newList=new ArrayList<>();
		for(Map<String,Object> map:list) {
			String parentDeptKey=map.get("parentDeptKey").toString();
			String departmentKey=map.get("departmentKey").toString();
			if(id.equals(parentDeptKey)) {
				map.put("childDept", getChildDept(list,departmentKey));
				newList.add(map);
			}
		}
		return newList;
	}

	@Override
	public boolean updateUserDeptsById(String userId, String departmentKey, String state) {
//		2017-01-17修改
//		String[] departmentKey=departmentKeys.split(",");
//		for(int i=0;i<departmentKey.length;i++) {
//			String[] deptIsAdmin=departmentKey[i].split(":");
//			int k=userMapper.updateUserDeptsById(userId,deptIsAdmin[0].toString(),state,deptIsAdmin[1].toString());
//			if(k<1) {
//				return false;
//			}
//		}
//		return true;
		int i=userMapper.updateUserDeptRoleById(userId,departmentKey,state);
		if(i>0) {
			return true;
		}
		return false;
		
	}
	
	@Override
	public List<Map<String, Object>> getAllUserByDeptNo(String userId,String departmentKey){
//		List<Map<String,Object>> list=userMapper.getAllUserByDeptNo(userId,departmentKey);
//		return list;
		
		//2018-01-18修改：
		List<Map<String,Object>> list=new ArrayList<>();
		//获取当前用户在当前部门中的职位（角色）
		List<Map<String,Object>> rolesList=new ArrayList<>();
		Map<String,Object> map=userMapper.getRoleIdByUidDid(userId,departmentKey);
		if(map==null||map.isEmpty()) {
			return null;
		}
		String roleId=map.get("roleId").toString();
		//根据这个roleId查询其下的所有的角色（递归）
		map.put("childRoles", getChildRoles(roleId));
		rolesList.add(map);	
		//根据这个角色的树结构递归获取所有的roleId
		List<String> roleIds=new ArrayList<>();
		List<String> add=getRoleIds(rolesList);
		if(!(add==null||add.isEmpty())) {
			roleIds.addAll(add);
		}
		//根据获取的roleIds以及传入的departmentKey去数据库查询所有的可操作的用户信息
		for(String roleid:roleIds) {
			Map<String,Object> userMap=userMapper.getAllUserByRlist(departmentKey,roleid);
			if(!(userMap==null||userMap.isEmpty())) {
				list.add(userMap);
			}
		}
		return list;
	}
	public List<String> getRoleIds(List<Map<String,Object>> rolesList){
		if(rolesList==null||rolesList.isEmpty()) {
			return null;
		}
		List<String> roleIds=new ArrayList<>();
		for(Map<String,Object> map:rolesList) {
			roleIds.add(map.get("roleId").toString());
			List<Map<String,Object>> list=(List<Map<String, Object>>) map.get("childRoles");
			List<String> add=getRoleIds(list);
			if(!(add==null||add.isEmpty())) {
				roleIds.addAll(add);
			}
		}
		return roleIds;
	}
	public List<Map<String,Object>> getChildRoles(String id){
		List<Map<String,Object>> newList=new ArrayList<>();
		//获取这个id的下级角色
		List<Map<String,Object>> map=userMapper.getChildRole(id);
		if(map==null||map.isEmpty()) {
			return null;
		}
		for(Map<String,Object> map1:map) {
			String roleId=map1.get("roleId").toString();
			map1.put("childRoles", getChildRoles(roleId));
			newList.add(map1);
		}
		return newList;
	}
	

	@Override
	public Map<String, Object> getUserDeptPermsById(String userId, String departmentKey) {
		Map<String,Object> map=userMapper.getUserDeptPermsById(userId,departmentKey);
		return map;
	}

	
	
	
}
