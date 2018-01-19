package com.bonc.system.shiro;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.bonc.mapper.LoginMapper;
import com.bonc.mapper.ShiroMapper;

import redis.clients.jedis.Jedis;


public class UserRealm extends AuthorizingRealm {
	
	
	private static final Logger log = LoggerFactory.getLogger(UserRealm.class);

	
	Jedis jedis=new Jedis("127.0.0.1",6379);
	
	@Autowired
	ShiroMapper shiroMapper;
	
	@Autowired
	LoginMapper loginMapper;
	/**
	 * 权限认证
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
		log.info("执行权限验证！");
		try {
			Map<String,Object> map = (Map<String, Object>) SecurityUtils.getSubject().getPrincipal();
			String userId=map.get("userId").toString();
			List<Map<String,Object>> roleList=loginMapper.getRoleIds(userId);
			List<String> roles=new ArrayList<String>();
			for(Map<String,Object> m:roleList) {
				roles.add(m.get("roleId").toString());
			}
			List<Map<String,Object>> list=shiroMapper.findPermissionByUid(roles);
			if(list==null||list.isEmpty()) {
				Set<String> perms = new HashSet<>();
				SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
				log.info("perms:"+perms);
				info.setStringPermissions(perms);
				return info;
			}
			Set<String> perms = new HashSet<>();
			for(Map<String,Object> map1 : list) {
				String perm=map1.get("perms").toString();
				if(!"".equals(perm)) {
					perms.add(perm);
				}
			}
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			log.info("perms:"+perms);
			info.setStringPermissions(perms);
			return info;
		} catch (Exception e) {
			log.info("权限验证失败！");
			return null;
		}
	}
	/**
	 * 登陆认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		log.info("执行登陆验证！");
		String username = (String) token.getPrincipal();
		String password = new String((char[]) token.getCredentials());
		// 查询用户信息
		Map<String,Object> user = shiroMapper.findByName(username);
		// 账号不存在
		if (user == null) {
			log.info("认证失败！账号不存在！");
			throw new UnknownAccountException("账号不存在");
		}
		if (!user.get("state").equals("0")) {
			log.info("认证失败！账号状态异常，请联系管理员开启账号！");
			throw new LockedAccountException("账号状态异常，请联系管理员开启账号！");
		}
//		System.out.println(password);
//		System.out.println(user.get("password"));
		// 密码错误
		if (!password.equals(user.get("password"))) {
			log.info("认证失败！账号或密码不正确！");
			throw new IncorrectCredentialsException("账号或密码不正确");
		}
//		System.out.println("user:"+user);
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, password, getName());
		user.remove("password");
		user.remove("createTime");
		Session session=SecurityUtils.getSubject().getSession();//成功则放入session
        session.setAttribute("user", user);
        log.info("user:"+user);
		return info;
	}

}
