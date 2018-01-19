package com.bonc.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bonc.shiro.admin.entity.JsonResultNew;
import org.apache.shiro.authc.AuthenticationException;

@RestControllerAdvice
public class ExceptionController {
	/**
     * 登录认证异常
     */
    @ExceptionHandler({UnauthenticatedException.class,AuthenticationException.class})
    public JsonResultNew<Object> authenticationException(HttpServletRequest request, HttpServletResponse response, Exception e) {
        return new JsonResultNew<>("未登陆！或登录失效！");
    }
	/**
	 * 权限认证失败
	 * @param req
	 * @param response
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = UnauthorizedException.class)//处理访问方法时权限不足问题
    public JsonResultNew<Object> defaultErrorHandler(HttpServletRequest req, HttpServletResponse response, Exception e)  {
		return new JsonResultNew<>("没有相关的权限！");
	}
}