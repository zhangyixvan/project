package com.bonc.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonc.mapper.SelectMapper;
import com.bonc.service.SelectService;
@Service
public class SelectServiceImpl implements SelectService{
	@Autowired
	SelectMapper selectMapper;
	
	public List<Map<String,Object>> getAllRoles() {
		List<Map<String,Object>> list=selectMapper.getAllRoles();
		List<Map<String,Object>> newList=new ArrayList<>();
		for(Map<String,Object> map:list) {
			if("0".equals(map.get("parentRoleId"))) {
				List<Map<String,Object>> childNode=getChildRoles(list,map.get("roleId").toString());
				map.put("childNode", childNode);
				newList.add(map);
			}
		}
		return newList;
	}
	
	@Override
	public List<Map<String, Object>> getAllDepts() {
		List<Map<String,Object>> list=selectMapper.getAllDepts();
		List<Map<String,Object>> newList=new ArrayList<>();
		for(Map<String,Object> map:list) {
			if("0".equals(map.get("parentDeptKey"))) {
				List<Map<String,Object>> childNode=getChildNode(list,map.get("departmentKey").toString());
				map.put("childNode", childNode);
				newList.add(map);
			}
		}
		return newList;
	}

	@Override
	public List<Map<String, Object>> getAllMenus() {
		List<Map<String,Object>> list=selectMapper.getAllMenus();
		List<Map<String,Object>> newList=new ArrayList<>();
		for(Map<String,Object> map:list) {
			if("0".equals(map.get("parentId"))) {
				List<Map<String,Object>> childNode=getMenuTree(list,map.get("menuId").toString());
				map.put("childMenu", childNode);
				newList.add(map);
			}
		}
		return newList;
	}

	@Override
	public List<Map<String, Object>> getAllUsers() {
		List<Map<String,Object>> list=selectMapper.getAllUsers();
		return list;
	}
	
	/**
	 * 子部门
	 * @param map
	 * @param id
	 * @return
	 */
	public List<Map<String,Object>> getChildNode(List<Map<String,Object>> map,String id){
		List<Map<String,Object>> newList=new ArrayList<>();
		for(Map<String,Object> map1:map) {
			if(id.equals(map1.get("parentDeptKey"))) {
				List<Map<String,Object>> childNode=getChildNode(map,map1.get("departmentKey").toString());
				map1.put("childNode", childNode);
				newList.add(map1);
			}
		}
		return newList;
	}
	/**
	 * 子菜单
	 * @param map
	 * @param id
	 * @return
	 */
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
	/**
	 * 子角色
	 * @param map
	 * @param id
	 * @return
	 */
	public List<Map<String,Object>> getChildRoles(List<Map<String,Object>> map,String id){
		List<Map<String,Object>> newList=new ArrayList<>();
		for(Map<String,Object> map1:map) {
			if(id.equals(map1.get("parentRoleId"))) {
				List<Map<String,Object>> childNode=getChildNode(map,map1.get("roleId").toString());
				map1.put("childNode", childNode);
				newList.add(map1);
			}
		}
		return newList;
	}
}
