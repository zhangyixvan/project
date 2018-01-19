package com.bonc.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LoginMapper {

	public List<Map<String,Object>> getRoleIds(@Param("userId")String userId);

	public List<Map<String, Object>> getAllMenu(List<String> list);
	
}
