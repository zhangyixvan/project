package com.bonc.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonc.mapper.MenuMapper;
import com.bonc.mapper.SelectMapper;
import com.bonc.service.MenuService;

@Service
public class MenuServiceImpl implements MenuService {
	@Autowired
	MenuMapper menuMapper;
	@Autowired
	SelectMapper selectMapper;
	
	@Override
	public boolean addMenu(Map<String, Object> map) {
		String menuId=UUID.randomUUID().toString();
		map.put("menuId", menuId);
		int result1=menuMapper.addMenu(map);
		List<Map<String,Object>> list=selectMapper.getAllRoles();
		List<Map<String,Object>> newList=new ArrayList<>(); 
		for(Map<String,Object> role:list) {
			Map<String,Object> newMap=new HashMap<>();
			newMap.put("roleId", role.get("roleId").toString());
			newMap.put("menuId", menuId);
			newMap.put("deleteState", "1");
			newList.add(newMap);
		}
		int result2=menuMapper.addRoleMenu(newList);
		if(result1>0&&result2>0) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public Map<String, Object> getMenuById(String id) {
		Map<String, Object> map=menuMapper.getMenuById(id);
		String parentId=(String) map.get("parentId");
		if("0".equals(parentId)) {
			map.put("parentMenuName", "");
			return map;
		}
		String menuName=menuMapper.getMenuNameById(parentId);
		map.put("parentMenuName", menuName);
		return map;
	}

	@Override
	public boolean updateMenuById(Map<String, Object> map) {
		int i=menuMapper.updateMenuById(map);
		if(i>0) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public boolean deleteMenuById(String id) {
		int i1=menuMapper.deleteMenuById(id);
		int i2=menuMapper.deleteRoleMenuById(id);
		if(i1>0&&i2>0) {
			return true;
		}else {
			return false;
		}
	}
	
	@Override
	public List<Map<String,Object>> getMenuChildById(String id){
		return menuMapper.getMenuChildById(id);
	}
}
