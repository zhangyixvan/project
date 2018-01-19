package com.bonc.service;

import java.util.List;
import java.util.Map;

public interface UserService {
	public Map<String, Object> getUser(String userName);
	
	public boolean addUser(Map<String,Object> map);
	
	public Map<String,Object> getUserById(String userId);

	public boolean updateUserById(Map<String, Object> map);
	
	public List<Map<String, Object>> getUserPermsById(String userId);

	public boolean updateUserPermsById(String userId, String roleId, String state);

	public boolean deleteUserById(String userId);

	public List<Map<String, Object>> getAllDeptByUserId(String userId);

	public List<Map<String, Object>> getAllUserByUserId(String userId);

	public List<Map<String, Object>> getUserDeptsById(String userId);

	public boolean updateUserDeptsById(String userId, String departmentKeys, String state);

	public boolean updatePasswordByUserId(String userId, String password);

	public List<Map<String, Object>> getAllUserByDeptNo(String userId,String departmentKey);

	public Map<String, Object> getUserDeptPermsById(String userId, String departmentKey);

	public boolean updateUserDeptPermsById(String userId, String departmentKey, String roleId);



	
}
