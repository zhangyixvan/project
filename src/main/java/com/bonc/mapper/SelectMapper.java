package com.bonc.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SelectMapper {
	
	public List<Map<String,Object>> getAllRoles();
	
	public List<Map<String,Object>> getAllDepts();
	
	public List<Map<String,Object>> getAllMenus();

	public List<Map<String, Object>> getAllUsers();
}
