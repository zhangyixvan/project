package com.bonc.service;

import java.util.List;
import java.util.Map;

public interface MenuService {
	
	public boolean addMenu(Map<String,Object> map);
	
	public Map<String,Object> getMenuById(String id);
	
	public boolean updateMenuById(Map<String,Object> map);
	
	public boolean deleteMenuById(String id);
	
	public List<Map<String,Object>> getMenuChildById(String id);
}
