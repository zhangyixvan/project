package com.bonc.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;



@Mapper
public interface ShiroMapper {
	Map<String,Object> findByName(@Param("name")String name);
	
	List<Map<String,Object>> findPermissionByUid(List<String> list);
	
	List<Map<String,String>> getFilterChain();

//	List<String> findRolesByUserId(@Param("userId")String userId);
	
}
