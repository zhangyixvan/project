package com.bonc.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DeptMapper {
	public int addDept(@Param("id")String id,@Param("parentDeptKey")String parentDeptKey,
			@Param("departmentValue")String departmentValue,@Param("description")String description,
			@Param("createTime")String createTime,@Param("deleteState")String deleteState,@Param("ord")String ord);
	
	public Map<String,Object> getDeptById(@Param("id")String id);
	
	public String getDeptValueById(@Param("id")String parentDeptKey);
	
	public int updateDeptById(@Param("departmentValue")String departmentValue,@Param("id")String id,
			@Param("description")String description,@Param("deleteState")String deleteState,@Param("ord")String ord);
	
	public int deleteDeptById(@Param("departmentKey")String departmentKey);

	public List<Map<String,Object>> getUserDeptById(@Param("departmentKey")String departmentKey);
	
	public List<Map<String,Object>> getChildDeptById(@Param("departmentKey")String departmentKey);

	public int addDeptUser(List<Map<String, Object>> list);

	public int deleteDeptUserById(@Param("id")String departmentKey);

	public int addDeptRoleUser(List<Map<String, Object>> list);

	public int deleteDeptRoleUserById(@Param("departmentKey")String departmentKey);
	
	
	
	
	
//	public int getAllTest();
	
	
}
