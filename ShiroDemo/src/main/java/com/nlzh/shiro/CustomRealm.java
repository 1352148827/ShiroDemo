package com.nlzh.shiro;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.nlzh.pojo.ActiveUser;
import com.nlzh.pojo.SysPermission;
import com.nlzh.pojo.SysUser;
import com.nlzh.service.SysService;

public class CustomRealm extends AuthorizingRealm{
	
	@Autowired
	private SysService sysService;
	
	/**
	 * 用于认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		
		// token 存放用户名和密码
		// 1 获取用户名
		String userCode = (String) token.getPrincipal();
		
		// 根据用户名向数据库中查询用户
		SysUser sysUser = null;
		try {
			sysUser = sysService.findSysUserByUserCode(userCode);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		if(sysUser == null){
			return null;
		}
		
		// 获取密码
		String password = sysUser.getPassword();
		
		// 获取盐
		String salt = sysUser.getSalt();
		
		// 设置ActiveUser对象
		ActiveUser activeUser = new ActiveUser();
		activeUser.setUserid(sysUser.getId());
		activeUser.setUsercode(sysUser.getUsercode());
		activeUser.setUsername(sysUser.getUsername());
		
		
		List<SysPermission> menus = null;
		try {
			// 通过userid获取菜单
			menus = sysService.findMenuListByUserId(sysUser.getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 将用户菜单设置到
		activeUser.setMenus(menus);
		
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(activeUser, password, salt);
		
		return info;
	}
	

	/**
	 * 用于授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		ActiveUser activeUser = (ActiveUser) principals.getPrimaryPrincipal();
		 
		//根据身份信息获取权限信息
		//从数据库获取到权限数据
		List<SysPermission> permissionList = null;
		try {
			permissionList = sysService.findPermissionListByUserId(activeUser.getUserid());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//单独定一个集合对象 
		List<String> permissions = new ArrayList<String>();
		if(permissionList!=null){
			for(SysPermission sysPermission:permissionList){
				//将数据库中的权限标签 符放入集合
				permissions.add(sysPermission.getPercode());
			}
		}
		
		//查到权限数据，返回授权信息(要包括 上边的permissions)
		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
		//将上边查询到授权信息填充到simpleAuthorizationInfo对象中
		simpleAuthorizationInfo.addStringPermissions(permissions);
		
		return simpleAuthorizationInfo;
	}
	
	//清除缓存
	public void clearCached() {
		PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
		super.clearCache(principals);
	}

	

}
