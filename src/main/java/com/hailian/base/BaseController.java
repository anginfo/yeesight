package com.hailian.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.hailian.conf.Constant;
import com.hailian.domain.User;
import com.hailian.entity.SysUser;

public class BaseController implements Constant{
	@Autowired
	public JdbcTemplate jdbcTemplate;
	public void clearSessionUser(HttpServletRequest request,HttpServletResponse response,String key){
		request.getSession().setAttribute(key, null);
	}
	
	/**
	 * 设置session
	 * */
	public void setSession(HttpServletRequest request,HttpServletResponse response,String key,Object value) {
		request.getSession().setAttribute(key, value);
		if("datamart_user".equals(key)){
			request.getSession().setMaxInactiveInterval(3600);
		}
	}
	/**
	 * 获取session
	 * @return 
	 * */
	public Object getSession(HttpServletRequest request,HttpServletResponse response,String key) {
		return request.getSession().getAttribute(key);
	}
	/**
	 * 带参重定向
	 *
	 * @param path
	 * @return
	 */
	public String redirect(String path) {
		return "redirect:" + path;
	}
	/**
	 * 获取当前登录人
	 * @param request
	 * @param response
	 * @return
	 */
	public User getLoginUser(HttpServletRequest request){
		User user=(User) request.getSession().getAttribute(USER_INFO);
		if(user==null)
			user=new User();
		return user;
	}
	/**
	 * @time   2018年9月26日 上午11:24:33
	 * @author zuoqb
	 * @todo   判断是否为超级管理员
	 * @return_type   boolean
	 */
	public boolean isAdmin(HttpServletRequest request){
		User user=(User) request.getSession().getAttribute(USER_INFO);
		if(user==null)
			return false;
		if(!"1".equals(user.getUserType()))
			return false;
		return true;
	}
	/**
	 * @time   2018年9月30日 下午5:09:34
	 * @author zuoqb
	 * @todo  设置session
	 */
	public void setSessionUser(HttpServletRequest request,HttpServletResponse response,SysUser user){
		setSession(request, response, USER_INFO, user);
	}
	
}
