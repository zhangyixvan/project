package com.bonc.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RoleMapper {

	public int addRole(@Param("roleId")String roleId,@Param("roleName")String roleName,@Param("remarks")String remarks,@Param("parentRoleId")String parentRoleId);

	public int addRoleMenu(List<Map<String, Object>> list);
	
	public int addUserRole(List<Map<String, Object>> list);
	
	public Map<String,Object> getRoleById(@Param("roleId")String id);
	
	public List<Map<String,Object>> getRoleMenu(@Param("roleId")String id);
	
	public int updateRoleById(@Param("roleId")String roleId,@Param("roleName")String roleName, @Param("remarks")String remarks,@Param("parentRoleId")String parentRoleId);

	public int updateRoleMenuById(List<Map<String,Object>> list);
	
	public List<Map<String,Object>> getUserRole(@Param("roleId")String roleId);

	public int deleteRoleById(@Param("roleId")String roleId);

	public int deleteRoleMenuById(@Param("roleId")String roleId);

	public int deleteRoleUserById(@Param("roleId")String roleId);

	public String getParentRoleName(@Param("parentRoleId")String parentRoleId);

	public List<Map<String,Object>> getChildRole(@Param("roleId")String roleId);
	
	
}
