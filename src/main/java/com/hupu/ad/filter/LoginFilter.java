package com.hupu.ad.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

public class LoginFilter implements Filter {
	
	FilterConfig filterConfig;

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest requestHttp = (HttpServletRequest) request;
		HttpServletResponse responseHttp = (HttpServletResponse) response;
		String requestUri = requestHttp.getRequestURI();
		
		if(StringUtils.contains(requestUri, "/cache/logs")){
			chain.doFilter(requestHttp, responseHttp);
			return ;
		}
		
		if(StringUtils.contains(requestUri, "/cache") && requestHttp.getSession().getAttribute("USER") == null){
			responseHttp.setCharacterEncoding("utf-8");
			responseHttp.sendRedirect(requestHttp.getContextPath() + "/login");
			return ;
		}
		chain.doFilter(requestHttp, responseHttp);
	}

	public void destroy() {

	}
}
