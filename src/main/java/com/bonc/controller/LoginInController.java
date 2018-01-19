package com.bonc.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.session.InvalidSessionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bonc.mapper.LoginMapper;
import com.bonc.service.LoginService;
import com.bonc.service.ShiroService;
import com.bonc.shiro.admin.entity.JsonResultNew;
import com.bonc.shiro.admin.entity.Md5Util;
import com.bonc.shiro.admin.entity.MyBusinessEnum;
import com.bonc.shiro.admin.entity.MySystemEnum;



/**
 * 登录模块
 * @author yixuan.zhang
 * @date 2018年1月16日
 *
 */
@RestController
@RequestMapping("/loginIn")
public class LoginInController {
	
	private static final Logger log = LoggerFactory.getLogger(LoginInController.class);

	@Autowired
	ShiroService shiroService;
	
	@Autowired
	LoginMapper loginMapper;
	
	@Autowired
	LoginService loginService;
	
	
   @RequestMapping("login")
   public JsonResultNew<Object> login(HttpServletRequest req){
	   String username=req.getParameter("userName");
	   String password=req.getParameter("password");
	   if(username==null||"".equals(username)||password==null||"".equals(password)) {
		   return new JsonResultNew<>("数据传入错误！");
	   }
       try {
	       log.info(username+":"+password);
	       UsernamePasswordToken token = new UsernamePasswordToken(username, Md5Util.encoding(password));
           SecurityUtils.getSubject().login(token);
           Map<String,Object> user=(Map<String, Object>) SecurityUtils.getSubject().getSession().getAttribute("user");
//			String userId=user.get("userId").toString();
//			List<Map<String,Object>> list=loginMapper.getRoleIds(userId);
//			user.put("roles", list);
    	   return new JsonResultNew<>(user);
       } catch (Exception e) {
    	   return new JsonResultNew<>(MySystemEnum.SYSTEM_SUCCESS, "登陆验证失败！"+e.getMessage(), MyBusinessEnum.BUSINESS_ERROR, null);
       }
   }
   /**
    * 获取当前登陆用户所有的菜单
    * @param req
    * @param res
    * @return
    */
   @RequestMapping("getAllMenu")
   @RequiresAuthentication
   public JsonResultNew<Object> getALL(HttpServletRequest req, HttpServletResponse res){
	   log.info("success in getAllMenu!");
	   	try {
			Map<String,Object> user=(Map<String, Object>) SecurityUtils.getSubject().getSession().getAttribute("user");
			String userId=user.get("userId").toString();
			List<Map<String,Object>> list=loginService.getAllMenu(userId);
			log.info("userId:"+userId);
			if(list==null||list.isEmpty()) {
				return new JsonResultNew<>(MyBusinessEnum.BUSINESS_ERROR, "这个用户没有相关的菜单！");
			}
			return new JsonResultNew<>(list);
		} catch (InvalidSessionException e) {
			return new JsonResultNew<>(MySystemEnum.SYSTEM_SUCCESS, "获取菜单失败！", MyBusinessEnum.BUSINESS_ERROR, null);
		}
   }
//   /**
//    * 获取当前登陆用户的信息
//    * @param req
//    * @param res
//    * @return
//    */
//   @RequestMapping("getUser")
//   public JsonResultNew<Object> getALLNew(HttpServletRequest req, HttpServletResponse res){
//	   
//   }
   
   @RequestMapping("pleaseLogin")
   public JsonResultNew<Object> pleaseLogin(HttpServletRequest req, HttpServletResponse res){
	   log.info("please Login");
       return new JsonResultNew<>("登陆失效或未登陆，请登陆");
   }
   
   @RequestMapping("updatePermission")
   @RequiresAuthentication
   public JsonResultNew<Object> updatePermission(HttpServletRequest req, HttpServletResponse res){
	   	try {
			shiroService.updatePermission();
			return new JsonResultNew<Object>(MySystemEnum.SYSTEM_SUCCESS, "重新加载成功！", MyBusinessEnum.BUSINESS_SUCCESS, null);
		} catch (Exception e) {
			return new JsonResultNew<Object>(MySystemEnum.SYSTEM_SUCCESS, "重新加载失败！", MyBusinessEnum.BUSINESS_ERROR, null);
		}
   }
   
   
//   @RequestMapping("error")
//   public JsonResultNew<Object> error(HttpServletRequest req, HttpServletResponse res){
//       return new JsonResultNew<>("没有相关的权限");
//   }
   @RequestMapping("error")
   public JsonResultNew<Object> error(HttpServletRequest req, HttpServletResponse res){
	   return new JsonResultNew<Object>(MySystemEnum.SYSTEM_SUCCESS, "没有相关的权限！", MyBusinessEnum.BUSINESS_ERROR, null);
   }
   	
}