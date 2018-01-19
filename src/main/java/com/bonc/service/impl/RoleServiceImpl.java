package com.bonc.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonc.mapper.RoleMapper;
import com.bonc.mapper.SelectMapper;
import com.bonc.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService{
	@Autowired
	RoleMapper roleMapper;
	
	@Autowired
	SelectMapper selectMapper;
	
	@Override
	public boolean addRole(String roleName, String remarks, String parentRoleId) {
		String roleId=UUID.randomUUID().toString();
		int i1=roleMapper.addRole(roleId,roleName,remarks,parentRoleId);
		List<Map<String,Object>> list=selectMapper.getAllMenus();
		List<Map<String,Object>> newList=new ArrayList<Map<String,Object>>();
		for(Map<String,Object> map:list) {
			Map<String,Object> newMap=new HashMap<String,Object>();
			newMap.put("roleId", roleId);
			newMap.put("menuId", map.get("menuId"));
			newMap.put("deleteState", "1");
			newList.add(newMap);
		}
		//2018-01-16修改，不再对user_role_1表进行添加默认值操作
//		List<Map<String,Object>> list1=selectMapper.getAllUsers();
//		List<Map<String,Object>> newList1=new ArrayList<>();
//		for(Map<String,Object> oldMap:list1) {
//			Map<String,Object> newMap=new HashMap<>();
//			newMap.put("userId", oldMap.get("userId"));
//			newMap.put("roleId", roleId);
//			newMap.put("state", "1");
//			newList1.add(newMap);
//		}
		int i2=roleMapper.addRoleMenu(newList);
//		int i3=roleMapper.addUserRole(newList1);
		if(i1>0&&i2>0/*&&i3>0*/) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public Map<String,Object> getRoleById(String id) {
		Map<String,Object> map=new HashMap<String,Object>();
		map=roleMapper.getRoleById(id);
		if(map==null||map.isEmpty()) {
			return null;
		}
		String parentRoleId=map.get("parentRoleId").toString();
		if("0".equals(parentRoleId)) {
			map.put("parentRoleName", "");
			return map;
		}
		String parentRoleName=roleMapper.getParentRoleName(parentRoleId);
		map.put("parentRoleName", parentRoleName);
		return map;
	}

	@Override
	public boolean updateRoleById(String roleId,String roleName, String remarks, String parentRoleId) {
		int i=roleMapper.updateRoleById(roleId,roleName,remarks,parentRoleId);
		if(i>0) {
			return true;
		}else {
			return false;
		}
	}
	
	
	@Override
	public List<Map<String,Object>> getRoleMenuById(String roleId){
		List<Map<String,Object>> list=new ArrayList<>();
		//获取这个角色的所有的菜单
		List<Map<String,Object>> roleMenu=roleMapper.getRoleMenu(roleId);
		//将菜单以树状格式的展现
		for(Map<String,Object> menuMap:roleMenu) {
			String parentId=(String) menuMap.get("parentId");
			if("0".equals(parentId)) {
				List<Map<String,Object>> childMenu=getMenuTree(roleMenu,menuMap.get("menuId").toString());
				menuMap.put("childMenu", childMenu);
				list.add(menuMap);
			}
		}
		return list;
	}
	public List<Map<String,Object>> getMenuTree(List<Map<String,Object>> map,String id){
		List<Map<String,Object>> list=new ArrayList<>();
		for(Map<String,Object> menuMap:map) {
			String parentId=(String) menuMap.get("parentId");
			if(id.equals(parentId)) {
				List<Map<String,Object>> childMenu=getMenuTree(map,menuMap.get("menuId").toString());
				menuMap.put("childMenu", childMenu);
				list.add(menuMap);
			}
		}
		return list;
	}

	@Override
	public boolean updateRoleMenuById(List<Map<String,Object>> list) {
		int i=roleMapper.updateRoleMenuById(list);
		if(i>0) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public boolean deleteRoleById(String roleId) {
		//查看这个角色是否存在有用户调用
		List<Map<String,Object>> userRole=roleMapper.getUserRole(roleId);
		//查看这个角色是否存在子角色
		List<Map<String,Object>> childRole=roleMapper.getChildRole(roleId);
		if(userRole==null||userRole.isEmpty()||childRole==null||childRole.isEmpty()) {
			int i1=roleMapper.deleteRoleById(roleId);
			int i2=roleMapper.deleteRoleMenuById(roleId);
//			2018-01-16修改
//			int i3=roleMapper.deleteRoleUserById(roleId);
			if(i1>0&&i2>0/*&&i3>0*/) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
	
	
}
