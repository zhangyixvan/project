package com.bonc.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonc.mapper.LoginMapper;
import com.bonc.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService {
	@Autowired
	LoginMapper loginMapper;
	
	@Override
	public List<Map<String, Object>> getAllMenu(String userId) {
		List<Map<String,Object>> list=new ArrayList<>();
		List<Map<String,Object>> roleList=loginMapper.getRoleIds(userId);
		List<String> roles=new ArrayList<String>();
		for(Map<String,Object> m:roleList) {
			roles.add(m.get("roleId").toString());
		}
		if(roles==null||roles.isEmpty()) {
			return null;
		}
		list=loginMapper.getAllMenu(roles);
		if(list==null||list.isEmpty()) {
			return null;
		}
		//排序
		List<Map<String,Object>> newList=new ArrayList<>();
		for(Map<String,Object> map:list) {
			String parentId=map.get("parentId").toString();
			if("0".equals(parentId)) {
				String id=map.get("menuId").toString();
				map.put("childMenu", getChildMenu(list,id));
				newList.add(map);
			}
		}
		return newList;
	}
	public List<Map<String,Object>> getChildMenu(List<Map<String,Object>> list,String id){
		List<Map<String,Object>> newList=new ArrayList<>();
		for(Map<String,Object> map:list) {
			String parentId=map.get("parentId").toString();
			if(id.equals(parentId)) {
				String menuId=map.get("menuId").toString();
				map.put("childMenu", getChildMenu(list,menuId));
				newList.add(map);
			}
		}
		return newList;
	}
	
}
