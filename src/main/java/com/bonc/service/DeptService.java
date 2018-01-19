package com.bonc.service;

import java.util.Map;

public interface DeptService {
	
	public boolean addDept(String parentDeptKey,String departmentValue,String description,String createTime,String deleteState,String ord);
	
	public Map<String,Object> getDeptById(String id);
	
	public boolean updateDeptById(String id,String departmentValue,String description,String deleteState,String ord);
	
	public boolean deleteDeptById(String departmentKey);
}
