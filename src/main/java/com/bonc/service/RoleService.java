package com.bonc.service;

import java.util.List;
import java.util.Map;

public interface RoleService {
	
	public boolean addRole(String roleName,String remarks,String parentRoleId);
	
	public Map<String,Object> getRoleById(String id);
	
	public boolean updateRoleById(String roleId,String roleName,String remarks,String parentRoleId);
	
	public List<Map<String,Object>> getRoleMenuById(String id);
	
	public boolean updateRoleMenuById(List<Map<String,Object>> list);
	
	public boolean deleteRoleById(String roleId);
}
