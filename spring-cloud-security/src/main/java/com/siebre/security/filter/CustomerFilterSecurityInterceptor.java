package com.siebre.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

public class CustomerFilterSecurityInterceptor extends FilterSecurityInterceptor {
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		
		if (StringUtils.equals(httpServletRequest.getMethod(), "OPTIONS")) {
			chain.doFilter(request, response);
			return;
		}
		
		super.doFilter(request, response, chain);
	}
}
