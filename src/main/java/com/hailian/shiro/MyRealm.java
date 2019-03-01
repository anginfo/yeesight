package com.hailian.shiro;

import org.apache.poi.ss.formula.functions.T;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.hailian.entity.SysUser;
import com.hailian.entity.User;
import com.hailian.service.ISysUserService;
import com.hailian.utils.EncryptUtils;

/**
 * @author bjjoy
 */
@Component
public class MyRealm extends AuthorizingRealm {

	public MyRealm(){
		super(new AllowAllCredentialsMatcher());
        setAuthenticationTokenClass(UsernamePasswordToken.class);

        //FIXME: 暂时禁用Cache
        setCachingEnabled(false);
	}

	/*@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private MenuService menuService;*/
	@Autowired
	public ISysUserService iSysUserService;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		SysUser user = (SysUser) principals.getPrimaryPrincipal();
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		/*User dbUser = userService.findUserByName(user.getLoginName());
		List<Role> roleList = roleService.getListByUserId(user.getId());
		Set<String> shiroPermissions = new HashSet<>();
		Set<String> roleSet = new HashSet<String>();
		for (Role role : roleList) {
			List<Menu> menus = menuService.getListByRoleId(role.getId());
			for (Menu menu : menus) {
				shiroPermissions.add(menu.getPermission());

			}
			roleSet.add(role.getEnname());
		}
		authorizationInfo.setRoles(roleSet);
		authorizationInfo.setStringPermissions(shiroPermissions);*/
		return authorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		String loginName = (String) token.getPrincipal();
		EntityWrapper<SysUser> wrapper = new EntityWrapper<SysUser>();
		wrapper.where("del_flag={0}",0);
		wrapper.eq("login_name", loginName);
		SysUser user = iSysUserService.selectOne(wrapper);
		String password = new String((char[]) token.getCredentials());

		// 账号不存在
		if (user == null) {
			throw new UnknownAccountException("账号或密码不正确");
		}
		// 密码错误
		if (!EncryptUtils.encryptMD5(password).equals(user.getPassword())) {
			throw new IncorrectCredentialsException("账号或密码不正确");
		}
		// 账号锁定
	/*	if (user.getState() == 0) {
			throw new LockedAccountException("账号已被锁定,请联系管理员");
		}*/

		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, password, getName());

		return info;
	}

}
