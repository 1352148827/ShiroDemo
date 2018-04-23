package com.nlzh.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nlzh.exception.CustomException;

@Controller
@RequestMapping(value="/user")
public class UserAction {

	/**
	 * 用户登录
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/login")
	public String login(HttpServletRequest request)throws Exception{
		
		// 如果登录失败从request中获取异常信息,shiroLoginFailure是shiro异常类的全限定名	
		// shiro在认证过程中出现错误后将异常类路径通过request返回
		String exceptionClassName = (String) request
				.getAttribute("shiroLoginFailure");
		if(exceptionClassName!=null){
			if (UnknownAccountException.class.getName().equals(exceptionClassName)) {
				throw new CustomException("账号不存在");
			} else if (IncorrectCredentialsException.class.getName().equals(
					exceptionClassName)) {
				throw new CustomException("用户名/密码错误");
			} else if("randomCodeError".equals(exceptionClassName)){
				throw new CustomException("验证码错误");
			} else{
				throw new Exception();//最终在异常处理器生成未知错误
			}
		}
		
		
		
		
		// 登录失败到的页面（注：此方法不处理登录成功，shiro登录成功会跳转到上一个请求路径）
		return "login";
	}
	
	
	
}
