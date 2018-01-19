package com.bonc.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MenuMapper {
	
	public int addMenu(Map<String,Object> map);
	
	public Map<String,Object> getMenuById(@Param("menuId")String id);
	public String getMenuNameById(@Param("menuId")String id);
	
	public int updateMenuById(Map<String,Object> map);
	
	public int deleteMenuById(@Param("menuId")String id);
	public int deleteRoleMenuById(@Param("menuId")String id);
	
	public int addRoleMenu(List<Map<String, Object>> list);
	
	public List<Map<String,Object>> getMenuChildById(@Param("menuId")String id);
}
