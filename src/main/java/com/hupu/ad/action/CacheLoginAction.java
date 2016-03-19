package com.hupu.ad.action;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.hupu.ad.util.CacheManagerLoginUtils;

/**
 * 用于登录action
 * @author zzy
 */
@Controller
public class CacheLoginAction {
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	/**
	 * 跳转到登录页面
	 * @param adId 广告ID
	 */
	@RequestMapping(value="login", method=RequestMethod.GET)
	public String login(Model model){
		return "login";
	}
	
	/**
	 * 登录验证
	 * @param adId 广告ID
	 */
	@RequestMapping(value="login-valid", method=RequestMethod.POST)
	public String dealLogin(@RequestParam String username,@RequestParam String password,Model model, HttpSession session){
		if(!CacheManagerLoginUtils.getUsername().equals(username)) {
			model.addAttribute("error_user", "用户名输入错误");
			return "redirect:/login";
		}else if(!CacheManagerLoginUtils.getPassword().equals(password)) {
			model.addAttribute("error_pass", "密码输入错误");
			return "redirect:/login";
		}else {
			session.setAttribute("USER", username);
			return "redirect:/cache/adlist";
		}
	}
	
}
