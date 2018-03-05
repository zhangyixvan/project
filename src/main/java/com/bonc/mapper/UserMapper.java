package com.bonc.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
	public Map<String, Object> getUser(@Param("userName")String userName);
	
	public int addUser(Map<String,Object> map);

	public int addUserDept(List<Map<String, Object>> list);
	
	public int addUserRole(List<Map<String,Object>> list);
	
	public Map<String,Object> getUserById(@Param("userId")String userId);

	public int updateUserById(Map<String, Object> map);
	
	public int updateUserDeptById(@Param("userId")String userId,@Param("deptNo")String deptNo);

	public List<Map<String, Object>> getUserPermsById(@Param("userId")String userId);
	
	public int updateUserPermsById(@Param("userId")String userId, @Param("roleId")String roleId, @Param("state")String state);

	public int deleteUserById(@Param("userId")String userId);

	public int deleteUserDeptById(@Param("userId")String userId);

	public int deleteUserRoleById(@Param("userId")String userId);

	public List<Map<String, Object>> getAllDeptByUserId(@Param("userId")String userId);

	public List<Map<String, Object>> getChild(@Param("departmentKey")String departmentKey);

	public List<Map<String, Object>> getUserDepts(List<String> list);

	public List<Map<String, Object>> getUsers(List<String> list);

	public List<Map<String, Object>> getUserDeptsById(@Param("userId")String userId);

	public int updateUserDeptsById(@Param("userId")String userId, @Param("departmentKey")String departmentKey, @Param("state")String state, @Param("isAdmin")String isAdmin);

	public List<Map<String, Object>> getUserDeptByUserId(@Param("userId")String userId);

	public int updatePasswordByUserId(@Param("userId")String userId, @Param("password")String password);

	public List<Map<String, Object>> getAllUserByDeptNo(@Param("userId")String userId,@Param("departmentKey")String departmentKey);

	public int addUserRoleDept(List<Map<String, Object>> deptList);

	public int updateUserDeptRoleById(@Param("userId")String userId, @Param("departmentKey")String departmentKey, @Param("state")String state);

	public Map<String, Object> getUserDeptPermsById(@Param("userId")String userId, @Param("departmentKey")String departmentKey);

	public int updateUserDeptPermsById(@Param("userId")String userId, @Param("departmentKey")String departmentKey, @Param("roleId")String roleId);

	public int deleteUserDeptRoleById(@Param("userId")String userId);

	public Map<String, Object> getRoleIdByUidDid(@Param("userId")String userId, @Param("departmentKey")String departmentKey);

	public List<Map<String, Object>> getChildRole(@Param("id")String id);

	public List<Map<String, Object>> getAllUserByRlist(@Param("departmentKey")String departmentKey, @Param("roleId")String roleId);

}
